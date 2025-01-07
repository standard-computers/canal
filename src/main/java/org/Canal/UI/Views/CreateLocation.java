package org.Canal.UI.Views;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
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

    private String objexType;
    private Selectable objexSelection, organizations, countries;
    private JTextField locationIdField, locationNameField, streetField , cityField, stateField, postalField;

    public CreateLocation(String objexType, DesktopState desktop, RefreshListener refreshListener) {

        super("New Location", objexType + "/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateLocation.class.getResource("/icons/create.png")));
        this.objexType = objexType;

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("General Information", generalInformation());
        tabs.add("Contact Info", contactInformation());

        JButton make = Elements.button("Make");

        add(Elements.header("Make a Location", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
        String finalObjexType = objexType;
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
                Pipe.save(finalObjexType, location);
                dispose();
                if(refreshListener != null) {
                    refreshListener.onRefresh();
                }
                desktop.put(Engine.router(finalObjexType + "/" + locationId, desktop));
            }
        });
    }

    private JPanel generalInformation(){
        String oo = objexType;
        if(oo.startsWith("/")){
            oo = oo.substring(1);
        }
        ArrayList<Location> ls = Engine.getLocations(oo);
        String generatedId = ((String) Engine.codex.getValue(oo, "prefix")) + (100000 + (ls.size() + 1));
        objexSelection = Selectables.locationObjex(objexType);
        locationIdField = new JTextField(generatedId);
        organizations = Selectables.organizations();
        locationNameField = Elements.input(15);
        streetField = Elements.input(15);
        cityField = Elements.input(15);
        stateField = Elements.input(15);
        postalField = Elements.input(15);
        countries = Selectables.countries();
        Form f = new Form();
        f.addInput(Elements.coloredLabel("Type",  UIManager.getColor("Label.foreground")), objexSelection);
        f.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), locationIdField);
        if(!objexType.equals("/ORGS")) {
            f.addInput(Elements.coloredLabel("*Organization", UIManager.getColor("Label.foreground")), organizations);
        }
        f.addInput(Elements.coloredLabel("Name", Constants.colors[0]), locationNameField);
        f.addInput(Elements.coloredLabel("Street", Constants.colors[1]), streetField);
        f.addInput(Elements.coloredLabel("City", Constants.colors[2]), cityField);
        f.addInput(Elements.coloredLabel("State", Constants.colors[3]), stateField);
        f.addInput(Elements.coloredLabel("Postal", Constants.colors[4]), postalField);
        f.addInput(Elements.coloredLabel("Country", Constants.colors[5]), countries);
        return f;
    }

    private JPanel contactInformation(){
        Form f = new Form();

        return f;
    }
}