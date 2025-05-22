import javax.swing.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Notepad extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private UndoManager undoManager;

    public Notepad() {
        setTitle("Notepad");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);

        fileChooser = new JFileChooser();

        createMenuBar();
        createToolBar();
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();

        toolBar.add(createToolButton("C:/Users/pc/Downloads/new-document.png", "New", e -> newFile()));
        toolBar.add(createToolButton("C:/Users/pc/Downloads/open.png", "Open", e -> openFile()));
        toolBar.add(createToolButton("C:/Users/pc/Downloads/save-instagram.png", "Save", e -> saveFile()));

        toolBar.addSeparator();

        toolBar.add(createToolButton("C:/Users/pc/Downloads/scissors.png", "Cut", e -> textArea.cut()));
        toolBar.add(createToolButton("C:/Users/pc/Downloads/copy.png", "Copy", e -> textArea.copy()));
        toolBar.add(createToolButton("C:/Users/pc/Downloads/paste.png", "Paste", e -> textArea.paste()));

        toolBar.addSeparator();

        toolBar.add(createToolButton("C:/Users/pc/Downloads/undo-circular-arrow.png", "Undo", e -> undo()));
        toolBar.add(createToolButton("C:/Users/pc/Downloads/redo-arrow-symbol.png", "Redo", e -> redo()));

        add(toolBar, BorderLayout.NORTH);
    }

    private JButton createToolButton(String iconPath, String tooltip, ActionListener action) {
        ImageIcon originalIcon = new ImageIcon(iconPath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton button = new JButton(scaledIcon);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(36, 36)); // consistent size
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(action);
        return button;
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(createMenuItem("New", e -> newFile()));
        fileMenu.add(createMenuItem("Open...", e -> openFile()));
        fileMenu.add(createMenuItem("Save", e -> saveFile()));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Exit", e -> System.exit(0)));

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(createMenuItem("Cut", e -> textArea.cut()));
        editMenu.add(createMenuItem("Copy", e -> textArea.copy()));
        editMenu.add(createMenuItem("Paste", e -> textArea.paste()));
        editMenu.addSeparator();
        editMenu.add(createMenuItem("Undo", e -> undo()));
        editMenu.add(createMenuItem("Redo", e -> redo()));

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);
    }

    private JMenuItem createMenuItem(String name, ActionListener action) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(action);
        return item;
    }

    private void newFile() {
        textArea.setText("");
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                showError("Failed to open file.");
            }
        }
    }

    private void saveFile() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                textArea.write(writer);
            } catch (IOException ex) {
                showError("Failed to save file.");
            }
        }
    }

    private void undo() {
        if (undoManager.canUndo()) undoManager.undo();
    }

    private void redo() {
        if (undoManager.canRedo()) undoManager.redo();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Notepad().setVisible(true));
    }
}
