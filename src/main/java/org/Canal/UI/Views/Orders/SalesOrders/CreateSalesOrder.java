package org.Canal.UI.Views.Orders.SalesOrders;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Vendor;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.FormFrame;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * /ORDS/SO/NEW
 */
public class CreateSalesOrder extends FormFrame {

    public CreateSalesOrder(){
        Constants.checkLocke(this, true, true);
        setTransactionCode("/ORDS/SO/NEW");
        setTitle("Create Sales Order");
        addInput(new Label("Sales Order #", new Color(255, 102, 255)), new JTextField());
        HashMap<String, String> opts = new HashMap<>();
        for(Location cs : Engine.getCustomers()){
            opts.put(cs.getId() + " – " + cs.getName(), cs.getId());
        }
        for(Vendor vs : Engine.getVendors()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        Selectable ats = new Selectable(opts);
        addInput(new Label("Customer", new Color(204, 153, 255)), ats);
        pack();
        setVisible(true);
    }
}