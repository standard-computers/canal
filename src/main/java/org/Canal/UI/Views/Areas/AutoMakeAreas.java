package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * /AREAS/AUTO_MK
 */
public class AutoMakeAreas extends LockeState {

    //Operating Object
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //General Area Info
    private JTextField areaIdField;
    private JTextField areaNameField;
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;

    //Controls
    private JCheckBox allowsInventory;
    private JCheckBox allowsProduction;
    private JCheckBox allowsPurchasing;
    private JCheckBox allowsSales;

    //Locations to make Area for
    private JPanel checkboxPanel;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes;

    public AutoMakeAreas(DesktopState desktop, RefreshListener refreshListener) {

        super("AutoMake Areas", "/AREAS/AUTO_MK");
        setFrameIcon(new ImageIcon(AutoMakeAreas.class.getResource("/icons/automake.png")));
        setLayout(new BorderLayout());
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());
        tabs.addTab("Locations", locations());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("AutoMake Areas", SwingConstants.LEFT), BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("AutoMake", "automake", "Start AutoMake");
        create.addActionListener(_ -> {



            for (JCheckBox checkbox : checkboxes) {

                if (checkbox.isSelected()) {

                    Area newArea = new Area();
                    newArea.setId(areaIdField.getText().trim().replace("@", checkbox.getActionCommand()));
                    newArea.setLocation(checkbox.getActionCommand());
                    newArea.setName(areaNameField.getText().replace("@", checkbox.getActionCommand()));
                    newArea.setWidth(Double.parseDouble(widthField.getValue()));
                    newArea.setWidthUOM(widthField.getUOM());
                    newArea.setLength(Double.parseDouble(lengthField.getValue()));
                    newArea.setLengthUOM(lengthField.getUOM());
                    newArea.setHeight(Double.parseDouble(heightField.getValue()));
                    newArea.setHeightUOM(heightField.getUOM());
                    newArea.setAllowsInventory(allowsInventory.isSelected());
                    newArea.setAllowsProduction(allowsProduction.isSelected());
                    newArea.setAllowsSales(allowsSales.isSelected());
                    newArea.setAllowsPurchasing(allowsPurchasing.isSelected());
                    Pipe.save("/AREAS", newArea);
                }
            }
            dispose();
            JOptionPane.showMessageDialog(AutoMakeAreas.this, "AutoMake Complete");

            if (refreshListener != null) {
                refreshListener.refresh();
            }
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        panel.add(tb, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        areaIdField = Elements.input("HBD1-@");
        areaNameField = Elements.input("Highbay");
        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Area ID (current: LOC_ID-001)"), areaIdField);
        form.addInput(Elements.inputLabel("Area Name (current: LOC_ID-001"), areaNameField);
        form.addInput(Elements.inputLabel("Width"), widthField);
        form.addInput(Elements.inputLabel("Length"), lengthField);
        form.addInput(Elements.inputLabel("Height"), heightField);
        general.add(form);

        return general;
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

    private JPanel locations() {

        JPanel locationsSelection = new JPanel(new BorderLayout());
        locations = Engine.getLocations();

        this.checkboxes = new ArrayList<>();
        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        addCheckboxes();

        JScrollPane js = new JScrollPane(checkboxPanel);
        js.setPreferredSize(new Dimension(200, 200));

        JPanel selector = new JPanel(new BorderLayout());

        JTextField search = Elements.input();
        search.addActionListener(_ -> {

            String searchValue = search.getText().trim();
            if (searchValue.endsWith("*")) { //Searching for ID starts with
                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().startsWith(searchValue.substring(0, searchValue.length() - 1))) {
                        checkbox.setSelected(!checkbox.isSelected());
                    }
                }
            } else if (searchValue.startsWith("/")) { //Objex type selection

                for (int i = 0; i < checkboxes.size(); i++) {
                    if (locations.get(i).getType().equals(searchValue.toUpperCase().replaceFirst("/", ""))) {
                        checkboxes.get(i).setSelected(!checkboxes.get(i).isSelected());
                    }
                }

            } else { //Select exact match
                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().equals(searchValue)) {
                        checkbox.setSelected(!checkbox.isSelected());
                    }
                }
            }
        });
        selector.add(search, BorderLayout.NORTH);

        selector.add(js, BorderLayout.CENTER);

        JPanel opts = new JPanel(new GridLayout(1, 2));

        JButton selectAll = Elements.button("Select All");
        selectAll.addActionListener(_ -> {
            checkboxes.forEach(cb -> cb.setSelected(true));
            repaint();
        });
        opts.add(selectAll);

        JButton deselectAll = Elements.button("Deselect All");
        deselectAll.addActionListener(_ -> {
            checkboxes.forEach(cb -> cb.setSelected(false));
            repaint();
        });
        opts.add(deselectAll);

        selector.add(opts, BorderLayout.SOUTH);
        locationsSelection.add(selector, BorderLayout.CENTER);

        return locationsSelection;
    }

    private void addCheckboxes() {

        for (Location location : locations) {
            String displayText = location.getId() + " - " + location.getName();
            JCheckBox checkbox = new JCheckBox(displayText);
            checkbox.setActionCommand(String.valueOf(location.getId())); // Set the value as ID
            checkboxes.add(checkbox);
            checkboxPanel.add(checkbox);
        }
    }

    private void performReview() {

        if (areaIdField.getText().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Area ID field empty!"});
        }

        if (areaNameField.getText().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Area Name field empty!"});
        }

        if (!allowsInventory.isSelected()) {
            addToQueue(new String[]{"WARNING", "Area does not allow inventory movements, are you sure?"});
        }

        if (widthField.getValue().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Area WIDTH dimension is set to 0, are you sure?"});
        }

        if (lengthField.getValue().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Area LENGTH dimension is set to 0, are you sure?"});
        }

        if (heightField.getValue().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Area HEIGHT dimension is set to 0, are you sure?"});
        }

        int locationCount = 0;
        for (JCheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                locationCount++;
            }
        }
        if (locationCount == 0) {
            addToQueue(new String[]{"CRITICAL", "NO LOCATIONS SELECTED!!!"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}