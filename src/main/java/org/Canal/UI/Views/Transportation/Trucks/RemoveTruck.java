package org.Canal.UI.Views.Transportation.Trucks;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /TRANS/TRCKS/DEL
 */
public class RemoveTruck extends JInternalFrame {

    public RemoveTruck() {
        super("Remove Truck", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setLayout(new BorderLayout());
        Form f = new Form();

        f.addInput(new Label("Truck ID", UIManager.getColor("Label.foreground")), Elements.input(10));
        f.addInput(new Label("Truck TPDID", UIManager.getColor("Label.foreground")), Elements.input(10));

        add(f, BorderLayout.CENTER);
        JPanel options = new JPanel(new GridLayout(1, 2));
        Button confirm = new Button("Confirm");
        confirm.setForeground(Color.RED);
        Button cancel = new Button("Cancel");
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
        add(Elements.header("Confirm Truck Deletion"), BorderLayout.NORTH);
    }
}