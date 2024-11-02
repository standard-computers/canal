package org.Canal.UI.Elements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Panels {

    public static JPanel header(String text){
        JPanel panel = new JPanel();
        panel.add(Labels.h2(text));
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        panel.setBackground(UIManager.getColor("Panel.background").darker());
        return panel;
    }
}
