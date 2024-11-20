package org.Canal.UI.Views.Orders;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /ORDS/RTRN
 */
public class ReturnOrder extends JInternalFrame {

    private JTextField poField;
    private JTextField onField;
    private Selectable ats;
    private Selectable availablePutaway;

    public ReturnOrder(DesktopState desktop){
        super("Return Order", false, true, false, true);
        Constants.checkLocke(this, true, true);
        Form f = new Form();
        poField = new JTextField(12);
        onField = new JTextField(12);
        f.addInput(new Label("Purchase Order #", Constants.colors[0]), poField);
        f.addInput(new Label("[or] Order #", Constants.colors[1]), onField);
        ats = Selectables.allLocations();
        f.addInput(new Label("Receiving Location", Constants.colors[2]), ats);
        f.addInput(new Label("Putaway Area", Constants.colors[3]), availablePutaway);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button receive = new Button("Receive Order");
        receive.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String po = poField.getText();
                String on = onField.getText();
                String rlid = ats.getSelectedValue();
                if(po.isEmpty() && on.isEmpty()){
                    JOptionPane.showMessageDialog(null, "PO or Order Number required!", "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    if(po.isEmpty()){
                        PurchaseOrder spo = Engine.orderProcessing.getPurchaseOrder(po);
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