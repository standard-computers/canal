package org.Canal.UI.Views.Transportation.InboundDeliveryOrders;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;
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
        JTextField purchaseOrderField = Elements.input(20);
        f.addInput(new Label("Enter Purchase Order", Constants.colors[0]), purchaseOrderField);
        setLayout(new BorderLayout(5, 5));
        add(f, BorderLayout.NORTH);
        JButton create = Elements.button("Create");
        add(create, BorderLayout.SOUTH);
        create.addActionListener(e -> {

        });
    }
}