package org.Canal.UI.Views.Inventory;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.FormFrame;
import org.Canal.Utils.Engine;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CreateSTO extends FormFrame {

    public CreateSTO(){
        setTitle("Create Stock Transfer Order");
        setTransactionCode("/INV/MV/STO");
        addInput(new Label("Purchase Order #", new Color(255, 102, 255)), new JTextField());
        HashMap<String, String> opts = new HashMap<>();
        for(Location cs : Engine.getCustomers()){
            opts.put(cs.getId() + " – " + cs.getName(), cs.getId());
        }
        for(Location vs : Engine.getVendors()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        Selectable senderOps = new Selectable(opts);
        Selectable destinationOps = new Selectable(opts);
        addInput(new Label("Sender", new Color(204, 153, 255)), senderOps);
        addInput(new Label("Destination", new Color(204, 153, 255)), destinationOps);
        setVisible(true);
        pack();
    }
}