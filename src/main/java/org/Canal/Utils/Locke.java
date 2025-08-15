package org.Canal.Utils;

import javax.swing.*;
import java.awt.*;

public class Locke {

    private String name, transaction;
    private Icon icon;
    private Color color;
    private Locke[] children;

    public Locke(String name, Icon icon, String transaction, Color color, Locke[] children) {
        this.name = name;
        this.icon = icon;
        this.transaction = transaction;
        this.color = (color == null ? UIManager.getColor("Label.foreground") : color);
        this.children = children;
    }

    public Locke(String name, Icon icon, String transaction, Locke[] canals) {
        this.name = name;
        this.icon = icon;
        this.transaction = transaction;
        this.color = UIManager.getColor("Label.foreground");
        this.children = canals;
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }

    public Color getColor() {
        return color;
    }

    public Locke[] getChildren() {
        return children;
    }

    public String getTransaction() {
        return transaction;
    }

    @Override
    public String toString() {
        return name + (Engine.getConfiguration().showCanalCodes() ? " – " + transaction : "");
    }
}