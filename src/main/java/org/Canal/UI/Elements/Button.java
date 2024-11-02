package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {

    private Color originalColor;

    public Button(String text){
        super(text);
        setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(190, 35));
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Reset cursor when not hovering
            }
        });
    }

    public void color(Color color){
        this.originalColor = color;
        this.setForeground(Color.WHITE);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Color currentColor = getBackground();
                setBackground(getDarkerColor(currentColor));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(originalColor);
            }
        });
        setBackground(color);
    }

    private Color getDarkerColor(Color color) {
        return new Color(
                Math.max((int) (color.getRed() * 0.85), 0),
                Math.max((int) (color.getGreen() * 0.85), 0),
                Math.max((int) (color.getBlue() * 0.85), 0)
        );
    }
}