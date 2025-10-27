package org.Canal.UI.Elements;

import org.Canal.Models.HumanResources.User;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IconButton extends JButton {

    private User me = Engine.getAssignedUser();

    public IconButton(String text, String icon, String toolTip, String locke) {
        configureButton(text, icon, toolTip);
        addMouseListener(createMouseAdapter(locke));
        if(!me.hasAccess(locke)){
            setVisible(false);
        }
    }

    public IconButton(String text, String icon, String toolTip) {
        configureButton(text, icon, toolTip);
        addMouseListener(createMouseAdapter(null));
    }

    private void configureButton(String text, String icon, String toolTip) {
        ImageIcon originalIcon = new ImageIcon(IconButton.class.getResource("/icons/" + icon + ".png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));

        if(Engine.getConfiguration().showButtonLabels()) {
            setText(text);
        }

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
                if (locke != null) {
                    //Start.q.put(Engine.router(locke, Start.q));
                }
            }
        };
    }

    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += 6;
        size.height += 6;
        return size;
    }
}