import javax.swing.*;
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class MsWord extends JFrame {

    private JTextPane textPane;
    private JFileChooser fileChooser;
    private JComboBox<String> fontBox;
    private JComboBox<Integer> sizeBox;

    public MsWord() {
        setTitle("Mini Word");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        createMenuBar();
        createToolBar();

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Rich Text Format (*.rtf)", "rtf"));
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");

        open.addActionListener(e -> openFile());
        save.addActionListener(e -> saveFile());
        exit.addActionListener(e -> System.exit(0));

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();

        // Font selector
        fontBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontBox.setMaximumSize(new Dimension(150, 25));
        fontBox.addActionListener(e -> setFontStyle());

        // Size selector
        sizeBox = new JComboBox<>(new Integer[]{12, 14, 16, 18, 20, 24, 28, 32, 36});
        sizeBox.setMaximumSize(new Dimension(60, 25));
        sizeBox.setSelectedItem(16);
        sizeBox.addActionListener(e -> setFontStyle());

        // Style buttons
        JButton bold = createStyleButton("B", StyleConstants.Bold);
        JButton italic = createStyleButton("I", StyleConstants.Italic);
        JButton underline = createStyleButton("U", StyleConstants.Underline);

        // Color chooser
        JButton colorBtn = new JButton("Color");
        colorBtn.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Choose Text Color", textPane.getForeground());
            if (color != null) {
                MutableAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setForeground(attrs, color);
                textPane.setCharacterAttributes(attrs, false);
            }
        });

        toolBar.add(fontBox);
        toolBar.add(sizeBox);
        toolBar.addSeparator();
        toolBar.add(bold);
        toolBar.add(italic);
        toolBar.add(underline);
        toolBar.addSeparator();
        toolBar.add(colorBtn);

        add(toolBar, BorderLayout.NORTH);
    }

    private JButton createStyleButton(String text, Object styleConstant) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.addActionListener(e -> toggleStyle(styleConstant));
        return button;
    }

    private void toggleStyle(Object styleConstant) {
        StyledDocument doc = textPane.getStyledDocument();
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if (start == end) return;

        MutableAttributeSet attrs = new SimpleAttributeSet(doc.getCharacterElement(start).getAttributes());

        boolean current = StyleConstants.isBold(attrs);
        if (styleConstant == StyleConstants.Italic) current = StyleConstants.isItalic(attrs);
        if (styleConstant == StyleConstants.Underline) current = StyleConstants.isUnderline(attrs);

        if (styleConstant == StyleConstants.Bold) StyleConstants.setBold(attrs, !current);
        if (styleConstant == StyleConstants.Italic) StyleConstants.setItalic(attrs, !current);
        if (styleConstant == StyleConstants.Underline) StyleConstants.setUnderline(attrs, !current);

        doc.setCharacterAttributes(start, end - start, attrs, false);
    }

    private void setFontStyle() {
        String font = (String) fontBox.getSelectedItem();
        int size = (int) sizeBox.getSelectedItem();

        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs, font);
        StyleConstants.setFontSize(attrs, size);
        textPane.setCharacterAttributes(attrs, false);
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile())) {
                textPane.getStyledDocument().remove(0, textPane.getStyledDocument().getLength());

            } catch (Exception ex) {
                showError("Failed to open file.");
            }
        }
    }

    private void saveFile() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile())) {
            } catch (Exception ex) {
                showError("Failed to save file.");
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MsWord().setVisible(true));
    }
}
