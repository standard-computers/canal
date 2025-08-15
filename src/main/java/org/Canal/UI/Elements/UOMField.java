package org.Canal.UI.Elements;

import javax.swing.*;

public class UOMField extends JPanel {

    JTextField textField;
    Selectable uom;

    public UOMField(String preset, boolean packaging) {
        textField = Elements.input("0.00", 13);
        if(packaging){
            uom = Selectables.packagingUoms();
            uom.setSelectedValue(preset);
        }else{
            uom = Selectables.uoms(preset);
        }
        add(textField);
        add(uom);
    }

    public UOMField(){
        this("IN", false);
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

    public void setUOM(String value){
        uom.setSelectedValue(value);
    }
}