import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ModernBankingSystem extends JFrame {

    private final JTextField holderInput = new JTextField(15);
    private final JTextField moneyInput = new JTextField(10);
    private final JTextArea historyDisplay = new JTextArea();
    private double currentBalance = 0.0;
    private final ArrayList<String> logs = new ArrayList<>();

    public ModernBankingSystem() {
        setTitle("Modern Banking System");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        header.setBackground(new Color(245, 245, 245));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        header.add(new JLabel("Account Holder:"));
        header.add(holderInput);

        JButton createAccountBtn = new JButton("Create Account");
        createAccountBtn.addActionListener(e -> handleAccountCreation());
        header.add(createAccountBtn);
        add(header, BorderLayout.NORTH);

        historyDisplay.setFont(new Font("Consolas", Font.PLAIN, 14));
        historyDisplay.setEditable(false);
        historyDisplay.setBorder(BorderFactory.createTitledBorder("Transaction Log"));
        add(new JScrollPane(historyDisplay), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actions.setBorder(new EmptyBorder(10, 10, 10, 10));
        actions.add(new JLabel("Amount:"));
        actions.add(moneyInput);

        JButton depositBtn = new JButton("Deposit");
        depositBtn.addActionListener(e -> depositFunds());

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.addActionListener(e -> withdrawFunds());

        JButton checkBtn = new JButton("View Balance");
        checkBtn.addActionListener(e -> showBalance());

        JButton resetBtn = new JButton("Clear");
        resetBtn.addActionListener(e -> resetAll());

        actions.add(depositBtn);
        actions.add(withdrawBtn);
        actions.add(checkBtn);
        actions.add(resetBtn);

        add(actions, BorderLayout.SOUTH);
    }

    private void handleAccountCreation() {
        String name = holderInput.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the account holder's name.");
            return;
        }
        currentBalance = 0;
        logs.clear();
        historyDisplay.setText("");
        record("Account created for " + name);
        holderInput.setEditable(false);
    }

    private void depositFunds() {
        if (!accountReady()) return;

        try {
            double amount = Double.parseDouble(moneyInput.getText());
            if (amount <= 0) throw new NumberFormatException();
            currentBalance += amount;
            record("Deposited $" + amount);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount to deposit.");
        }
    }

    private void withdrawFunds() {
        if (!accountReady()) return;

        try {
            double amount = Double.parseDouble(moneyInput.getText());
            if (amount <= 0 || amount > currentBalance) {
                JOptionPane.showMessageDialog(this, "Invalid withdrawal amount.");
                return;
            }
            currentBalance -= amount;
            record("Withdrew $" + amount);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount to withdraw.");
        }
    }

    private void showBalance() {
        if (!accountReady()) return;
        JOptionPane.showMessageDialog(this, "Current Balance: $" + String.format("%.2f", currentBalance));
    }

    private boolean accountReady() {
        if (holderInput.getText().trim().isEmpty() || holderInput.isEditable()) {
            JOptionPane.showMessageDialog(this, "Create an account first.");
            return false;
        }
        return true;
    }

    private void resetAll() {
        holderInput.setText("");
        moneyInput.setText("");
        historyDisplay.setText("");
        holderInput.setEditable(true);
        currentBalance = 0;
        logs.clear();
    }

    private void record(String entry) {
        String timestamp = LocalDateTime.now().toString().replace('T', ' ').substring(0, 19);
        String logEntry = "[" + timestamp + "] " + entry;
        logs.add(logEntry);
        historyDisplay.append(logEntry + "\n");
        moneyInput.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernBankingSystem().setVisible(true));
    }
}
