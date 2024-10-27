package org.Canal.Utils;

import javax.swing.*;
import java.awt.*;

public class Canal {

    private String name, transaction;
    private boolean status;
    private Color color;
    private Canal[] children;

    public Canal(String name, boolean status, String transaction, Color color, Canal[] children) {
        this.name = name;
        this.status = status;
        this.transaction = transaction;
        this.color = (color == null ? UIManager.getColor("Label.foreground") : color);
        this.children = children;
    }

    public Canal(String name, boolean status, String transaction, Canal[] canals) {
        this.name = name;
        this.status = status;
        this.transaction = transaction;
        this.color = UIManager.getColor("Label.foreground");
        this.children = canals;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() {
        return status;
    }

    public Color getColor() {
        return color;
    }

    public Canal[] getChildren() {
        return children;
    }

    public String getTransaction() {
        return transaction;
    }

    @Override
    public String toString() {
        return name;
    }
}