package org.Canal.UI.Elements;

import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;

import javax.swing.*;

public class UOMField extends JPanel {

    JTextField textField;
    Selectable uom;

    public UOMField() {
        textField = Elements.input("0.00", 13);
        uom = Selectables.uoms();
        add(textField);
        add(uom);
    }

    public String getValue(){
        return textField.getText();
    }

    public void setValue(String value){
        textField.setText(value);
    }

    public String getUOM(){
        return uom.getSelectedValue();
    }
}