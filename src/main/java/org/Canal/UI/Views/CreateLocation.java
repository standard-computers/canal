package org.Canal.UI.Views;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /$/NEW
 * Create a Location by Objex type
 */
public class CreateLocation extends LockeState {

    public CreateLocation(String objexType, DesktopState desktop, RefreshListener refreshListener) {
        super("New Location", objexType + "/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateLocation.class.getResource("/icons/create.png")));
        ArrayList<Location> ls = Engine.getLocations(objexType.replace("/", ""));
        String generatedId = ((String) Engine.codex.getValue(objexType, "prefix")) + (100000 + (ls.size() + 1));
        Selectable objexSelection = Selectables.locationObjex(objexType);
        JTextField locationIdField = new JTextField(generatedId);
        Selectable organizations = Selectables.organizations();
        JTextField locationNameField = Elements.input(15);
        JTextField streetField = Elements.input(15);
        JTextField cityField = Elements.input(15);
        JTextField stateField = Elements.input(15);
        JTextField postalField = Elements.input(15);
        Selectable countries = Selectables.countries();
        JButton make = Elements.button("Make");
        Form f = new Form();
        f.addInput(new Label("Type",  UIManager.getColor("Label.foreground")), objexSelection);
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), locationIdField);
        if(!objexType.equals("/ORGS")) {
            f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), organizations);
        }
        f.addInput(new Label("Name", Constants.colors[0]), locationNameField);
        f.addInput(new Label("Street", Constants.colors[1]), streetField);
        f.addInput(new Label("City", Constants.colors[2]), cityField);
        f.addInput(new Label("State", Constants.colors[3]), stateField);
        f.addInput(new Label("Postal", Constants.colors[4]), postalField);
        f.addInput(new Label("Country", Constants.colors[5]), countries);
        add(Elements.header("Make a Location", SwingConstants.LEFT), BorderLayout.NORTH);
        add(f, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String locationId = locationIdField.getText().trim();
                String organizationId = organizations.getSelectedValue();
                String locationName = locationNameField.getText().trim();
                String line1 = streetField.getText().trim();
                String city = cityField.getText().trim();
                String state = stateField.getText().trim();
                String postal = postalField.getText().trim();
                String country = countries.getSelectedValue();
                Location location = new Location();
                location.setType(objexSelection.getSelectedValue());
                location.setId(locationId);
                location.setOrganization(organizationId);
                location.setName(locationName);
                location.setLine1(line1);
                location.setCity(city);
                location.setState(state);
                location.setPostal(postal);
                location.setCountry(country);
                Pipe.save(objexType, location);
                dispose();
                if(refreshListener != null) {
                    refreshListener.onRefresh();
                }
                desktop.put(Engine.router(objexType + "/" + locationId, desktop));
            }
        });
    }
}