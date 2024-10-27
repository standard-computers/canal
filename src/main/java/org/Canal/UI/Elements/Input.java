package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;

public class Input extends JPanel {

    private final JTextField textField;

    public Input(String labelText) {
        textField = new JTextField(16);
        JLabel label = new JLabel(labelText);
        this.add(label);
        this.add(textField);
        label.setPreferredSize(new Dimension(200,20));
        this.setPreferredSize(new Dimension(200, 60));
    }

    public void setValue(String text) {
        textField.setText(text);
    }

    public String value() {
        return textField.getText();
    }

    @Override
    public void disable(){
        textField.setEditable(false);
    }
}