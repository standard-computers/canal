package org.Canal.UI.Views;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /$/NEW
 * Create a Location by Objex type
 */
public class CreateLocation extends LockeState {

    private String objexType;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //Header Elements
    private Selectable objexSelection;
    private Selectable organizations;

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

    //Notes Tab
    private RTextScrollPane notes;


    public CreateLocation(String objexType, DesktopState desktop, RefreshListener refreshListener) {

        super("New Location", objexType + "/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateLocation.class.getResource("/icons/create.png")));
        this.objexType = objexType;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.add("General", general());
        tabs.add("Contact", contact());
        tabs.add("Dimensional", dimensional());
        tabs.add("Controls", controls());
        tabs.add("Notes", notes());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("Make a Location", SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(header(), BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy From Location");
        copyFrom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String locationId = JOptionPane.showInputDialog(CreateLocation.this, "Enter Location ID");
                Location location = Engine.getLocationWithId(locationId);

                //Copy General Info
                locationNameField.setText(location.getName());
                line1Field.setText(location.getLine1());
                line2Field.setText(location.getLine2());
                cityField.setText(location.getCity());
                stateField.setText(location.getState());
                postalField.setText(location.getPostal());
                einField.setText(location.getEin());
                einField.setText(location.getEin());
                taxExempt.setSelected(location.isTaxExempt());

                //Copy Contact Info
                emailField.setText(location.getEmail());
                phoneField.setText(location.getPhone());

                //Copy Dimensional Data
                widthUOM.setValue(String.valueOf(location.getWidth()));
                widthUOM.setUOM(location.getWidthUOM());
                lengthUOM.setValue(String.valueOf(location.getLength()));
                lengthUOM.setUOM(location.getLengthUOM());
                heightUOM.setValue(String.valueOf(location.getHeight()));
                heightUOM.setUOM(location.getHeightUOM());

                allowsInventory.setSelected(location.allowsInventory());
                allowsProduction.setSelected(location.allowsProduction());
                allowsSales.setSelected(location.allowsSales());
                allowsPurchasing.setSelected(location.allowsPurchasing());
            }
        });
        buttons.add(copyFrom);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Location Information");
        review.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

            }
        });
        buttons.add(review);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create Location");
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int ccc = JOptionPane.showConfirmDialog(null, "Confirm Location creation?", "Confirm Execution", JOptionPane.YES_NO_OPTION);
                if (ccc == JOptionPane.YES_OPTION) {

                    ArrayList<Location> ls = Engine.getLocations(objexType);
                    String prefix = (String) Engine.codex.getValue(objexType, "prefix");
                    int leadingZeros = (Integer) Engine.codex.getValue(objexType, "leading_zeros"); // e.g., 3 -> 001
                    int nextId = ls.size() + 1;
                    int width = Math.max(0, leadingZeros);
                    String numberPart = String.format("%0" + width + "d", nextId); // zero-pad to width
                    String locationId = prefix + numberPart;

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

                    Pipe.save(objexType, location);

                    dispose();
                    if (refreshListener != null) refreshListener.refresh();

                    //TODO auto_open_new
//                    desktop.put(Engine.router(objexType + "/" + locationId, desktop));
                }
            }
        });
        buttons.add(create);
        buttons.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-create");
        rp.getActionMap().put("do-create", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create.doClick();
            }
        });

        return buttons;
    }

    private JPanel header() {

        Form form = new Form();
        form.addInput(Elements.inputLabel("Type"), objexSelection);
        if (!objexType.equals("/ORGS")) {
            form.addInput(Elements.inputLabel("*Organization"), organizations);
        }
        return form;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String oo = objexType;
        if (oo.startsWith("/")) {
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

        emailField = Elements.input(15);
        phoneField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Email Address"), emailField);
        form.addInput(Elements.inputLabel("Phone Number"), phoneField);
        contactInfo.add(form);

        return contactInfo;
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.inputLabel("Width"), widthUOM);
        form.addInput(Elements.inputLabel("Length"), lengthUOM);
        form.addInput(Elements.inputLabel("Height"), heightUOM);
        dimensional.add(form);

        return dimensional;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        allowsInventory = new JCheckBox("Inventory Movements");
        allowsProduction = new JCheckBox("Allows Production");
        allowsSales = new JCheckBox("Sales Order Processing");
        allowsPurchasing = new JCheckBox("Purchase Order Processing");

        Form form = new Form();
        form.addInput(Elements.inputLabel("Allow Inventory"), allowsInventory);
        form.addInput(Elements.inputLabel("Allow Production"), allowsProduction);
        form.addInput(Elements.inputLabel("Allow Sales"), allowsSales);
        form.addInput(Elements.inputLabel("Allow Purchasing"), allowsPurchasing);
        controls.add(form);

        return controls;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }
}