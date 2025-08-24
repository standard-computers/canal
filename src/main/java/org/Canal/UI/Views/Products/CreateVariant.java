package org.Canal.UI.Views.Products;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Elements.UOMField;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Includer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateVariant extends LockeState {

    public CreateVariant(Includer includer) {

        super("Create Item Variant", "/", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateVariant.class.getResource("/icons/locke.png")));

        Form f = new Form();
        JTextField inclusionId = Elements.input();
        UOMField usage = new UOMField();
        f.addInput(Elements.coloredLabel("Material/Component ID", Constants.colors[9]), inclusionId);
        f.addInput(Elements.coloredLabel("Usage", Constants.colors[8]), usage);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
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