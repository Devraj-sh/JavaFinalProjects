import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ModernBillingSystem extends JFrame {
    private final DefaultTableModel tableModel;
    private final JTextField nameField = new JTextField(10);
    private final JTextField priceField = new JTextField(5);
    private final JTextField quantityField = new JTextField(3);
    private final JTextArea receiptArea = new JTextArea();

    public ModernBillingSystem() {
        setTitle("Modern Billing System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel: Form Inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(new JLabel("Item:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Qty:"));
        inputPanel.add(quantityField);

        JButton addBtn = new JButton("➕ Add Item");
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> addItem());
        inputPanel.add(addBtn);

        add(inputPanel, BorderLayout.NORTH);

        // Center Panel: Table + Receipt
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Table: Cart Items
        tableModel = new DefaultTableModel(new Object[]{"Item", "Price", "Qty", "Total"}, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane tableScroll = new JScrollPane(table);
        centerPanel.add(tableScroll);

        // Receipt
        receiptArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        receiptArea.setEditable(false);
        receiptArea.setBorder(BorderFactory.createTitledBorder("Receipt"));
        centerPanel.add(new JScrollPane(receiptArea));

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel: Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton generateReceiptBtn = new JButton("🧾 Generate Receipt");
        generateReceiptBtn.addActionListener(e -> generateReceipt());

        JButton clearBtn = new JButton("🧹 Clear");
        clearBtn.addActionListener(e -> clearAll());

        JButton exitBtn = new JButton("❌ Exit");
        exitBtn.addActionListener(e -> System.exit(0));

        bottomPanel.add(generateReceiptBtn);
        bottomPanel.add(clearBtn);
        bottomPanel.add(exitBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addItem() {
        String name = nameField.getText();
        String priceText = priceField.getText();
        String qtyText = quantityField.getText();

        if (name.isEmpty() || priceText.isEmpty() || qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all item details.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int qty = Integer.parseInt(qtyText);
            double total = price * qty;

            tableModel.addRow(new Object[]{name, price, qty, String.format("%.2f", total)});

            nameField.setText("");
            priceField.setText("");
            quantityField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.");
        }
    }

    private void generateReceipt() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items to generate receipt.");
            return;
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("======= Java Billing Receipt =======\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        receipt.append("====================================\n");
        receipt.append(String.format("%-15s %-8s %-6s %-8s\n", "Item", "Price", "Qty", "Total"));
        receipt.append("------------------------------------\n");

        double grandTotal = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String item = tableModel.getValueAt(i, 0).toString();
            double price = Double.parseDouble(tableModel.getValueAt(i, 1).toString());
            int qty = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
            double total = Double.parseDouble(tableModel.getValueAt(i, 3).toString());

            grandTotal += total;

            receipt.append(String.format("%-15s %-8.2f %-6d %-8.2f\n", item, price, qty, total));
        }

        receipt.append("------------------------------------\n");
        receipt.append(String.format("%-30s %-8.2f\n", "Grand Total:", grandTotal));
        receipt.append("====================================\n");
        receipt.append("Thank you for shopping with us!\n");

        receiptArea.setText(receipt.toString());
    }

    private void clearAll() {
        tableModel.setRowCount(0);
        receiptArea.setText("");
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernBillingSystem().setVisible(true));
    }
}
