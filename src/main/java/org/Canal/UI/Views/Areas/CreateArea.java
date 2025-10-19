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
    private Selectable availableLocations;
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
                notes.getTextArea().setText(a.getNotes());
            }
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        review.addActionListener(_ -> performReview(true));
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Refresh Data");
        create.addActionListener(_ -> {

            for (String[] s : getQueue()) {
                if (s[0].equals("CRITICAL")) {
                    JOptionPane.showMessageDialog(CreateArea.this, "Critical message encountered. Click Review.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Area area = new Area();
            area.setId(areaIdField.getText().trim());
            area.setLocation(availableLocations.getSelectedValue());
            area.setName(areaNameField.getText());
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
            if (refreshListener != null) {
                refreshListener.refresh();
            }

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
        statuses = Selectables.statusTypes();
        statuses.setSelectedValue("ACTIVE");

        Form form = new Form();
        form.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), areaIdField);
        form.addInput(Elements.coloredLabel("*Location", UIManager.getColor("Label.foreground")), availableLocations);
        form.addInput(Elements.coloredLabel("Area Name", Constants.colors[10]), areaNameField);
        form.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        form.addInput(Elements.coloredLabel("Length", Constants.colors[8]), lengthField);
        form.addInput(Elements.coloredLabel("Height", Constants.colors[7]), heightField);
        form.addInput(Elements.coloredLabel("Status", Constants.colors[6]), statuses);
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
        form.addInput(Elements.coloredLabel("Allows Inventory", Constants.colors[0]), allowsInventory);
        form.addInput(Elements.coloredLabel("Allows Production", Constants.colors[1]), allowsProduction);
        form.addInput(Elements.coloredLabel("Allows Sales", Constants.colors[2]), allowsSales);
        form.addInput(Elements.coloredLabel("Allows Purchasing", Constants.colors[3]), allowsPurchasing);
        controls.add(form);

        return controls;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview(boolean purge) {

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
        if (purge) purgeQueue();
    }
}