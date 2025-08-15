package org.Canal.UI.Views.System;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /ORGS/NEW
 * Create a new Organization
 */
public class IniSystem extends JFrame {

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
    private UOMField widthUOM = new UOMField();
    private UOMField lengthUOM = new UOMField();
    private UOMField heightUOM = new UOMField();

    public IniSystem() {

        setTitle("Setup Ratio");
        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.add("General", general());
        tabs.add("Contact Info", contact());
        tabs.add("Dimensional", dimensional());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("Start Organization", SwingConstants.LEFT), BorderLayout.NORTH);
        header.add (header(), BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private JPanel toolbar() {

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        IconButton execute = new IconButton("Create", "execute", "Create Organization");
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(execute);
        execute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String locationId = locationIdField.getText().trim();
                String locationName = locationNameField.getText().trim();
                String line1 = line1Field.getText().trim();
                String line2 = line2Field.getText().trim();
                String city = cityField.getText().trim();
                String state = stateField.getText().trim();
                String postal = postalField.getText().trim();
                String country = countries.getSelectedValue();
                String ein = einField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                Location location = new Location();
                location.setType("");
                location.setId(locationId);
                location.setName(locationName);
                location.setLine1(line1);
                location.setLine2(line2);
                location.setCity(city);
                location.setState(state);
                location.setPostal(postal);
                location.setCountry(country);
                location.setEin(ein);
                location.setTaxExempt(taxExempt.isSelected());
                location.setEmail(email);
                location.setPhone(phone);
                location.setWidth(Double.parseDouble(widthUOM.getValue()));
                location.setWidthUOM(widthUOM.getUOM());
                location.setLength(Double.parseDouble(lengthUOM.getValue()));
                location.setLengthUOM(lengthUOM.getUOM());
                location.setHeight(Double.parseDouble(heightUOM.getValue()));
                location.setHeightUOM(heightUOM.getUOM());
                Pipe.save("/ORGS", location);
                dispose();
                new QuickExplorer();
            }
        });
        return buttons;
    }


    private JPanel header() {

        Form f1 = new Form();
        f1.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), locationIdField);
        return f1;
    }

    private JPanel general(){

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        locationIdField = new JTextField("10001");
        locationNameField = Elements.input(15);
        line1Field = Elements.input();
        line2Field = Elements.input();
        cityField = Elements.input();
        stateField = Elements.input();
        postalField = Elements.input();
        countries = Selectables.countries();
        einField = Elements.input();
        taxExempt = new JCheckBox();

        Form f2 = new Form();
        f2.addInput(Elements.coloredLabel("Name", Constants.colors[0]), locationNameField);
        f2.addInput(Elements.coloredLabel("Address Line 1", Constants.colors[1]), line1Field);
        f2.addInput(Elements.coloredLabel("Line 2", Constants.colors[2]), line2Field);
        f2.addInput(Elements.coloredLabel("City", Constants.colors[3]), cityField);
        f2.addInput(Elements.coloredLabel("State", Constants.colors[4]), stateField);
        f2.addInput(Elements.coloredLabel("Postal", Constants.colors[5]), postalField);
        f2.addInput(Elements.coloredLabel("Country", Constants.colors[6]), countries);
        f2.addInput(Elements.coloredLabel("EIN (Tax ID)", UIManager.getColor("Label.foreground")), einField);
        f2.addInput(Elements.coloredLabel("Tax Exempt?", UIManager.getColor("Label.foreground")), taxExempt);
        general.add(f2);
        return general;
    }

    private JPanel contact(){

        JPanel contactInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();
        emailField = Elements.input(20);
        phoneField = Elements.input();
        f.addInput(Elements.coloredLabel("Email Address", Constants.colors[10]), emailField);
        f.addInput(Elements.coloredLabel("Phone Number", Constants.colors[9]), phoneField);
        contactInfo.add(f);
        return contactInfo;
    }

    private JPanel dimensional(){

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();
        f.addInput(Elements.coloredLabel("Width", Constants.colors[0]), widthUOM);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[1]), lengthUOM);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[2]), heightUOM);
        dimensional.add(f);
        return dimensional;
    }
}