package org.Canal.UI.Views.Finance;

import org.Canal.UI.Elements.Input;

import javax.swing.*;
import java.awt.*;

/**
 * /FIN/PYMNTS/NEW
 */
public class IssuePayment extends JInternalFrame {

    public IssuePayment() {
        setTitle("Issue Payment");
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