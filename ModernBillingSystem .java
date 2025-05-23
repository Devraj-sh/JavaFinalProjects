import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ModernBillingSystem extends JFrame {

    private final DefaultTableModel cartModel;
    private final JTextField itemInput = new JTextField(10);
    private final JTextField priceInput = new JTextField(5);
    private final JTextField quantityInput = new JTextField(3);
    private final JTextArea billArea = new JTextArea();

    public ModernBillingSystem() {
        setTitle("Modern Billing System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topBar.setBackground(new Color(245, 245, 245));
        topBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        topBar.add(new JLabel("Item:"));
        topBar.add(itemInput);
        topBar.add(new JLabel("Price:"));
        topBar.add(priceInput);
        topBar.add(new JLabel("Qty:"));
        topBar.add(quantityInput);

        JButton addItemBtn = new JButton("Add Item");
        addItemBtn.addActionListener(e -> handleAddItem());
        topBar.add(addItemBtn);

        add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.setBorder(new EmptyBorder(10, 10, 10, 10));

        cartModel = new DefaultTableModel(new Object[]{"Item", "Price", "Qty", "Total"}, 0);
        JTable cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cartTable.setRowHeight(25);
        center.add(new JScrollPane(cartTable));

        billArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        billArea.setEditable(false);
        billArea.setBorder(BorderFactory.createTitledBorder("Receipt"));
        center.add(new JScrollPane(billArea));

        add(center, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton receiptBtn = new JButton("Generate Receipt");
        receiptBtn.addActionListener(e -> handleReceipt());

        JButton resetBtn = new JButton("Clear");
        resetBtn.addActionListener(e -> resetForm());

        JButton closeBtn = new JButton("Exit");
        closeBtn.addActionListener(e -> System.exit(0));

        footer.add(receiptBtn);
        footer.add(resetBtn);
        footer.add(closeBtn);

        add(footer, BorderLayout.SOUTH);
    }

    private void handleAddItem() {
        String name = itemInput.getText().trim();
        String priceText = priceInput.getText().trim();
        String qtyText = quantityInput.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill in all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int qty = Integer.parseInt(qtyText);
            double total = price * qty;

            cartModel.addRow(new Object[]{name, price, qty, String.format("%.2f", total)});

            itemInput.setText("");
            priceInput.setText("");
            quantityInput.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
        }
    }

    private void handleReceipt() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items in the cart.");
            return;
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("========= Billing Receipt =========\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        receipt.append("===================================\n");
        receipt.append(String.format("%-15s %-8s %-6s %-8s\n", "Item", "Price", "Qty", "Total"));
        receipt.append("-----------------------------------\n");

        double sum = 0;

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String item = cartModel.getValueAt(i, 0).toString();
            double price = Double.parseDouble(cartModel.getValueAt(i, 1).toString());
            int qty = Integer.parseInt(cartModel.getValueAt(i, 2).toString());
            double total = Double.parseDouble(cartModel.getValueAt(i, 3).toString());

            sum += total;

            receipt.append(String.format("%-15s %-8.2f %-6d %-8.2f\n", item, price, qty, total));
        }

        receipt.append("-----------------------------------\n");
        receipt.append(String.format("%-30s %-8.2f\n", "Grand Total:", sum));
        receipt.append("===================================\n");
        receipt.append("Thanks for your purchase!\n");

        billArea.setText(receipt.toString());
    }

    private void resetForm() {
        cartModel.setRowCount(0);
        billArea.setText("");
        itemInput.setText("");
        priceInput.setText("");
        quantityInput.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernBillingSystem().setVisible(true));
    }
}
