package org.Canal.UI.Elements;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Label extends JLabel {

    private Color color;

    public Label(String text, Color color){
        super(text);
        this.color = color;
        setFont(new Font(UIManager.getFont("Label.font").getName(), Font.BOLD, 12));
        double luminance = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
        if (luminance < 128) {
            setForeground(Color.WHITE);
        }
        setMinimumSize(new Dimension(120, 25));
        setMaximumSize(new Dimension(200, 25));
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                new RoundedCornerBorder(8, color),
                BorderFactory.createEmptyBorder(0, 1, 0, 1)
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        int width = getWidth();
        int height = getHeight();
        int diameter = 8 * 2;
        g2.fillRoundRect(0, 0, width - 8, height, diameter, diameter);
        g2.fillRect(8, 0, width - 8, height);
        g2.dispose();
        super.paintComponent(g);
    }

    private static class RoundedCornerBorder implements Border {
        private int cornerRadius;
        private Color color;

        public RoundedCornerBorder(int cornerRadius, Color color) {
            this.cornerRadius = cornerRadius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, cornerRadius * 2, cornerRadius * 2);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(cornerRadius, cornerRadius, cornerRadius, cornerRadius);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}