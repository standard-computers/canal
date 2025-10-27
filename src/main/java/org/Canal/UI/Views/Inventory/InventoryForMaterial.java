package org.Canal.UI.Views.Inventory;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /STK/PI/MTS
 */
public class InventoryForMaterial extends LockeState {

    public InventoryForMaterial() {
        super("PI for Material", "/STK/PI/MTS", false, true, false, true);
        Form f = new Form();
        f.addInput(Elements.inputLabel("Item ID"), new JTextField(10));
        f.addInput(Elements.inputLabel("Location (* all)"), new JTextField("*"));
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
    }
}