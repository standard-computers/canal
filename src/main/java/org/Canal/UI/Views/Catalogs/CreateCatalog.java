package org.Canal.UI.Views.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * /CATS/NEW
 */
public class CreateCatalog extends LockeState {

    private JTextField catalogIdField;
    private JTextField catalogNameField;
    private JTextField descriptionField;
    private JTextField periodField;
    private DatePicker validFromField;
    private DatePicker validToField;
    private SelectionInput availableCostCenters;
    private SelectionInput availableCustomers;
    private SelectionInput availableVendors;

    public CreateCatalog(DesktopState desktop) {

        super("Build a Catalog", "/CATS/NEW");

        String cid = ((String) Engine.codex("CATS", "prefix")) + (Engine.getCatalogs().size() + 1);
        catalogIdField = new Copiable(cid);
        catalogNameField = Elements.input();
        descriptionField = Elements.input();
        periodField = Elements.input();
        validFromField = new DatePicker();
        validToField = new DatePicker();
        availableCostCenters = new SelectionInput(desktop, "");
        availableCustomers = new SelectionInput(desktop, "");
        availableVendors = new SelectionInput(desktop, "");

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