package org.Canal.UI.Views.Inventory;

import org.Canal.UI.Elements.Windows.FormFrame;
import org.Canal.UI.Elements.Label;
import javax.swing.*;
import java.awt.*;

public class InventoryForItem extends FormFrame {

    public InventoryForItem() {
        setTitle("PI for Item");
        setTransactionCode("/INV/PI/ITS");
        addInput(new Label("Item ID", new Color(65, 180, 45)), new JTextField(10));
        addInput(new Label("Location ID (* all)", new Color(65, 180, 45)), new JTextField("*"));
        setVisible(true);
        pack();
    }
}