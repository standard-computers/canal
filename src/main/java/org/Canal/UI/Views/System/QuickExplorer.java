package org.Canal.UI.Views.System;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.ColorUtil;
import org.Canal.UI.Elements.DesktopInterface;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.UI.Views.Controllers.Inbox;
import org.Canal.UI.Views.Controllers.MyProfile;
import org.Canal.UI.Views.ViewLocation;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

/**
 * /
 */
public class QuickExplorer extends JFrame implements DesktopState {

    private String statusText = "OK", openLockes = "0";
    private JDesktopPane desktopPane;
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
        add(statusBar(), BorderLayout.SOUTH);
        add(splitPane, BorderLayout.CENTER);
        setVisible(true);
        installShortcuts();
    }

    private JPanel toolbar() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.90f));
        JPanel employee = new JPanel(new FlowLayout(FlowLayout.LEFT));
        employee.setBackground(ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.90f));
        JLabel myName = new JLabel("PLEASE LOGIN");
        myName.setBackground(ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.90f));
        if(Engine.getAssignedUser() != null){
            Employee e = Engine.getEmployee(Engine.getAssignedUser().getEmployee());
            myName.setText(e.getName());
        }
        myName.setFont(UIManager.getFont("h2.font"));
        myName.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));
        employee.add(myName);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        IconButton activity = new IconButton("", "activity", "Recent Activity");
        IconButton inbox = new IconButton("", "inbox", "Message Inbox");
        IconButton me = new IconButton("", "me", "Your Profile");
        buttons.add(activity);
        buttons.add(inbox);
        buttons.add(me);
        buttons.setBackground(ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.90f));

        inbox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                put(new Inbox(QuickExplorer.this));
            }
        });
        me.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                put(new MyProfile(QuickExplorer.this));
            }
        });

        panel.add(employee, BorderLayout.WEST);
        panel.add(buttons, BorderLayout.EAST);
//        panel.setBackground(ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.90f));
        return panel;
    }

    private JPanel statusBar(){
        JPanel statusPanel = new JPanel(new BorderLayout());
        JLabel statusLabel = Elements.label(statusText);
        JLabel openLockesLabel = Elements.label(openLockes);
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(openLockesLabel, BorderLayout.CENTER);
        return statusPanel;
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