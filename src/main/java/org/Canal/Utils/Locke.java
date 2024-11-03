package org.Canal.Utils;

import javax.swing.*;
import java.awt.*;

public class Locke {

    private String name, transaction;
    private boolean status;
    private Color color;
    private Locke[] children;

    public Locke(String name, boolean status, String transaction, Color color, Locke[] children) {
        this.name = name;
        this.status = status;
        this.transaction = transaction;
        this.color = (color == null ? UIManager.getColor("Label.foreground") : color);
        this.children = children;
    }

    public Locke(String name, boolean status, String transaction, Locke[] canals) {
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

    public Locke[] getChildren() {
        return children;
    }

    public String getTransaction() {
        return transaction;
    }

    @Override
    public String toString() {
        return name + (Engine.getConfiguration().isShowCanalCodes() ? " – " + transaction : "");
    }
}