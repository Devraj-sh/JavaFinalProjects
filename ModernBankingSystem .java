import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

class ModernBankingSystem extends JFrame {

    private final JTextField nameField = new JTextField(15);
    private final JTextField amountField = new JTextField(10);
    private final JTextArea transactionArea = new JTextArea();
    private double balance = 0.0;
    private final ArrayList<String> transactions = new ArrayList<>();

    public ModernBankingSystem() {
        setTitle("Modern Banking System");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🧾 Top Panel – Create Account
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("Account Holder:"));
        topPanel.add(nameField);

        JButton createBtn = new JButton("🧑‍💼 Create Account");
        createBtn.addActionListener(e -> createAccount());
        topPanel.add(createBtn);
        add(topPanel, BorderLayout.NORTH);

        // 💸 Center Panel – Transaction History
        transactionArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        transactionArea.setEditable(false);
        transactionArea.setBorder(BorderFactory.createTitledBorder("Transaction History"));
        add(new JScrollPane(transactionArea), BorderLayout.CENTER);

        // 🔘 Bottom Panel – Actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        bottomPanel.add(new JLabel("Amount:"));
        bottomPanel.add(amountField);

        JButton depositBtn = new JButton("➕ Deposit");
        depositBtn.addActionListener(e -> deposit());

        JButton withdrawBtn = new JButton("➖ Withdraw");
        withdrawBtn.addActionListener(e -> withdraw());

        JButton balanceBtn = new JButton("💰 View Balance");
        balanceBtn.addActionListener(e -> viewBalance());

        JButton clearBtn = new JButton("🧹 Clear");
        clearBtn.addActionListener(e -> clearAll());

        bottomPanel.add(depositBtn);
        bottomPanel.add(withdrawBtn);
        bottomPanel.add(balanceBtn);
        bottomPanel.add(clearBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createAccount() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter account holder name.");
            return;
        }
        balance = 0;
        transactions.clear();
        transactionArea.setText("");
        log("Account created for " + name);
        nameField.setEditable(false);
    }

    private void deposit() {
        if (!isAccountCreated()) return;

        try {
            double amt = Double.parseDouble(amountField.getText());
            if (amt <= 0) throw new NumberFormatException();
            balance += amt;
            log("Deposited: $" + amt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid deposit amount.");
        }
    }

    private void withdraw() {
        if (!isAccountCreated()) return;

        try {
            double amt = Double.parseDouble(amountField.getText());
            if (amt <= 0 || amt > balance) {
                JOptionPane.showMessageDialog(this, "Invalid withdrawal amount.");
                return;
            }
            balance -= amt;
            log("Withdrew: $" + amt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid withdrawal amount.");
        }
    }

    private void viewBalance() {
        if (!isAccountCreated()) return;
        JOptionPane.showMessageDialog(this, "Current Balance: $" + String.format("%.2f", balance));
    }

    private boolean isAccountCreated() {
        if (nameField.getText().trim().isEmpty() || nameField.isEditable()) {
            JOptionPane.showMessageDialog(this, "Create an account first.");
            return false;
        }
        return true;
    }

    private void clearAll() {
        nameField.setText("");
        amountField.setText("");
        transactionArea.setText("");
        nameField.setEditable(true);
        balance = 0;
        transactions.clear();
    }

    private void log(String message) {
        String time = LocalDateTime.now().toString().replace('T', ' ').substring(0, 19);
        String fullMsg = "[" + time + "] " + message;
        transactions.add(fullMsg);
        transactionArea.append(fullMsg + "\n");
        amountField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernBankingSystem().setVisible(true));
    }
}
