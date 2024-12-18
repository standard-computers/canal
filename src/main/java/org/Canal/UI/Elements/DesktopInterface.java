package org.Canal.UI.Elements;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DesktopInterface extends JDesktopPane {

    private BufferedImage backgroundImage;

    public DesktopInterface() {
        try {
            backgroundImage = ImageIO.read(DesktopInterface.class.getResource("/interfaceBackground.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}