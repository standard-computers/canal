package org.Canal.UI.Views.Invoices;

import org.Canal.Models.BusinessUnits.Order;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import java.awt.*;

/**
 * /INVS/CR
 */
public class CreditInvoice extends LockeState {

    private Order newInvoice;

    public CreditInvoice(Order invoice) {
        super("Credit Invoice", "/", false, true, false, true);
        if(Engine.getLocations("CSTS").isEmpty()){
            JOptionPane.showMessageDialog(null, "No customers to invoice.");
            dispose();
            return;
        }
        JPanel orderInfo = new JPanel(new GridLayout(2, 1));
        JButton save = new JButton("Save");
        setLayout(new BorderLayout());
        add(orderInfo, BorderLayout.NORTH);
        add(save, BorderLayout.SOUTH);
    }
}