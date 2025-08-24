package org.Canal.UI.Views.Rates;

import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /RTS
 */
public class Rates extends LockeState {

    public Rates() {
        super("Rates", "/RTS");
        Form f = new Form();

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        JButton ok = new JButton("OK");
        add(buttons, BorderLayout.SOUTH);
    }
}