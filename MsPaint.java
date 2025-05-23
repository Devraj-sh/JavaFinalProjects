import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DrawingApp extends JFrame {
    private BufferedImage canvas;
    private Graphics2D drawTool;
    private int lastX, lastY;
    private boolean canDraw = false;

    public DrawingApp() {
        setTitle("Drawing Board");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        canvas = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        drawTool = canvas.createGraphics();
        drawTool.setColor(Color.BLACK);
        drawTool.setStroke(new BasicStroke(3));

        DrawingPanel panel = new DrawingPanel();
        add(panel);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
                canDraw = true;
            }

            public void mouseReleased(MouseEvent e) {
                canDraw = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (canDraw) {
                    int currentX = e.getX();
                    int currentY = e.getY();
                    drawTool.drawLine(lastX, lastY, currentX, currentY);
                    lastX = currentX;
                    lastY = currentY;
                    panel.repaint();
                }
            }
        });
    }

    private class DrawingPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(canvas, 0, 0, null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DrawingApp().setVisible(true));
    }
}
