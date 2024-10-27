package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;

public class IconButton extends JButton {

    public IconButton(String text, String icon, String toolTip) {
        ImageIcon originalIcon = new ImageIcon(IconButton.class.getResource("/icons/" + icon + ".png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));
        setText(text);
        setToolTipText(toolTip);
    }
}