package org.Canal.UI.Views.Managers;

import org.Canal.Utils.DesktopState;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class QuickExplorer extends JFrame implements DesktopState {

    private JDesktopPane desktopPane;

    public QuickExplorer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktopPane = new JDesktopPane();
        add(desktopPane);
        addFrame(new Controller(this));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void addFrame(JInternalFrame win) {
        win.setLocation(5, 5);
        win.pack();
        win.setVisible(true);
        desktopPane.add(win);
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