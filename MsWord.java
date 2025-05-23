import javax.swing.*;
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class MsWord extends JFrame {

    private JTextPane editor;
    private JFileChooser filePicker;
    private JComboBox<String> fontSelector;
    private JComboBox<Integer> sizeSelector;

    public MsWord() {
        setTitle("Mini Word");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        editor = new JTextPane();
        add(new JScrollPane(editor), BorderLayout.CENTER);

        setupMenu();
        setupToolbar();

        filePicker = new JFileChooser();
        filePicker.setFileFilter(new FileNameExtensionFilter("Rich Text Format (*.rtf)", "rtf"));
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem closeItem = new JMenuItem("Exit");

        openItem.addActionListener(e -> handleOpen());
        saveItem.addActionListener(e -> handleSave());
        closeItem.addActionListener(e -> System.exit(0));

        file.add(openItem);
        file.add(saveItem);
        file.addSeparator();
        file.add(closeItem);

        menuBar.add(file);
        setJMenuBar(menuBar);
    }

    private void setupToolbar() {
        JToolBar tools = new JToolBar();

        fontSelector = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontSelector.setMaximumSize(new Dimension(150, 25));
        fontSelector.addActionListener(e -> applyTextStyle());

        sizeSelector = new JComboBox<>(new Integer[]{12, 14, 16, 18, 20, 24, 28, 32, 36});
        sizeSelector.setMaximumSize(new Dimension(60, 25));
        sizeSelector.setSelectedItem(16);
        sizeSelector.addActionListener(e -> applyTextStyle());

        JButton boldBtn = createFormatButton("B", StyleConstants.Bold);
        JButton italicBtn = createFormatButton("I", StyleConstants.Italic);
        JButton underlineBtn = createFormatButton("U", StyleConstants.Underline);

        JButton colorBtn = new JButton("Color");
        colorBtn.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Choose Text Color", editor.getForeground());
            if (chosen != null) {
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setForeground(attr, chosen);
                editor.setCharacterAttributes(attr, false);
            }
        });

        tools.add(fontSelector);
        tools.add(sizeSelector);
        tools.addSeparator();
        tools.add(boldBtn);
        tools.add(italicBtn);
        tools.add(underlineBtn);
        tools.addSeparator();
        tools.add(colorBtn);

        add(tools, BorderLayout.NORTH);
    }

    private JButton createFormatButton(String label, Object styleAttr) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.addActionListener(e -> toggleTextAttribute(styleAttr));
        return btn;
    }

    private void toggleTextAttribute(Object attribute) {
        StyledDocument doc = editor.getStyledDocument();
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();

        if (start == end) return;

        AttributeSet currentAttr = doc.getCharacterElement(start).getAttributes();
        MutableAttributeSet newAttr = new SimpleAttributeSet(currentAttr);

        boolean isActive = false;
        if (attribute == StyleConstants.Bold) isActive = StyleConstants.isBold(currentAttr);
        if (attribute == StyleConstants.Italic) isActive = StyleConstants.isItalic(currentAttr);
        if (attribute == StyleConstants.Underline) isActive = StyleConstants.isUnderline(currentAttr);

        if (attribute == StyleConstants.Bold) StyleConstants.setBold(newAttr, !isActive);
        if (attribute == StyleConstants.Italic) StyleConstants.setItalic(newAttr, !isActive);
        if (attribute == StyleConstants.Underline) StyleConstants.setUnderline(newAttr, !isActive);

        doc.setCharacterAttributes(start, end - start, newAttr, false);
    }

    private void applyTextStyle() {
        String font = (String) fontSelector.getSelectedItem();
        int size = (int) sizeSelector.getSelectedItem();

        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attr, font);
        StyleConstants.setFontSize(attr, size);
        editor.setCharacterAttributes(attr, false);
    }

    private void handleOpen() {
        if (filePicker.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = filePicker.getSelectedFile();
            try (FileInputStream input = new FileInputStream(selectedFile)) {
                editor.getStyledDocument().remove(0, editor.getDocument().getLength());
                new RTFEditorKit().read(input, editor.getDocument(), 0);
            } catch (Exception e) {
                showAlert("Could not open the file.");
            }
        }
    }

    private void handleSave() {
        if (filePicker.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = filePicker.getSelectedFile();
            try (FileOutputStream output = new FileOutputStream(file)) {
                new RTFEditorKit().write(output, editor.getDocument(), 0, editor.getDocument().getLength());
            } catch (Exception e) {
                showAlert("Could not save the file.");
            }
        }
    }

    private void showAlert(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MsWord().setVisible(true));
    }
}
