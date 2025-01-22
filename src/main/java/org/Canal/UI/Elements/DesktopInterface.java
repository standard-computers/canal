package org.Canal.UI.Elements;

import org.Canal.Utils.Engine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DesktopInterface extends JDesktopPane {

    private BufferedImage backgroundImage;

    public DesktopInterface() {
        try {
            String backgroundPath = Engine.getConfiguration().getBackground();
            if (backgroundPath == null || backgroundPath.trim().isEmpty()) {
                backgroundImage = ImageIO.read(DesktopInterface.class.getResource("/interfaceBackground.jpg"));
            } else {
                backgroundImage = ImageIO.read(new File(backgroundPath));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load background image.", e);
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