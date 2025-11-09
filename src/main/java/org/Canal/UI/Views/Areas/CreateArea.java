package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

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
    private JTextField locationId;
    private JTextField areaNameField;
    private Selectable statuses;

    //Controls Tab
    private JCheckBox allowsInventory;
    private JCheckBox allowsProduction;
    private JCheckBox allowsPurchasing;
    private JCheckBox allowsSales;

    //Dimensional Tab
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;

    //Notes Tab
    private RTextScrollPane notes;

    public CreateArea(String location, DesktopState desktop, RefreshListener refreshListener) {

        super("New Area", "/AREAS/NEW");
        setFrameIcon(new ImageIcon(CreateArea.class.getResource("/icons/windows/areas.png")));
        this.location = location;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());

        if ((boolean) Engine.codex.getValue("AREAS", "allow_notes")) {
            tabs.addTab("Notes", notes());
        }

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("New Area", SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("AREAS", "start_maximized")) {
            setMaximized(true);
        }
    }

    public JPanel toolbar() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy Area");
        copyFrom.addActionListener(_ -> {

            String areaId = JOptionPane.showInputDialog(CreateArea.this, "Enter Area ID", "Copy Area", JOptionPane.QUESTION_MESSAGE);
            if (!areaId.isEmpty()) {

                Area a = Engine.getArea(areaId);
                locationId.setText(a.getLocation());
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
                notes.getTextArea().setText(a.getNotes());
            }
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Refresh Data");
        create.addActionListener(_ -> {

            if (areaIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Area ID cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Area area = new Area();
            area.setId(areaIdField.getText().trim());
            area.setLocation(locationId.getText().trim());
            area.setName(areaNameField.getText().trim());
            area.setWidth(Double.parseDouble(widthField.getValue()));
            area.setWidthUOM(widthField.getUOM());
            area.setLength(Double.parseDouble(lengthField.getValue()));
            area.setLengthUOM(lengthField.getUOM());
            area.setHeight(Double.parseDouble(heightField.getValue()));
            area.setHeightUOM(heightField.getUOM());
            area.setAllowsInventory(allowsInventory.isSelected());
            area.setAllowsProduction(allowsProduction.isSelected());
            area.setAllowsPurchasing(allowsPurchasing.isSelected());
            area.setAllowsSales(allowsSales.isSelected());
            area.setNotes(notes.getTextArea().getText());
            area.setStatus(LockeStatus.valueOf(statuses.getSelectedValue()));

            Pipe.save("/AREAS", area);

            dispose();
            if (refreshListener != null) refreshListener.refresh();

            if ((boolean) Engine.codex.getValue("AREAS", "item_created_alert")) {
                JOptionPane.showMessageDialog(CreateArea.this, "Area Created");
            }
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-modify");
        rp.getActionMap().put("do-modify", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create.doClick();
            }
        });

        panel.add(tb, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel general() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        areaIdField = Elements.input();
        locationId = Elements.input();
        if (location == null) {
            areaIdField.setText(Engine.generateId("AREAS"));
        } else {
            areaIdField.setText(Engine.generateId("AREAS") + "-" + location);
            locationId.setText(location);
        }
        areaNameField = Elements.input(areaIdField.getText(), 20);
        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();
        statuses = Selectables.statusTypes();
        statuses.setSelectedValue("ACTIVE");

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New ID"), areaIdField);
        form.addInput(Elements.inputLabel("*Location"), locationId);
        form.addInput(Elements.inputLabel("Area Name"), areaNameField);
        form.addInput(Elements.inputLabel("Width"), widthField);
        form.addInput(Elements.inputLabel("Length"), lengthField);
        form.addInput(Elements.inputLabel("Height"), heightField);
        form.addInput(Elements.inputLabel("Status"), statuses);
        panel.add(form);

        return panel;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        allowsInventory = new JCheckBox("Inventory Movements");
        allowsProduction = new JCheckBox("Allows Production");
        allowsSales = new JCheckBox("Sales Order Processing");
        allowsPurchasing = new JCheckBox("Purchase Order Processing");

        Form form = new Form();
        form.addInput(Elements.inputLabel("Allows Inventory", "Inventory may be kept within this are if checked."), allowsInventory);
        form.addInput(Elements.inputLabel("Allows Production", "Production can be performed in this area if checked."), allowsProduction);
        form.addInput(Elements.inputLabel("Allows Sales", "Goods Issues can be performed within this area if checked."), allowsSales);
        form.addInput(Elements.inputLabel("Allows Purchasing", "Goods Receipts can be performed within this are if checked."), allowsPurchasing);
        controls.add(form);

        return controls;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
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

        double volume = Double.parseDouble(heightField.getValue()) * Double.parseDouble(lengthField.getValue()) * Double.parseDouble(widthField.getValue());


        addToQueue(new String[]{"MESSAGE", "Calculated volume is " + volume + " " + heightField.getUOM() + 3});
//        addToQueue(new String[]{"WARNING", "Height is set to zero 0"});
        if (allowsProduction.isSelected()) {
            Location l = Engine.getLocationWithId(areaIdField.getText());
            if (l != null) {
                if (!l.allowsProduction()) {
                    addToQueue(new String[]{"CRITICAL", "The selected location does not allow production and will fail."});
                }
            }
        }

        if (!allowsInventory.isSelected()) {
            addToQueue(new String[]{"WARNING", "'Allows Inventory' is not selected, are you sure?'"});
        }

        if (!statuses.getSelectedValue().equals("ACTIVE")) {
            addToQueue(new String[]{"WARNING", "Area Status not set to ACTIVE, are you sure?'"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}