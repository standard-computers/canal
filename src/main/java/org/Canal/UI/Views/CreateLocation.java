package org.Canal.UI.Views;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;
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
 * /$/NEW
 * Create a Distribution Center
 */
public class CreateLocation extends LockeState {

    public CreateLocation(String objexType, DesktopState desktop) {
        super("New Location", objexType + "/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateLocation.class.getResource("/icons/create.png")));
        ArrayList<Location> ls = Engine.getLocations("DCSS");
        String generatedId = "DC" + (100000 + (ls.size() + 1));
        Selectable objexSelection = Selectables.locationObjex();
        JTextField dcIdField = new JTextField(generatedId);
        JTextField orgIdField = new JTextField();
        if(!objexType.equals("/ORGS")){
            orgIdField = new JTextField(Engine.getOrganization().getId());
        }
        JTextField dcNameField = Elements.input(15);
        JTextField dcStreetField = Elements.input(15);
        JTextField dcCityField = Elements.input(15);
        JTextField dcStateField = Elements.input(15);
        JTextField dcPostalField = Elements.input(15);
        Selectable countries = Selectables.countries();
        JButton make = Elements.button("Make");
        Form f = new Form();
        f.addInput(new Label("Type", UIManager.getColor("Label.foreground")), objexSelection);
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), dcIdField);
        if(!objexType.equals("/ORGS")) {
            f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), orgIdField);
        }
        f.addInput(new Label("Name", Constants.colors[0]), dcNameField);
        f.addInput(new Label("Street", Constants.colors[1]), dcStreetField);
        f.addInput(new Label("City", Constants.colors[2]), dcCityField);
        f.addInput(new Label("State", Constants.colors[3]), dcStateField);
        f.addInput(new Label("Postal", Constants.colors[4]), dcPostalField);
        f.addInput(new Label("Country", Constants.colors[5]), countries);
        add(Elements.header("Make a Location", SwingConstants.LEFT), BorderLayout.NORTH);
        add(f, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
        JTextField finalOrgIdField = orgIdField;
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String dcId = dcIdField.getText().trim();
                String orgTieId = finalOrgIdField.getText().trim();
                String name = dcNameField.getText().trim();
                String line1 = dcStreetField.getText().trim();
                String city = dcCityField.getText().trim();
                String state = dcStateField.getText().trim();
                String postal = dcPostalField.getText().trim();
                String country = countries.getSelectedValue();
                Location location = new Location();
                location.setId(dcId);
                location.setOrganization(orgTieId);
                location.setName(name);
                location.setLine1(line1);
                location.setCity(city);
                location.setState(state);
                location.setPostal(postal);
                location.setCountry(country);
                Pipe.save(objexType, location);
                dispose();
                desktop.put(new Locations(objexType, desktop));
            }
        });
    }
}