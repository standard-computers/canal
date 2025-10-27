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
        form.addInput(Elements.inputLabel("Type"), new Copiable(location.getType()));
        if (!location.getType().equals("ORGS")) {
            form.addInput(Elements.inputLabel("Organization"), new Copiable(location.getOrganization()));
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
        form.addInput(Elements.inputLabel("Name"), locationNameField);
        form.addInput(Elements.inputLabel("Address Line 1"), line1Field);
        form.addInput(Elements.inputLabel("Line 2"), line2Field);
        form.addInput(Elements.inputLabel("City"), cityField);
        form.addInput(Elements.inputLabel("State"), stateField);
        form.addInput(Elements.inputLabel("Postal"), postalField);
        form.addInput(Elements.inputLabel("Country"), countries);
        form.addInput(Elements.inputLabel("EIN (Tax ID)"), einField);
        form.addInput(Elements.inputLabel("Tax Exempt?"), taxExempt);
        general.add(form);

        return general;
    }

    private JPanel contact() {

        JPanel contactInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        emailField = Elements.input(location.getEmail());
        phoneField = Elements.input(location.getPhone());

        Form form = new Form();
        form.addInput(Elements.inputLabel("Email Address"), emailField);
        form.addInput(Elements.inputLabel("Phone Number"), phoneField);
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
        form.addInput(Elements.inputLabel("Width"), widthUOM);
        form.addInput(Elements.inputLabel("Length"), lengthUOM);
        form.addInput(Elements.inputLabel("Height"), heightUOM);
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
        form.addInput(Elements.inputLabel("Allow Inventory"), allowsInventory);
        form.addInput(Elements.inputLabel("Allow Production"), allowsProduction);
        form.addInput(Elements.inputLabel("Allow Sales"), allowsSales);
        form.addInput(Elements.inputLabel("Allow Purchasing"), allowsPurchasing);
        form.addInput(Elements.inputLabel("Location Status"), status);
        controls.add(form);

        return controls;
    }

    private void performReview(boolean purge) {

    }
}