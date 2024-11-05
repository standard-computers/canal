package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.UI.Elements.Input;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CATS/NEW
 */
public class CreateCatalog extends JInternalFrame {

    public CreateCatalog(DesktopState desktop) {
        setTitle("Build a Catalog");
        if(Engine.getItems().isEmpty() && Engine.getMaterials().isEmpty()){
            JOptionPane.showMessageDialog(null, "No materials or items.");
            dispose();
            return;
        }
        Input cn = new Input("Name");
        Input availableCostCenters = new Input("Cost Centers (* or ;)");
        Input availableCustomers = new Input("Customers (* or ;)");
        Input availableVendors = new Input("Vendors (* or ;)");
        JButton bl = new JButton("Start Building");
        JPanel php = new JPanel(new BorderLayout());
        JPanel hp = new JPanel(new GridLayout(4, 1));
        hp.add(cn);
        hp.add(availableCostCenters);
        hp.add(availableCustomers);
        hp.add(availableVendors);
        php.add(hp, BorderLayout.CENTER);
        php.add(bl, BorderLayout.SOUTH);
        add(php);
        setResizable(false);
        bl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Catalog c = new Catalog("[ID]", cn.value());
                Pipe.save("/CATS", c);
                //TODO Catalog View
                dispose();
            }
        });
    }
}