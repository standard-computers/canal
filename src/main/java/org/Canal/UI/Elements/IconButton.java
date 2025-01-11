package org.Canal.UI.Elements;

import org.Canal.Start;
import org.Canal.UI.ColorUtil;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IconButton extends JButton {

    private final int radius; // Corner radius for rounded corners
    private final Color borderColor; // Border color

    public IconButton(String text, String icon, String toolTip, String locke) {
        this.radius = 8; // Adjust corner radius
        this.borderColor = ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.85f);
        configureButton(text, icon, toolTip);
        addMouseListener(createMouseAdapter(locke));
    }

    public IconButton(String text, String icon, String toolTip) {
        this.radius = 8; // Adjust corner radius
        this.borderColor = ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.85f);
        configureButton(text, icon, toolTip);
        addMouseListener(createMouseAdapter(null));
    }

    private void configureButton(String text, String icon, String toolTip) {
        ImageIcon originalIcon = new ImageIcon(IconButton.class.getResource("/icons/" + icon + ".png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));
        setText(text);
        setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, Engine.getConfiguration().getFontSize()));
        setToolTipText(toolTip);
        setContentAreaFilled(false); // Disable default background painting
        setFocusPainted(false); // Remove focus painting
        setBorderPainted(true);
        setBackground(UIManager.getColor("Button.background"));
    }

    private MouseAdapter createMouseAdapter(String locke) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBackground(ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.85f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                setBackground(UIManager.getColor("Button.background"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                setContentAreaFilled(false);
                if (locke != null) {
                    Start.q.put(Engine.router(locke, Start.q));
                }
            }
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the background with rounded corners
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Draw the border with rounded corners
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        // Let the superclass handle the text and icon painting
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        // Adjust preferred size to account for padding
        Dimension size = super.getPreferredSize();
        size.width += 8; // Add padding to width
        size.height += 8; // Add padding to height
        return size;
    }
}