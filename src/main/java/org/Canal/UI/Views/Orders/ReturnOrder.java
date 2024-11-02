package org.Canal.UI.Views.Orders;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Selectable;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class ReturnOrder extends JInternalFrame {

    private JTextField poField, onField;
    private Selectable ats, availablePutaway;

    public ReturnOrder(DesktopState desktop){
        Constants.checkLocke(this, true, true);
        setTitle("Return Order");
        Form f = new Form();
        poField = new JTextField(12);
        onField = new JTextField(12);
        f.addInput(new Label("Purchase Order #", Constants.colors[0]), poField);
        f.addInput(new Label("[or] Order #", Constants.colors[1]), onField);
        HashMap<String, String> opts = new HashMap<>();
        for(Location cs : Engine.getCustomers()){
            opts.put(cs.getId() + " – " + cs.getName(), cs.getId());
        }
        for(Location vs : Engine.getVendors()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        for(Location vs : Engine.getDistributionCenters()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        ats = new Selectable(opts);
        f.addInput(new Label("Receiving Location", Constants.colors[2]), ats);
        f.addInput(new Label("Putaway Area", Constants.colors[3]), availablePutaway);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button receive = new Button("Receive Order");
        setClosable(true);
        setIconifiable(true);
        receive.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String po = poField.getText();
                String on = onField.getText();
                String rlid = ats.getSelectedValue();
                if(po.isEmpty() && on.isEmpty()){
                    JOptionPane.showMessageDialog(null, "PO or Order Number required!", "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    if(po.isEmpty()){
                        PurchaseOrder spo = Engine.realtime.getPurchaseOrders(po);
                        if(spo == null){
                            JOptionPane.showMessageDialog(null, "PO or Order Number required!", "Error", JOptionPane.ERROR_MESSAGE);
                            if(!rlid.equals(spo.getVendor())){
                                int override = JOptionPane.showConfirmDialog(null, "The receiving location and buyer do not match. Override?");
                                if(override == JOptionPane.NO_OPTION){

                                }else{

                                }
                            }
                        }
                    }
                }
            }
        });
    }
}