package org.Canal.UI.Elements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class IconButton extends JButton {

    public IconButton(String text, String icon, String toolTip) {
        ImageIcon originalIcon = new ImageIcon(IconButton.class.getResource("/icons/" + icon + ".png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));
        setText(text);
        setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 14));
        setToolTipText(toolTip);
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

    public IconButton(String text, String icon) {
        ImageIcon originalIcon = new ImageIcon(IconButton.class.getResource("/icons/" + icon + ".png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));
        setText(text);
        setFont(new Font(UIManager.getFont("Label.font").getName(), Font.PLAIN, 14));
        setBorder(new EmptyBorder(15, 15, 15, 15));
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
}