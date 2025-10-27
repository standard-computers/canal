package org.Canal.UI.Views.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * /CATS/NEW
 */
public class CreateCatalog extends LockeState {

    public CreateCatalog(DesktopState desktop) {

        super("Build a Catalog", "/CATS/NEW");
        setFrameIcon(new ImageIcon(CreateCatalog.class.getResource("/icons/create.png")));

        String cid = ((String) Engine.codex("CATS", "prefix")) + (Engine.getCatalogs().size() + 1);
        JTextField catalogIdField = new Copiable(cid);
        JTextField catalogNameField = Elements.input();
        JTextField descriptionField = Elements.input();
        JTextField periodField = Elements.input();
        DatePicker validFromField = new DatePicker();
        DatePicker validToField = new DatePicker();
        HashMap<String, String> costCenters = new HashMap<>();
        for (Location l : Engine.getLocations("CCS")) {
            costCenters.put(l.getId(), l.getName());
        }
        JTextField availableCostCenters = Elements.selector("", costCenters, this);
        JTextField availableCustomers = Elements.input();
        JTextField availableVendors = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Catalog ID"), catalogIdField);
        form.addInput(Elements.inputLabel("Catalog Name"), catalogNameField);
        form.addInput(Elements.inputLabel("Description"), descriptionField);
        form.addInput(Elements.inputLabel("Period"), periodField);
        form.addInput(Elements.inputLabel("Valid From"), validFromField);
        form.addInput(Elements.inputLabel("Valid To"), validToField);
        form.addInput(Elements.inputLabel("Restrict to Cost Centers"), availableCostCenters);
        form.addInput(Elements.inputLabel("Restrict to Customers"), availableCustomers);
        form.addInput(Elements.inputLabel("Restrict to Vendors"), availableVendors);

        setLayout(new BorderLayout());
        add(Elements.header("Build a Catalog", SwingConstants.LEFT), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        JButton build = Elements.button("Start Building");
        add(build, BorderLayout.SOUTH);
        build.addActionListener(_ -> {

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
        });
    }
}