package org.Canal.UI.Views.Transactions.Invoices;

import org.Canal.Models.BusinessUnits.Invoice;
import org.Canal.UI.Elements.Input;
import org.Canal.Utils.Engine;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * /INVS/CR
 */
public class CreditInvoice extends JInternalFrame {

    private Invoice newInvoice;

    public CreditInvoice(Invoice invoice) {
        setTitle("Credit Invoice");
        if(Engine.getCustomers().size() == 0){
            JOptionPane.showMessageDialog(null, "No customers to invoice.");
            dispose();
            return;
        }
        JPanel orderInfo = new JPanel(new GridLayout(2, 1));
        Input customerId = new Input("Customer ID");
        Input vendorId = new Input("Vendor ID");
        orderInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton save = new JButton("Save");
        orderInfo.add(customerId);
        orderInfo.add(vendorId);
        setLayout(new BorderLayout());
        add(orderInfo, BorderLayout.NORTH);
        add(save, BorderLayout.SOUTH);
        setResizable(false);
    }
}