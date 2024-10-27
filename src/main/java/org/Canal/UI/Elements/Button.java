package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {

    private Color originalColor;

    public Button(String text){
        super(text);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setPreferredSize(new Dimension(190, 35));
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