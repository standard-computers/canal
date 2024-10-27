package org.Canal.UI.Views.New;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Views.Singleton.Controller;
import org.Canal.UI.Views.Singleton.WarehouseView;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CreateWarehouse extends JInternalFrame {

    public CreateWarehouse(DesktopState desktop) {
        setTitle("New Warehouse");
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        ArrayList<Location> ls = Engine.getWarehouses();
        String generatedId = "WH" + (100000 + (ls.size() + 1));
        JTextField whsIdField = new JTextField(generatedId);
        JTextField orgIdField = new JTextField(Engine.getOrganization().getId());
        JTextField warehouseNameField = new JTextField(20);
        JTextField streetField = new JTextField(20);
        JTextField cityField = new JTextField(20);
        JTextField stateField = new JTextField(20);
        JTextField postalCodeField = new JTextField(20);
        JTextField countryField = new JTextField(20);
        countryField.setText("US");
        Button make = new Button("Make");
        getRootPane().setDefaultButton(make);
        Form f = new Form();
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), whsIdField);
        f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), orgIdField);
        f.addInput(new Label("Name", Constants.colors[0]), warehouseNameField);
        f.addInput(new Label("Street", Constants.colors[1]), streetField);
        f.addInput(new Label("City", Constants.colors[2]), cityField);
        f.addInput(new Label("State", Constants.colors[3]), stateField);
        f.addInput(new Label("Postal", Constants.colors[4]), postalCodeField);
        f.addInput(new Label("Country", Constants.colors[5]), countryField);
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
                String country = countryField.getText();
                Location location = new Location(genId, orgTieId, name, line1, city, state, postal, country, false);
                Pipe.save("/WHS", location);
                dispose();
                desktop.put(new WarehouseView(location, desktop));
            }
        });
    }
}