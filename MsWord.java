import javax.swing.*;
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class SimpleWord extends JFrame {

    private JTextPane editor;
    private JFileChooser chooser;
    private JComboBox<String> fontSelector;
    private JComboBox<Integer> sizeSelector;

    public SimpleWord() {
        setTitle("Simple Word");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        editor = new JTextPane();
        add(new JScrollPane(editor), BorderLayout.CENTER);

        setupMenu();
        setupToolbar();

        chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Rich Text Format (*.rtf)", "rtf"));
    }

    private void setupMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");

        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem close = new JMenuItem("Exit");

        open.addActionListener(e -> loadFile());
        save.addActionListener(e -> saveFile());
        close.addActionListener(e -> System.exit(0));

        file.add(open);
        file.add(save);
        file.addSeparator();
        file.add(close);

        bar.add(file);
        setJMenuBar(bar);
    }

    private void setupToolbar() {
        JToolBar tools = new JToolBar();

        fontSelector = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontSelector.setMaximumSize(new Dimension(150, 25));
        fontSelector.addActionListener(e -> applyFont());

        sizeSelector = new JComboBox<>(new Integer[]{12, 14, 16, 18, 20, 24, 28, 32});
        sizeSelector.setMaximumSize(new Dimension(60, 25));
        sizeSelector.setSelectedItem(16);
        sizeSelector.addActionListener(e -> applyFont());

        JButton bold = formatButton("B", StyleConstants.Bold);
        JButton italic = formatButton("I", StyleConstants.Italic);
        JButton underline = formatButton("U", StyleConstants.Underline);

        JButton color = new JButton("Color");
        color.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Select Color", editor.getForeground());
            if (c != null) {
                MutableAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setForeground(attr, c);
                editor.setCharacterAttributes(attr, false);
            }
        });

        tools.add(fontSelector);
        tools.add(sizeSelector);
        tools.addSeparator();
        tools.add(bold);
        tools.add(italic);
        tools.add(underline);
        tools.addSeparator();
        tools.add(color);

        add(tools, BorderLayout.NORTH);
    }

    private JButton formatButton(String label, Object attrKey) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.addActionListener(e -> toggleStyle(attrKey));
        return btn;
    }

    private void toggleStyle(Object key) {
        StyledDocument doc = editor.getStyledDocument();
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        if (start == end) return;

        MutableAttributeSet attr = new SimpleAttributeSet(doc.getCharacterElement(start).getAttributes());
        boolean state = false;

        if (key == StyleConstants.Bold) {
            state = StyleConstants.isBold(attr);
            StyleConstants.setBold(attr, !state);
        } else if (key == StyleConstants.Italic) {
            state = StyleConstants.isItalic(attr);
            StyleConstants.setItalic(attr, !state);
        } else if (key == StyleConstants.Underline) {
            state = StyleConstants.isUnderline(attr);
            StyleConstants.setUnderline(attr, !state);
        }

        doc.setCharacterAttributes(start, end - start, attr, false);
    }

    private void applyFont() {
        String font = (String) fontSelector.getSelectedItem();
        int size = (int) sizeSelector.getSelectedItem();

        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attr, font);
        StyleConstants.setFontSize(attr, size);
        editor.setCharacterAttributes(attr, false);
    }

    private void loadFile() {
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileInputStream in = new FileInputStream(chooser.getSelectedFile())) {
                editor.read(in, null);
            } catch (Exception ex) {
                alert("Unable to load file");
            }
        }
    }

    private void saveFile() {
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileOutputStream out = new FileOutputStream(chooser.getSelectedFile())) {
                editor.write(out);
            } catch (Exception ex) {
                alert("Unable to save file");
            }
        }
    }

    private void alert(String text) {
        JOptionPane.showMessageDialog(this, text, "Alert", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleWord().setVisible(true));
    }
}
