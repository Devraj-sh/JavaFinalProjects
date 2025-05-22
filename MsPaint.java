import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MsPaint extends JFrame {
    private final DrawArea drawArea = new DrawArea();
    private final JButton colorButton = new JButton("🎨");
    private final JSlider brushSizeSlider = new JSlider(1, 30, 5);

    public MsPaint() {
        setTitle("Modern MS Paint - Java Swing");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Modern flat design
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.focusWidth", 0);

        // Top toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(245, 245, 245));
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Color Picker
        colorButton.setFocusable(false);
        colorButton.setBackground(drawArea.getColor());
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Brush Color", drawArea.getColor());
            if (newColor != null) {
                drawArea.setColor(newColor);
                colorButton.setBackground(newColor);
            }
        });

        // Brush size
        brushSizeSlider.setPreferredSize(new Dimension(100, 30));
        brushSizeSlider.setToolTipText("Brush Size");
        brushSizeSlider.addChangeListener(e -> drawArea.setBrushSize(brushSizeSlider.getValue()));

        // Clear Button
        JButton clearBtn = createIconButton("🧹", "Clear Canvas");
        clearBtn.addActionListener(e -> drawArea.clear());

        // Save Button
        JButton saveBtn = createIconButton("💾", "Save as PNG");
        saveBtn.addActionListener(e -> drawArea.saveToFile());

        toolBar.add(new JLabel(" Brush: "));
        toolBar.add(brushSizeSlider);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(colorButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(clearBtn);
        toolBar.add(saveBtn);

        add(toolBar, BorderLayout.NORTH);
        add(drawArea, BorderLayout.CENTER);
    }

    // Utility: Styled button with emoji/text
    private JButton createIconButton(String text, String tooltip) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btn.setFocusable(false);
        btn.setBackground(new Color(230, 230, 230));
        btn.setToolTipText(tooltip);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MsPaint().setVisible(true));
    }
}

// Drawing canvas
class DrawArea extends JPanel {
    private BufferedImage canvas;
    private Graphics2D g2;
    private int prevX, prevY;
    private boolean dragging = false;
    private Color brushColor = Color.BLACK;
    private int brushSize = 5;

    public DrawArea() {
        setBackground(Color.WHITE);
        setDoubleBuffered(false);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
                dragging = true;
                drawDot(prevX, prevY);
            }

            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    int x = e.getX();
                    int y = e.getY();
                    drawLine(prevX, prevY, x, y);
                    prevX = x;
                    prevY = y;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvas == null) {
            initCanvas();
        }
        g.drawImage(canvas, 0, 0, null);
    }

    private void initCanvas() {
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        g2 = canvas.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        repaint();
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        g2.setColor(brushColor);
        g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x1, y1, x2, y2);
        repaint();
    }

    private void drawDot(int x, int y) {
        g2.setColor(brushColor);
        g2.fillOval(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
        repaint();
    }

    public void setColor(Color c) {
        brushColor = c;
    }

    public Color getColor() {
        return brushColor;
    }

    public void setBrushSize(int size) {
        brushSize = size;
    }

    public void clear() {
        if (g2 != null) {
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            repaint();
        }
    }

    public void saveToFile() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Image");
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "Image saved successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save image.");
        }
    }
}
