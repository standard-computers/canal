package org.Canal.UI.Elements;

import org.Canal.Start;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IconButton extends JButton {


    public IconButton(String text, String icon, String toolTip, String locke) {
        configureButton(text, icon, toolTip);
        addMouseListener(createMouseAdapter(locke));
    }

    public IconButton(String text, String icon, String toolTip) {
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
        setFocusPainted(false);
        setBorderPainted(true);
    }


    private MouseAdapter createMouseAdapter(String locke) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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

    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += 8;
        size.height += 8;
        return size;
    }
}