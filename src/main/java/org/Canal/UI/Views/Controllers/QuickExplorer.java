package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Windows.DesktopInterface;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;

/**
 * /
 */
public class QuickExplorer extends JFrame implements DesktopState {

    private JDesktopPane desktopPane;

    public QuickExplorer() {
        setTitle("Canal – Enterprise Resource Planner – 2024");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktopPane = new DesktopInterface();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new Controller(this), desktopPane);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.2);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(splitPane);
        setVisible(true);
        if(Engine.assignedUser == null){
            putCenter(new Login(false));
        }
        if(Engine.getUsers().isEmpty()){
            JOptionPane.showMessageDialog(desktopPane, "Please make and assign a user to this Canal");
            put(Engine.router("/USRS/NEW", this));
        }
        installShortcuts();
    }

    private void installShortcuts() {
        KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlW = KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlM = KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlH = KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK);
        desktopPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlShiftTab, "cycleFrames");
        desktopPane.getActionMap().put("cycleFrames", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cycleThroughFrames(desktopPane);
            }
        });
        Action closeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JInternalFrame activeFrame = desktopPane.getSelectedFrame();
                if (activeFrame != null) {
                    activeFrame.dispose();
                }
            }
        };
        Action minimizeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JInternalFrame activeFrame = desktopPane.getSelectedFrame();
                if (activeFrame != null) {
                    try {
                        activeFrame.setIcon(true); // Minimizes the active internal frame
                    } catch (java.beans.PropertyVetoException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        desktopPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlW, "closeActiveFrame");
        desktopPane.getActionMap().put("closeActiveFrame", closeAction);
        desktopPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlM, "minimizeActiveFrame");
        desktopPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlH, "minimizeActiveFrame");
        desktopPane.getActionMap().put("minimizeActiveFrame", minimizeAction);
    }

    private static void cycleThroughFrames(JDesktopPane desktopPane) {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        if (frames.length > 0) {
            JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
            int currentIndex = -1;
            for (int i = 0; i < frames.length; i++) {
                if (frames[i] == selectedFrame) {
                    currentIndex = i;
                    break;
                }
            }
            int nextIndex = (currentIndex - 1 + frames.length) % frames.length;
            try {
                frames[nextIndex].setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addFrame(LockeState win) {
        win.setLocation(5, 5);
        win.pack();
        try {
            if (win.isMaximized()) {
                win.setMaximum(true); // This maximizes the internal frame
            }
        } catch (PropertyVetoException e) {
            e.printStackTrace(); // Handle the exception or log it
        }
        win.setVisible(true);
        desktopPane.add(win);
    }

    public void putCenter(JInternalFrame frame) {
        frame.pack();
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
        addFrame((LockeState) frame);
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