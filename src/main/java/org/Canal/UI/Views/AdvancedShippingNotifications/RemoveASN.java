package org.Canal.UI.Views.AdvancedShippingNotifications;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /TRANS/ASN/DEL
 */
public class RemoveASN extends LockeState {

    public RemoveASN() {
        super("Remove ASN", "/TRANS/ASN/DEL", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
        setLayout(new BorderLayout());
        Form f = new Form();

        f.addInput(new org.Canal.UI.Elements.Label("ASN ID", UIManager.getColor("Label.foreground")), Elements.input(10));
        f.addInput(new org.Canal.UI.Elements.Label("ASN Name", UIManager.getColor("Label.foreground")), Elements.input(10));
        f.addInput(new org.Canal.UI.Elements.Label("ASN Postal", UIManager.getColor("Label.foreground")), Elements.input(10));

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
        add(Elements.header("Confirm ASN Deletion"), BorderLayout.NORTH);
    }
}