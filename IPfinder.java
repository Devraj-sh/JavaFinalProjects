import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class IPfinder extends JFrame {

    private JTextField websiteField;
    private JButton lookupBtn;
    private JLabel resultText;

    public IPfinder() {
        setTitle("Website IP Finder");
        setSize(500, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Website IP Finder", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(heading, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new FlowLayout());
        websiteField = new JTextField(25);
        websiteField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lookupBtn = new JButton("Find IP");
        lookupBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        centerPanel.add(new JLabel("Enter website:"));
        centerPanel.add(websiteField);
        centerPanel.add(lookupBtn);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        resultText = new JLabel(" ");
        resultText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        bottomPanel.add(resultText);
        add(bottomPanel, BorderLayout.SOUTH);

        lookupBtn.addActionListener(e -> findIP());
        websiteField.addActionListener(e -> findIP());

        getContentPane().setBackground(new Color(240, 248, 255));
    }

    private void findIP() {
        String input = websiteField.getText().trim();
        if (input.isEmpty()) {
            resultText.setText("Please enter a website.");
            return;
        }

        try {
            InetAddress address = InetAddress.getByName(input);
            resultText.setText("IP Address of " + input + ": " + address.getHostAddress());
        } catch (UnknownHostException ex) {
            resultText.setText("Invalid website or network issue.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IPfinder().setVisible(true));
    }
}
