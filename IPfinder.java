import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class IPfinder extends JFrame {

    private JTextField urlField;
    private JButton findButton;
    private JLabel resultLabel;

    public IPfinder() {
        setTitle("Website IP Finder");
        setSize(500, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title Panel
        JLabel titleLabel = new JLabel("Website IP Finder", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel for input
        JPanel inputPanel = new JPanel(new FlowLayout());
        urlField = new JTextField(25);
        urlField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        findButton = new JButton("Find IP");
        findButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        inputPanel.add(new JLabel("Enter website:"));
        inputPanel.add(urlField);
        inputPanel.add(findButton);
        add(inputPanel, BorderLayout.CENTER);

        // Result Panel
        JPanel resultPanel = new JPanel();
        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        resultPanel.add(resultLabel);
        add(resultPanel, BorderLayout.SOUTH);

        // Button Click Listener
        findButton.addActionListener(e -> findIPAddress());

        // Enter key triggers search
        urlField.addActionListener(e -> findIPAddress());

        // Optional styling
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background
    }

    private void findIPAddress() {
        String site = urlField.getText().trim();
        if (site.isEmpty()) {
            resultLabel.setText("Please enter a website.");
            return;
        }

        try {
            InetAddress ip = InetAddress.getByName(site);
            resultLabel.setText("IP Address of " + site + ": " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            resultLabel.setText("Invalid website or network error.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IPfinder().setVisible(true));
    }
}
