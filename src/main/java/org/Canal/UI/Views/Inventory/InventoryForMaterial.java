package org.Canal.UI.Views.Inventory;

import org.Canal.UI.Elements.Windows.FormFrame;
import org.Canal.UI.Elements.Label;
import javax.swing.*;
import java.awt.*;

public class InventoryForMaterial extends FormFrame {

    public InventoryForMaterial() {
        setTitle("PI for Material");
        setTransactionCode("/INV/PI/MTS");
        addInput(new Label("Item ID", new Color(65, 180, 45)), new JTextField(10));
        addInput(new Label("Location (* all)", new Color(65, 180, 45)), new JTextField("*"));
        setVisible(true);
        pack();
    }
}