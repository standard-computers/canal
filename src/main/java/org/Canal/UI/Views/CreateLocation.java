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
    private Selectable objexSelection;
    private Selectable organizations;
    private Selectable countries;
    private JTextField locationIdField;
    private JTextField locationNameField;
    private JTextField line1Field;
    private JTextField line2Field;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField postalField;
    private JTextField einField;
    private JTextField emailField;
    private JTextField phoneField;
    private JCheckBox taxExempt;

    public CreateLocation(String objexType, DesktopState desktop, RefreshListener refreshListener) {

        super("New Location", objexType + "/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateLocation.class.getResource("/icons/create.png")));
        this.objexType = objexType;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.add("General", general());
        tabs.add("Contact Info", contact());
        tabs.add("Dimensional", dimensional());

        JButton make = Elements.button("Make");

        add(Elements.header("Make a Location", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String locationId = locationIdField.getText().trim();
                String organizationId = organizations.getSelectedValue();
                String locationName = locationNameField.getText().trim();
                String line1 = line1Field.getText().trim();
                String line2 = line2Field.getText().trim();
                String city = cityField.getText().trim();
                String state = stateField.getText().trim();
                String postal = postalField.getText().trim();
                String country = countries.getSelectedValue();
                String ein = einField.getText().trim();
                Location location = new Location();
                location.setType(objexSelection.getSelectedValue());
                location.setId(locationId);
                location.setOrganization(organizationId);
                location.setName(locationName);
                location.setLine1(line1);
                location.setLine2(line2);
                location.setCity(city);
                location.setState(state);
                location.setPostal(postal);
                location.setCountry(country);
                location.setEin(ein);
                location.setTaxExempt(taxExempt.isSelected());
                Pipe.save(objexType, location);
                dispose();
                if(refreshListener != null) {
                    refreshListener.refresh();
                }
                desktop.put(Engine.router(objexType + "/" + locationId, desktop));
            }
        });
    }

    private JPanel general(){

        JPanel general = new JPanel(new GridBagLayout());
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
        line1Field = Elements.input();
        line2Field = Elements.input();
        cityField = Elements.input();
        stateField = Elements.input();
        postalField = Elements.input();
        countries = Selectables.countries();
        einField = Elements.input();
        taxExempt = new JCheckBox();

        Form f1 = new Form();
        Form f2 = new Form();
        f1.addInput(Elements.coloredLabel("Type",  UIManager.getColor("Label.foreground")), objexSelection);
        f1.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), locationIdField);
        if(!objexType.equals("/ORGS")) {
            f1.addInput(Elements.coloredLabel("*Organization", UIManager.getColor("Label.foreground")), organizations);
        }
        f1.addInput(Elements.coloredLabel("Name", Constants.colors[0]), locationNameField);
        f1.addInput(Elements.coloredLabel("Address Line 1", Constants.colors[1]), line1Field);
        f1.addInput(Elements.coloredLabel("Line 2", Constants.colors[2]), line2Field);

        f2.addInput(Elements.coloredLabel("City", Constants.colors[3]), cityField);
        f2.addInput(Elements.coloredLabel("State", Constants.colors[4]), stateField);
        f2.addInput(Elements.coloredLabel("Postal", Constants.colors[5]), postalField);
        f2.addInput(Elements.coloredLabel("Country", Constants.colors[6]), countries);
        f2.addInput(Elements.coloredLabel("EIN (Tax ID)", UIManager.getColor("Label.foreground")), einField);
        f2.addInput(Elements.coloredLabel("Tax Exempt?", UIManager.getColor("Label.foreground")), taxExempt);
        general.add(f1);
        general.add(f2);
        return general;
    }

    private JPanel contact(){
        JPanel contactInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();
        emailField = Elements.input(20);
        phoneField = Elements.input();
        f.addInput(Elements.coloredLabel("Email Address", UIManager.getColor("Label.foreground")), emailField);
        f.addInput(Elements.coloredLabel("Phone Number", UIManager.getColor("Label.foreground")), phoneField);
        contactInfo.add(f);
        return contactInfo;
    }

    private JPanel dimensional(){
        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return dimensional;
    }
}