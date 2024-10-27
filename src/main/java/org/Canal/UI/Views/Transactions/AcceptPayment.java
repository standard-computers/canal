package org.Canal.UI.Views.Transactions;

import org.Canal.UI.Elements.Input;
import javax.swing.*;
import java.awt.*;

public class AcceptPayment extends JInternalFrame {

    public AcceptPayment() {
        setTitle("Process Payment");
        Input custId = new Input("Customer ID");
        Input poNumber = new Input("PO Number");
        Input amount = new Input("Invoice Number");
        Input ref = new Input("Ref Number");
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.add(custId);
        panel.add(poNumber);
        panel.add(amount);
        panel.add(ref);
        JButton process = new JButton("Process");
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(process, BorderLayout.SOUTH);
        setClosable(true);
    }
}