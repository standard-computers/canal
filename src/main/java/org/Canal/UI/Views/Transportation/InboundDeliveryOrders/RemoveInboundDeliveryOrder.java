package org.Canal.UI.Views.Transportation.InboundDeliveryOrders;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /TRANS/IDO/DEL
 */
public class RemoveInboundDeliveryOrder extends LockeState {

    public RemoveInboundDeliveryOrder() {
        super("Remove IDO", "/TRANS/IDO/DEL", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setLayout(new BorderLayout());
        Form f = new Form();

        f.addInput(new Label("IDO ID", UIManager.getColor("Label.foreground")), Elements.input(10));
        f.addInput(new Label("IDO TPDID", UIManager.getColor("Label.foreground")), Elements.input(10));

        add(f, BorderLayout.CENTER);
        JPanel options = new JPanel(new GridLayout(1, 2));
        JButton confirm = Elements.button("Confirm");
        confirm.setForeground(Color.RED);
        JButton cancel = Elements.button("Cancel");
        options.add(confirm);
        options.add(cancel);
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                //TODO Log deletion cancel
            }
        });
        add(options, BorderLayout.SOUTH);
        add(Elements.header("Confirm IDO Deletion"), BorderLayout.NORTH);
    }
}