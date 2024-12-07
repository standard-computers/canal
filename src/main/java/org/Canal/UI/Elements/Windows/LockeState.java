package org.Canal.UI.Elements.Windows;


import javax.swing.*;

public class LockeState extends JInternalFrame {

    private String lock; //Canal Code
    private boolean maximized = false;
    private boolean duplicated = false; //Can have more than one of these open
    private boolean savable = false; //This Locke state can be saved
    private int[] position = new int[2];

    public LockeState(String title, String locke, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        this.lock = locke;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    public boolean isDuplicated() {
        return duplicated;
    }

    public void setDuplicated(boolean duplicated) {
        this.duplicated = duplicated;
    }

    public boolean isSavable() {
        return savable;
    }

    public void setSavable(boolean savable) {
        this.savable = savable;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }
}