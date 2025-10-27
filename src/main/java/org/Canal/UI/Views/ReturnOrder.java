package org.Canal.UI.Views;

import org.Canal.Models.BusinessUnits.Order;
import org.Canal.UI.Elements.*;
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
public class ReturnOrder extends LockeState {

    private JTextField poField;
    private JTextField onField;
    private Selectable ats;
    private Selectable availablePutaway;

    public ReturnOrder(DesktopState desktop) {
        super("Return Order", "/ORDS/RTRN", false, true, false, true);
        Constants.checkLocke(this, true, true);

        poField = Elements.input(12);
        onField = Elements.input(12);
        ats = Selectables.allLocations();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Purchase Order #"), poField);
        form.addInput(Elements.inputLabel("[or] Order #"), onField);
        form.addInput(Elements.inputLabel("Receiving Location"), ats);
        form.addInput(Elements.inputLabel("Putaway Area"), availablePutaway);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        JButton receive = Elements.button("Receive Order");
        receive.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String po = poField.getText();
                String on = onField.getText();
                String rlid = ats.getSelectedValue();
                if (po.isEmpty() && on.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "PO or Order Number required!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (po.isEmpty()) {
                        Order spo = Engine.getPurchaseOrder(po);
                        if (spo == null) {
                            JOptionPane.showMessageDialog(null, "PO or Order Number required!", "Error", JOptionPane.ERROR_MESSAGE);
                            if (!rlid.equals(spo.getVendor())) {
                                int override = JOptionPane.showConfirmDialog(null, "The receiving location and buyer do not match. Override?");
                                if (override == JOptionPane.NO_OPTION) {

                                } else {

                                }
                            }
                        }
                    }
                }
            }
        });
    }
}