package org.Canal.UI.Views.ValueAddedServices;

import org.Canal.UI.Elements.Windows.Form;

import javax.swing.*;
import java.awt.*;

/**
 * /VAS
 */
public class ValueAddedServices extends JInternalFrame {

    public ValueAddedServices() {
        super("Value Added Services", false, true, false, true);
        Form f = new Form();

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        JButton ok = new JButton("OK");
        add(buttons, BorderLayout.SOUTH);
    }
}