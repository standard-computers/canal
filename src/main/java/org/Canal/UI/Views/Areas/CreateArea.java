package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * /AREAS/NEW
 * Create a new Area for a location
 */
public class CreateArea extends LockeState {

    //General Info Tab
    private String location;
    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JTextField areaIdField;
    private Selectable availableLocations;
    private JTextField areaNameField;

    //Controls Tab
    private JCheckBox allowsInventory;
    private JCheckBox allowsProduction;
    private JCheckBox allowsPurchasing;
    private JCheckBox allowsSales;

    //Dimensional Tab
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;

    public CreateArea(String location, DesktopState desktop, RefreshListener refreshListener) {

        super("New Area", "/AREAS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateArea.class.getResource("/icons/areas.png")));
        this.location = location;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("New Area", SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    public JPanel toolbar() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy Area");
        copyFrom.addActionListener(_ -> {

            String areaId = JOptionPane.showInputDialog(CreateArea.this, "Enter Area ID", "Copy Area", JOptionPane.QUESTION_MESSAGE);
            if (!areaId.isEmpty()) {

                Area a = Engine.getArea(areaId);
                availableLocations.setSelectedValue(a.getLocation());
                areaNameField.setText(a.getName());
                widthField.setValue(String.valueOf(a.getWidth()));
                widthField.setUOM(a.getWidthUOM());
                lengthField.setValue(String.valueOf(a.getLength()));
                lengthField.setUOM(a.getLengthUOM());
                heightField.setValue(String.valueOf(a.getHeight()));
                heightField.setUOM(a.getHeightUOM());
                allowsInventory.setSelected(a.allowsInventory());
                allowsProduction.setSelected(a.allowsProduction());
                allowsSales.setSelected(a.allowsSales());
                allowsPurchasing.setSelected(a.allowsPurchasing());
            }
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "execute", "Refresh Data");
        create.addActionListener(_ -> {

            Area newArea = new Area();

            newArea.setId(areaIdField.getText().trim());
            newArea.setLocation(availableLocations.getSelectedValue());
            newArea.setName(areaNameField.getText());
            newArea.setWidth(Double.parseDouble(widthField.getValue()));
            newArea.setWidthUOM(widthField.getUOM());
            newArea.setLength(Double.parseDouble(lengthField.getValue()));
            newArea.setLengthUOM(lengthField.getUOM());
            newArea.setHeight(Double.parseDouble(heightField.getValue()));
            newArea.setHeightUOM(heightField.getUOM());
            newArea.setAllowsInventory(allowsInventory.isSelected());
            newArea.setAllowsProduction(allowsProduction.isSelected());
            newArea.setAllowsPurchasing(allowsPurchasing.isSelected());
            newArea.setAllowsSales(allowsSales.isSelected());

            Pipe.save("/AREAS", newArea);

            dispose();
            if (refreshListener != null) {
                refreshListener.refresh();
            }

            if ((boolean) Engine.codex.getValue("AREAS", "item_created_alert")) {
                JOptionPane.showMessageDialog(CreateArea.this, "Area Created");
            }

        });
        tb.add(create);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));

        panel.add(tb, BorderLayout.SOUTH);
        return panel;
    }

    public JPanel general() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        areaIdField = Elements.input();
        availableLocations = Selectables.allLocations();
        if (location == null) {
            areaIdField.setText("A-" + Engine.getAreas().size());
        } else {
            areaIdField.setText("A-" + (Engine.getAreas(location).size() + 1) + "-" + location);
            availableLocations.setSelectedValue(location);
        }
        areaNameField = Elements.input(areaIdField.getText(), 20);
        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();

        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), areaIdField);
        f.addInput(Elements.coloredLabel("*Location", UIManager.getColor("Label.foreground")), availableLocations);
        f.addInput(Elements.coloredLabel("Area Name", Constants.colors[10]), areaNameField);
        f.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[8]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[7]), heightField);
        panel.add(f);

        return panel;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form f = new Form();

        allowsInventory = new JCheckBox("Inventory Movements");
        allowsProduction = new JCheckBox("Allows Production");
        allowsSales = new JCheckBox("Sales Order Processing");
        allowsPurchasing = new JCheckBox("Purchase Order Processing");

        f.addInput(Elements.coloredLabel("Allow Inventory", Constants.colors[0]), allowsInventory);
        f.addInput(Elements.coloredLabel("Allow Production", Constants.colors[1]), allowsProduction);
        f.addInput(Elements.coloredLabel("Allow Sales", Constants.colors[2]), allowsSales);
        f.addInput(Elements.coloredLabel("Allow Purchasing", Constants.colors[3]), allowsPurchasing);
        controls.add(f);

        return controls;
    }

    private void performReview() {

        if (areaIdField.getText().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Area ID is empty!"});
        } else {
            Area search = Engine.getArea(areaIdField.getText());
            if (search != null) {
                addToQueue(new String[]{"WARNING", "Area ID already in use!"});
            }
        }
        if (areaNameField.getText().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Area name is empty!"});
        }
        if (Double.parseDouble(widthField.getValue()) == 0) {
            addToQueue(new String[]{"WARNING", "Width is set to zero 0"});
        }
        if (Double.parseDouble(lengthField.getValue()) == 0) {
            addToQueue(new String[]{"WARNING", "Length is set to zero 0"});
        }
        if (Double.parseDouble(heightField.getValue()) == 0) {
            addToQueue(new String[]{"WARNING", "Height is set to zero 0"});
        }
        if (!allowsInventory.isSelected()) {
            addToQueue(new String[]{"WARNING", "'Allows Inventory' is not selected, are you sure?'"});
        }
        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}