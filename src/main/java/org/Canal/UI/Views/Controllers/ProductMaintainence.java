package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Products.Components.Components;
import org.Canal.UI.Views.Products.Items.Items;
import org.Canal.UI.Views.Products.Materials.Materials;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CNL/PROD_MTN
 */
public class ProductMaintainence extends LockeState {

    public ProductMaintainence(DesktopState desktop) {
        super("Product Maintainence", "/CNL/PROD_MTN", false, true, false, true);
        JButton items = Elements.button("Items");
        JButton materials = Elements.button("Materials");
        JButton components = Elements.button("Components");
        JButton boms = Elements.button("BoMs");
        setLayout(new GridLayout(2, 2));
        add(items);
        add(materials);
        add(components);
        add(boms);
        items.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                desktop.put(new Items(desktop));
            }
        });
        materials.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                desktop.put(new Materials(desktop));
            }
        });
        components.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                desktop.put(new Components(desktop));
            }
        });
        boms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

            }
        });
    }
}
