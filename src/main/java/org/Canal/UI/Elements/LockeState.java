package org.Canal.UI.Elements;


import javax.swing.*;
import java.util.ArrayList;

public class LockeState extends JInternalFrame {

    private String locke; //Canal Code
    private boolean maximized = false;
    private boolean duplicated = false; //Can have more than one of these open
    private boolean savable = false; //This Locke state can be saved
    private int[] position = new int[2];
    private ArrayList<String[]> queue = new ArrayList<>();

    public LockeState(String title, String locke, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        this.locke = locke;
        setBorder(BorderFactory.createLineBorder(UIManager.getColor("Button.darkShadow"), 1));
    }

    public String getLocke() {
        return locke;
    }

    public void setLocke(String locke) {
        this.locke = locke;
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

    public ArrayList<String[]> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<String[]> queue) {
        this.queue = queue;
    }

    public void addToQueue(String[] message) {
        queue.add(message);
    }
}