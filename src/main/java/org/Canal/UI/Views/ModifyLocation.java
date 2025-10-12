package org.Canal.UI.Views;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /$/NEW
 * Create a Location by Objex type
 */
public class ModifyLocation extends LockeState {

    //Operating Objects
    private Location location;
    private RefreshListener refreshListener;

    //General Info Tab
    private JTextField locationNameField;
    private JTextField line1Field;
    private JTextField line2Field;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField postalField;
    private Selectable countries;
    private JTextField einField;
    private JCheckBox taxExempt;

    //Contact Tab
    private JTextField emailField;
    private JTextField phoneField;

    //Dimensional Tab
    private UOMField widthUOM = new UOMField();
    private UOMField lengthUOM = new UOMField();
    private UOMField heightUOM = new UOMField();

    //Controls Tab
    private JCheckBox allowsInventory;
    private JCheckBox allowsProduction;
    private JCheckBox allowsSales;
    private JCheckBox allowsPurchasing;
    private Selectable status;

    public ModifyLocation(Location location, RefreshListener refreshListener) {

        super("Modify Location", "/" + location.getType() + "/MOD/" + location.getId());
        setFrameIcon(new ImageIcon(ModifyLocation.class.getResource("/icons/create.png")));
        this.location = location;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.add("General", general());
        tabs.add("Contact", contact());
        tabs.add("Dimensional", dimensional());
        tabs.add("Controls", controls());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("Modify " + location.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(header(), BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Location Information");
        buttons.add(review);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton save = new IconButton("Save", "save", "Save changes");
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                location.setName(locationNameField.getText().trim());
                location.setLine1(line1Field.getText().trim());
                location.setLine2(line2Field.getText().trim());
                location.setCity(cityField.getText().trim());
                location.setState(stateField.getText().trim());
                location.setPostal(postalField.getText().trim());
                location.setCountry(countries.getSelectedValue());
                location.setEin(einField.getText().trim());
                location.setTaxExempt(taxExempt.isSelected());
                location.setEmail(emailField.getText().trim());
                location.setPhone(phoneField.getText().trim());
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
                location.setStatus(LockeStatus.valueOf(status.getSelectedValue()));
                location.save();

                dispose();
                if (refreshListener != null) refreshListener.refresh();
            }
        });
        buttons.add(save);
        buttons.add(Box.createHorizontalStrut(5));

        return buttons;
    }

    private JPanel header() {

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Type", UIManager.getColor("Label.foreground")), new Copiable(location.getType()));
        if (!location.getType().equals("ORGS")) {
            form.addInput(Elements.coloredLabel("Organization", UIManager.getColor("Label.foreground")), new Copiable(location.getOrganization()));
        }
        return form;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        locationNameField = Elements.input(location.getName());
        line1Field = Elements.input(location.getLine1());
        line2Field = Elements.input(location.getLine2());
        cityField = Elements.input(location.getCity());
        stateField = Elements.input(location.getState());
        postalField = Elements.input(location.getPostal());
        countries = Selectables.countries();
        countries.setSelectedValue(countries.getSelectedValue());
        einField = Elements.input(location.getEin());
        taxExempt = new JCheckBox("", location.isTaxExempt());

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Name", Constants.colors[0]), locationNameField);
        form.addInput(Elements.coloredLabel("Address Line 1", Constants.colors[1]), line1Field);
        form.addInput(Elements.coloredLabel("Line 2", Constants.colors[2]), line2Field);
        form.addInput(Elements.coloredLabel("City", Constants.colors[3]), cityField);
        form.addInput(Elements.coloredLabel("State", Constants.colors[4]), stateField);
        form.addInput(Elements.coloredLabel("Postal", Constants.colors[5]), postalField);
        form.addInput(Elements.coloredLabel("Country", Constants.colors[6]), countries);
        form.addInput(Elements.coloredLabel("EIN (Tax ID)", UIManager.getColor("Label.foreground")), einField);
        form.addInput(Elements.coloredLabel("Tax Exempt?", UIManager.getColor("Label.foreground")), taxExempt);
        general.add(form);

        return general;
    }

    private JPanel contact() {

        JPanel contactInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        emailField = Elements.input(location.getEmail());
        phoneField = Elements.input(location.getPhone());

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Email Address", Constants.colors[10]), emailField);
        form.addInput(Elements.coloredLabel("Phone Number", Constants.colors[9]), phoneField);
        contactInfo.add(form);

        return contactInfo;
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));

        widthUOM.setValue(String.valueOf(location.getWidth()));
        widthUOM.setUOM(location.getWidthUOM());
        lengthUOM.setValue(String.valueOf(location.getLength()));
        lengthUOM.setUOM(location.getLengthUOM());
        heightUOM.setValue(String.valueOf(location.getHeight()));
        heightUOM.setUOM(location.getHeightUOM());

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Width", Constants.colors[0]), widthUOM);
        form.addInput(Elements.coloredLabel("Length", Constants.colors[1]), lengthUOM);
        form.addInput(Elements.coloredLabel("Height", Constants.colors[2]), heightUOM);
        dimensional.add(form);

        return dimensional;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        allowsInventory = new JCheckBox("Inventory Movements", location.allowsInventory());
        allowsProduction = new JCheckBox("Allows Production", location.allowsProduction());
        allowsSales = new JCheckBox("Sales Order Processing", location.allowsSales());
        allowsPurchasing = new JCheckBox("Purchase Order Processing", location.allowsPurchasing());
        status = Selectables.statusTypes();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Allow Inventory", Constants.colors[10]), allowsInventory);
        form.addInput(Elements.coloredLabel("Allow Production", Constants.colors[9]), allowsProduction);
        form.addInput(Elements.coloredLabel("Allow Sales", Constants.colors[8]), allowsSales);
        form.addInput(Elements.coloredLabel("Allow Purchasing", Constants.colors[7]), allowsPurchasing);
        form.addInput(Elements.coloredLabel("Location Status", Constants.colors[6]), status);
        controls.add(form);

        return controls;
    }

    private void performReview(boolean purge) {

    }
}