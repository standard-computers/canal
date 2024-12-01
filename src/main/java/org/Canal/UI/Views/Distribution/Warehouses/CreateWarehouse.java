package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.Models.SupplyChainUnits.Warehouse;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /WHS/NEW
 */
public class CreateWarehouse extends JInternalFrame {

    public CreateWarehouse(DesktopState desktop) {
        setTitle("New Warehouse");
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/warehouses.png")));
        ArrayList<Warehouse> ls = Engine.getWarehouses();
        String generatedId = "WH" + (100000 + (ls.size() + 1));
        JTextField whsIdField = new JTextField(generatedId);
        JTextField orgIdField = new JTextField(Engine.getOrganization().getId());
        JTextField warehouseNameField = new JTextField(15);
        JTextField streetField = new JTextField(15);
        JTextField cityField = new JTextField(15);
        JTextField stateField = new JTextField(15);
        JTextField postalCodeField = new JTextField(15);
        Selectable countries = Selectables.countries();
        countries.setSelectedValue("US");
        JTextField taxIdField = new JTextField(15);
        JCheckBox isTaxExempt = new JCheckBox();
        JButton make = Elements.button("Make");
        Form f = new Form();
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), whsIdField);
        f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), orgIdField);
        f.addInput(new Label("Name", Constants.colors[10]), warehouseNameField);
        f.addInput(new Label("Street", Constants.colors[9]), streetField);
        f.addInput(new Label("City", Constants.colors[8]), cityField);
        f.addInput(new Label("State", Constants.colors[7]), stateField);
        f.addInput(new Label("Postal", Constants.colors[6]), postalCodeField);
        f.addInput(new Label("Country", Constants.colors[5]), countries);
        f.addInput(new Label("Tax ID (optional)", UIManager.getColor("Label.foreground")), taxIdField);
        f.addInput(new Label("Tax Exempt?", UIManager.getColor("Label.foreground")), isTaxExempt);

        JPanel sif = new JPanel();
        JTextField areaField = new JTextField(12);
        JTextField areaUOMField = new JTextField("SQ FT", 5);
        sif.add(areaField);
        sif.add(areaUOMField);
        f.addInput(new Label("Area", UIManager.getColor("Label.foreground")), sif);

        add(f, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
        setResizable(false);
        setIconifiable(true);
        setClosable(true);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String genId = whsIdField.getText().trim();
                String orgTieId = orgIdField.getText().trim();
                String name = warehouseNameField.getText().trim();
                String line1 = streetField.getText().trim();
                String city = cityField.getText().trim();
                String state = stateField.getText().trim();
                String postal = postalCodeField.getText().trim();
                String country = countries.getSelectedValue();
                String taxId = taxIdField.getText().trim();
                double area = Double.parseDouble(areaField.getText().trim());
                String areaUOM = areaUOMField.getText().trim();
                Warehouse newWarehouse = new Warehouse();
                newWarehouse.setId(genId);
                newWarehouse.setOrg(orgTieId);
                newWarehouse.setName(name);
                newWarehouse.setLine1(line1);
                newWarehouse.setCity(city);
                newWarehouse.setState(state);
                newWarehouse.setPostal(postal);
                newWarehouse.setCountry(country);
                newWarehouse.setTaxId(taxId);
                newWarehouse.setArea(area);
                newWarehouse.setAreaUOM(areaUOM);
                newWarehouse.setTaxExempt(isTaxExempt.isSelected());
                Pipe.save("/WHS", newWarehouse);
                dispose();
                desktop.put(new WarehouseView(newWarehouse, desktop));
            }
        });
    }
}