import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class MsExcel extends JFrame {
    private JTable table;

    public MsExcel() {
        setTitle("Modern Excel - Java Swing (No external libraries)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Create table model (50 rows, 26 columns)
        DefaultTableModel model = new DefaultTableModel(50, 26);

        // Create table
        table = new JTable(model) {
            // Alternate row coloring for modern look
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE);
                } else {
                    c.setBackground(new Color(184, 207, 229)); // Selected color
                }
                c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return c;
            }
        };

        // Set column headers A-Z
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230));
        header.setForeground(Color.DARK_GRAY);

        for (int i = 0; i < 26; i++) {
            char col = (char) ('A' + i);
            table.getColumnModel().getColumn(i).setHeaderValue(String.valueOf(col));
            table.getColumnModel().getColumn(i).setPreferredWidth(70);
        }

        table.setRowHeight(25);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(table);

        // Toolbar with some basic formatting buttons
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton boldBtn = new JButton("B");
        boldBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boldBtn.setFocusable(false);
        boldBtn.setToolTipText("Toggle Bold");

        JButton italicBtn = new JButton("I");
        italicBtn.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        italicBtn.setFocusable(false);
        italicBtn.setToolTipText("Toggle Italic");

        toolBar.add(boldBtn);
        toolBar.add(italicBtn);

        // Bold action: toggle bold font on selected cells
        boldBtn.addActionListener(e -> toggleFontStyle(Font.BOLD));

        // Italic action: toggle italic font on selected cells
        italicBtn.addActionListener(e -> toggleFontStyle(Font.ITALIC));

        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void toggleFontStyle(int style) {
        int[] selectedRows = table.getSelectedRows();
        int[] selectedCols = table.getSelectedColumns();

        for (int row : selectedRows) {
            for (int col : selectedCols) {
                Object value = table.getValueAt(row, col);
                if (value == null) continue;

                // We will store the font style inside the cell renderer via a client property
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Component comp = table.prepareRenderer(renderer, row, col);
                Font currentFont = comp.getFont();

                // Calculate new font style: toggle given style bit
                int newStyle;
                if ((currentFont.getStyle() & style) != 0) {
                    // Style is on, remove it
                    newStyle = currentFont.getStyle() & ~style;
                } else {
                    // Style is off, add it
                    newStyle = currentFont.getStyle() | style;
                }

                // Update cell font via a custom renderer - Here simplified by setting cell's font directly
                // JTable doesn’t support per-cell font out of the box, so we’ll store info in the table model or use a map
                // For simplicity, we just set the value to a JLabel with styled font

                // Save styled value as JLabel (not perfect, but demonstrates concept)
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI", newStyle, 14));
                label.setOpaque(true);
                label.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE);
                label.setForeground(Color.BLACK);

                // Hack: set JLabel as the cell renderer temporarily for this cell:
                table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object val, boolean isSelected, boolean hasFocus, int rowR, int colR) {
                        JLabel comp = (JLabel) super.getTableCellRendererComponent(table, val, isSelected, hasFocus, rowR, colR);
                        if (rowR == row && colR == col) {
                            comp.setFont(label.getFont());
                        } else {
                            comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        }
                        if (!isSelected) {
                            comp.setBackground(rowR % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE);
                        }
                        return comp;
                    }
                });

                table.repaint();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MsExcel app = new MsExcel();
            app.setVisible(true);
        });
    }
}
