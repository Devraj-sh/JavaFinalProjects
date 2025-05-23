import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextEditor extends JFrame {

    private JTextArea textArea;
    private JFileChooser fileDialog;

    public TextEditor() {
        setTitle("Basic Notepad");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        fileDialog = new JFileChooser();

        setupMenu();
    }

    private void setupMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newDoc = new JMenuItem("New");
        JMenuItem openDoc = new JMenuItem("Open");
        JMenuItem saveDoc = new JMenuItem("Save");
        JMenuItem quit = new JMenuItem("Exit");

        newDoc.addActionListener(e -> textArea.setText(""));
        openDoc.addActionListener(e -> loadText());
        saveDoc.addActionListener(e -> storeText());
        quit.addActionListener(e -> System.exit(0));

        fileMenu.add(newDoc);
        fileMenu.add(openDoc);
        fileMenu.add(saveDoc);
        fileMenu.addSeparator();
        fileMenu.add(quit);

        bar.add(fileMenu);
        setJMenuBar(bar);
    }

    private void loadText() {
        if (fileDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileDialog.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
            } catch (IOException e) {
                showMessage("Error opening file");
            }
        }
    }

    private void storeText() {
        if (fileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileDialog.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                textArea.write(writer);
            } catch (IOException e) {
                showMessage("Error saving file");
            }
        }
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextEditor().setVisible(true));
    }
}
