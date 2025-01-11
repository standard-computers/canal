package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * /CATS/NEW
 */
public class CreateCatalog extends LockeState {

    public CreateCatalog(DesktopState desktop) {

        super("Build a Catalog", "/CATS/NEW", true, true, true, true);
        setFrameIcon(new ImageIcon(CreateCatalog.class.getResource("/icons/create.png")));

        if(Engine.products.getItems().isEmpty() && Engine.products.getMaterials().isEmpty()){
            JOptionPane.showMessageDialog(null, "No materials or items.");
            dispose();
            return;
        }

        Form f = new Form();
        String cid = ((String) Engine.codex("CATS", "preix")) + (Engine.getCatalogs().size() + 1);
        JTextField catalogIdField = new Copiable(cid);
        JTextField catalogNameField = Elements.input();
        JTextField descriptionField = Elements.input();
        JTextField periodField = Elements.input();
        DatePicker validFromField = new DatePicker();
        DatePicker validToField = new DatePicker();
        HashMap<String, String> costCenters = new HashMap<>();
        for(Location l : Engine.getLocations("CCS")){
            costCenters.put(l.getId(), l.getName());
        }
        JTextField availableCostCenters = Elements.selector("", costCenters, this);
        JTextField availableCustomers = Elements.input();
        JTextField availableVendors = Elements.input();
        f.addInput(Elements.coloredLabel("*New Catalog ID", UIManager.getColor("Label.foreground")), catalogIdField);
        f.addInput(Elements.coloredLabel("Catalog Name", Constants.colors[10]), catalogNameField);
        f.addInput(Elements.coloredLabel("Description", Constants.colors[9]), descriptionField);
        f.addInput(Elements.coloredLabel("Period", Constants.colors[8]), periodField);
        f.addInput(Elements.coloredLabel("Valid From", Constants.colors[7]), validFromField);
        f.addInput(Elements.coloredLabel("Valid To", Constants.colors[6]), validToField);
        f.addInput(Elements.coloredLabel("Restrict to Cost Centers", Constants.colors[5]), availableCostCenters);
        f.addInput(Elements.coloredLabel("Restrict to Customers", Constants.colors[4]), availableCustomers);
        f.addInput(Elements.coloredLabel("Restrict to Vendors", Constants.colors[3]), availableVendors);
        setLayout(new BorderLayout());
        add(Elements.header("Build a Catalog", SwingConstants.LEFT), BorderLayout.NORTH);
        add(f, BorderLayout.CENTER);
        JButton build = Elements.button("Start Building");
        add(build, BorderLayout.SOUTH);
        build.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Catalog c = new Catalog();
                c.setId(catalogIdField.getText());
                c.setName(catalogNameField.getText());
                c.setCostCenters(new ArrayList<>(Arrays.asList(availableCostCenters.getText().split(";"))));
                c.setCustomers(new ArrayList<>(Arrays.asList(availableCustomers.getText().split(";"))));
                c.setVendors(new ArrayList<>(Arrays.asList(availableVendors.getText().split(";"))));

                Pipe.save("/CATS", c);
                dispose();
                JOptionPane.showMessageDialog(null, "Catalog Created");
                desktop.put(new ViewCatalog(c));
            }
        });
    }
}