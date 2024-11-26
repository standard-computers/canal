package org.Canal.UI.Views.Inventory;

import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;

import javax.swing.*;
import java.awt.*;

/**
 * /STK/PI/MTS
 */
public class InventoryForMaterial extends JInternalFrame {

    public InventoryForMaterial() {
        super("PI for Material", false, true, false, true);
        Form f = new Form();
        f.addInput(new Label("Item ID", new Color(65, 180, 45)), new JTextField(10));
        f.addInput(new Label("Location (* all)", new Color(65, 180, 45)), new JTextField("*"));
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
    }
}