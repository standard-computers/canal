package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;

public class Form extends JPanel {

    private GridBagConstraints gbc;

    public Form(){
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 0.3;
        gbc.gridy = 0;
    }

    public void addInput(Component label, Component value) {
        gbc.gridx = 0;
        add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        add(value, gbc);
        gbc.gridy++;
    }
}