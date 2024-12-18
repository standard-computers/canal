package org.Canal.UI.Views.ValueAddedServices;

import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /VAS
 */
public class ValueAddedServices extends LockeState {

    public ValueAddedServices() {
        super("Value Added Services", "/VAS", false, true, false, true);
        Form f = new Form();

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        JButton ok = new JButton("OK");
        add(buttons, BorderLayout.SOUTH);
    }
}