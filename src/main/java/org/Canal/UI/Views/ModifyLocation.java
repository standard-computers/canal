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
public class ModifyLocation extends LockeState {

    private String objexType;
    private Location location;
    private DesktopState desktop;
    private RefreshListener refreshListener;
    private Selectable objexSelection;
    private Selectable organizations;
    private Selectable countries;
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
    private JCheckBox allowsInventory;
    private JCheckBox allowsProduction;
    private JCheckBox allowsSales;
    private JCheckBox allowsPurchasing;
    private UOMField widthUOM = new UOMField();
    private UOMField lengthUOM = new UOMField();
    private UOMField heightUOM = new UOMField();

    public ModifyLocation(String objexType, Location location, DesktopState desktop, RefreshListener refreshListener) {

        super("Modify " + location.getName(), objexType + "/MOD/" + location.getId(), false, true, false, true);
        setFrameIcon(new ImageIcon(ModifyLocation.class.getResource("/icons/create.png")));
        this.objexType = objexType;
        this.location = location;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.add("General", general());
        tabs.add("Contact Info", contact());
        tabs.add("Dimensional", dimensional());
        tabs.add("Config", config());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("Make a Location", SwingConstants.LEFT), BorderLayout.NORTH);
        header.add (header(), BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy From Location");
        copyFrom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String prId = JOptionPane.showInputDialog(ModifyLocation.this, "Enter Location ID");
            }
        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(copyFrom);

        IconButton review = new IconButton("Review", "review", "Review Location Information");
        review.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

            }
        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(review);

        IconButton execute = new IconButton("Execute", "execute", "Create Location");
        execute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int ccc = JOptionPane.showConfirmDialog(null, "Confirm Location creation?", "Confirm Execution", JOptionPane.YES_NO_OPTION);
                if(ccc == JOptionPane.YES_OPTION){

                    System.out.println(Engine.codex.getValue(objexType, "prefix"));
                    ArrayList<Location> ls = Engine.getLocations(objexType);
                    String locationId = ((String) Engine.codex.getValue(objexType, "prefix")) + (1000 + (ls.size() + 1));
                    String organizationId = organizations.getSelectedValue();
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
                    location.setEmail(email);
                    location.setPhone(phone);
                    location.setWidth(Double.parseDouble(widthUOM.getValue()));
                    location.setWidthUOM(widthUOM.getUOM());
                    location.setLength(Double.parseDouble(lengthUOM.getValue()));
                    location.setLengthUOM(lengthUOM.getUOM());
                    location.setHeight(Double.parseDouble(heightUOM.getValue()));
                    location.setHeightUOM(heightUOM.getUOM());
                    location.setAllowsInventory(allowsInventory.isSelected());
                    location.setAllowsProduction(allowsProduction.isSelected());
                    location.setAllowsSales(allowsSales.isSelected());
                    location.setAllowsPurchasing(allowsPurchasing.isSelected());
                    location.save();

                    if(refreshListener != null) {
                        refreshListener.refresh();
                    }
                    dispose();

                }
            }
        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(execute);
        return buttons;
    }

    private JPanel header() {

        Form f1 = new Form();
        f1.addInput(Elements.coloredLabel("Type",  UIManager.getColor("Label.foreground")), objexSelection);
        if(!objexType.equals("/ORGS")) {
            f1.addInput(Elements.coloredLabel("*Organization", UIManager.getColor("Label.foreground")), organizations);
        }
        return f1;
    }

    private JPanel general(){

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String oo = objexType;
        if(oo.startsWith("/")){
            oo = oo.substring(1);
        }
        objexSelection = Selectables.locationObjex(objexType);
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

    private JPanel config(){

        JPanel config = new JPanel(new FlowLayout(FlowLayout.LEFT));

        allowsInventory = new JCheckBox("Inventory Movements");
        allowsProduction = new JCheckBox("Allows Production");
        allowsSales = new JCheckBox("Sales Order Processing");
        allowsPurchasing = new JCheckBox("Purchase Order Processing");

        Form f = new Form();
        f.addInput(Elements.coloredLabel("Allow Inventory", Constants.colors[0]), allowsInventory);
        f.addInput(Elements.coloredLabel("Allow Production", Constants.colors[1]), allowsProduction);
        f.addInput(Elements.coloredLabel("Allow Sales", Constants.colors[2]), allowsSales);
        f.addInput(Elements.coloredLabel("Allow Purchasing", Constants.colors[3]), allowsPurchasing);
        config.add(f);

        return config;
    }
}