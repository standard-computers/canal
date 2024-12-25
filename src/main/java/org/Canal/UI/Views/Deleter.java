package org.Canal.UI.Views;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

public class Deleter extends LockeState {

    public Deleter(String objex){
        super("Deleter", objex + "/MOD", false, true, false, true);

        setLayout(new BorderLayout());
        JButton confirmDeletion = Elements.button("Confirm Deletion");
        add(confirmDeletion, BorderLayout.SOUTH);
    }
}