package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.DesktopInterface;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class QuickExplorer extends JFrame implements DesktopState {

    private JDesktopPane desktopPane;

    public QuickExplorer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktopPane = new DesktopInterface();
        add(desktopPane);
        addFrame(new Controller(this));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        if(Engine.client == null){
            putCenter(new Login());
        }
    }

    private void addFrame(JInternalFrame win) {
        win.setLocation(5, 5);
        win.pack();
        win.setVisible(true);
        desktopPane.add(win);
    }

    public void putCenter(JInternalFrame frame) {
        int desktopWidth = desktopPane.getWidth();
        int desktopHeight = desktopPane.getHeight();
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int x = (desktopWidth - frameWidth) / 2;
        int y = (desktopHeight - frameHeight) / 2;
        frame.setLocation(x, y);
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    @Override
    public void put(JInternalFrame frame) {
        addFrame(frame);
        smartPlace(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        repaint();
    }

    @Override
    public void clean() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (int i = 1; i < frames.length; i++) {
            try {
                frames[i].setIcon(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void purge() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (int i = 1; i < frames.length; i++) {
            try {
                frames[i].setClosed(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void smartPlace(JInternalFrame frame) {
        JDesktopPane desktopPane = frame.getDesktopPane();
        if (desktopPane == null) return;
        int desktopWidth = desktopPane.getWidth();
        int desktopHeight = desktopPane.getHeight();
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        boolean placed = false;
        JInternalFrame[] existingFrames = desktopPane.getAllFrames();
        for (int x = 0; x < desktopWidth - frameWidth; x += 30) {
            for (int y = 0; y < desktopHeight - frameHeight; y += 30) {
                boolean overlap = false;
                for (JInternalFrame existing : existingFrames) {
                    if (existing.getBounds().intersects(new Rectangle(x, y, frameWidth, frameHeight))) {
                        overlap = true;
                        break;
                    }
                }
                if (!overlap) {
                    frame.setLocation(x, y);
                    placed = true;
                    break;
                }
            }
            if (placed) break;
        }
        if (!placed) {
            int x = (desktopWidth - frameWidth) / 2;
            int y = (desktopHeight - frameHeight) / 2;
            frame.setLocation(x, y);
        }
    }
}