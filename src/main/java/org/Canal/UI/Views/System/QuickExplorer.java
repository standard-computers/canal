package org.Canal.UI.Views.System;

import org.Canal.UI.ColorUtil;
import org.Canal.UI.Elements.DesktopInterface;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.UI.Views.ViewLocation;
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

    private String statusText = "OK", openLockes = "0";
    private JDesktopPane desktopPane;
    private JTextField commander;
    private Controller controller;

    public QuickExplorer() {

        setTitle("Canal – 2025");
        setIconImage(new ImageIcon(ViewLocation.class.getResource("/icons/canal.png")).getImage());
        desktopPane = new DesktopInterface();
        controller = new Controller(this);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, controller, desktopPane);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.2);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        setVisible(true);
        installShortcuts();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JPanel toolbar() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.90f));

        commander = Elements.input();
        commander.addActionListener(_ -> {
            put(Engine.router(commander.getText(), this));
        });
        InputMap inputMap = commander.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = commander.getActionMap();
        KeyStroke keyStroke = KeyStroke.getKeyStroke("ctrl SLASH");
        inputMap.put(keyStroke, "focusTextField");
        actionMap.put("focusTextField", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commander.requestFocusInWindow();
                commander.selectAll();
            }
        });
        panel.add(commander, BorderLayout.CENTER);
        return panel;
    }

    private void installShortcuts() {
        KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlW = KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlM = KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlU = KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK);
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
        desktopPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlU, "maximizeActiveFrame");
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
        controller.setBar(win.getLocke());
        win.setLocation(5, 5);
        win.pack();
        try {
            if (win.isMaximized()) {
                win.setMaximum(true);
            }
        } catch (PropertyVetoException e) {
            e.printStackTrace();
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
    public void force(){
        super.revalidate();
        super.repaint();
        revalidate();
        repaint();
    }

    @Override
    public void setCommander(String text) {
        commander.setText(text);
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