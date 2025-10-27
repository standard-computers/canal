package org.Canal.UI.Views.Deliveries;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * /TRANS/IDO/NEW
 */
public class CreateInboundDeliveryOrder extends LockeState {

    public CreateInboundDeliveryOrder() {
        super("Create Inbound Delivery Order", "/TRANS/IDO/NEW", false, true, false, true);
        Form f = new Form();
        JTextField purchaseOrderField = Elements.input(10);
        f.addInput(Elements.inputLabel("Enter Purchase Order"), purchaseOrderField);
        setLayout(new BorderLayout(5, 5));
        add(f, BorderLayout.NORTH);
        JButton create = Elements.button("Create");
        add(create, BorderLayout.SOUTH);
        create.addActionListener(_ -> {

        });
    }
}