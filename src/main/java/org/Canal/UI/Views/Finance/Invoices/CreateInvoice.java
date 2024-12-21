package org.Canal.UI.Views.Finance.Invoices;

import org.Canal.Models.BusinessUnits.Invoice;
import org.Canal.UI.Elements.Input;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateInvoice extends LockeState {

    private Invoice newInvoice;

    public CreateInvoice(String providedCustomerId) {
        super("Create Invoice", "/INVS/NEW", false, true, false, true);
        if(Engine.getLocations("CSTS").isEmpty()){
            JOptionPane.showMessageDialog(null, "No customers to invoice.");
            dispose();
            return;
        }
        JPanel orderInfo = new JPanel(new GridLayout(2, 1));
        Input customerId = new Input("Customer ID");
        Input vendorId = new Input("Vendor ID");
        if(providedCustomerId != null){
            customerId.setValue(providedCustomerId);
            customerId.disable();
        }
        orderInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton save = new JButton("Save");
        orderInfo.add(customerId);
        orderInfo.add(vendorId);
        setLayout(new BorderLayout());
        add(orderInfo, BorderLayout.NORTH);
        add(save, BorderLayout.SOUTH);
    }
}