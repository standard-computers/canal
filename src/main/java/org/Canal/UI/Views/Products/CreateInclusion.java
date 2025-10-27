package org.Canal.UI.Views.Products;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.UOMField;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Includer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateInclusion extends LockeState {

    public CreateInclusion(Includer includer) {

        super("Include Item", "/$>", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateInclusion.class.getResource("/icons/windows/locke.png")));

        JTextField inclusionId = Elements.input();
        UOMField usage = new UOMField("EA", true);
        usage.setValue("1");

        Form form = new Form();
        form.addInput(Elements.inputLabel("Item ID"), inclusionId);
        form.addInput(Elements.inputLabel("Usage"), usage);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        JButton submit = Elements.button("Include");
        add(submit, BorderLayout.SOUTH);

        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String productID = inclusionId.getText().trim();
                Item component = Engine.getItem(productID);
                if(component == null) {
                    System.out.println("No such product: " + productID);
                }
                double qty = Double.parseDouble(usage.getValue());
                dispose();
                includer.commitInclusion(component, qty, usage.getUOM());
            }
        });
    }
}