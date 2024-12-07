package org.Canal.UI.Views.Transportation.OutboundDeliveryOrders;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /TRANS/ODO/DEL
 */
public class RemoveOutboundDeliveryOrder extends LockeState {

    public RemoveOutboundDeliveryOrder() {
        super("Remove ODO", "/TRANS/ODO/DEL", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setLayout(new BorderLayout());
        Form f = new Form();
        f.addInput(new Label("ODO ID", UIManager.getColor("Label.foreground")), Elements.input(10));
        f.addInput(new Label("ODO TPDID", UIManager.getColor("Label.foreground")), Elements.input(10));
        add(f, BorderLayout.CENTER);
        JPanel options = new JPanel(new GridLayout(1, 2));
        JButton confirm = Elements.button("Confirm");
        confirm.setForeground(Color.RED);
        JButton cancel = Elements.button("Cancel");
        options.add(confirm);
        options.add(cancel);
        add(options, BorderLayout.SOUTH);
        add(Elements.header("Confirm ODO Deletion"), BorderLayout.NORTH);
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                //TODO Log deletion cancel
            }
        });
    }
}