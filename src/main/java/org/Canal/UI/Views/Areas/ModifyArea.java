package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * /AREAS/MOD/$
 * Modify an Area
 */
public class ModifyArea extends LockeState {

    //Operating Objects
    private Area area;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //General Info Tab
    private JTextField availableLocations;
    private JTextField areaNameField;
    private Selectable statuses;

    //Controls
    private JCheckBox allowsInventory;
    private JCheckBox allowsProduction;
    private JCheckBox allowsPurchasing;
    private JCheckBox allowsSales;

    //Dimensions
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;

    //Notes Tab
    private RTextScrollPane notes;

    public ModifyArea(Area area, DesktopState desktop, RefreshListener refreshListener) {

        super("Modify " + area.getId(), "/AREAS/MOD/" + area.getId());
        setFrameIcon(new ImageIcon(ModifyArea.class.getResource("/icons/modify.png")));
        this.area = area;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabbedPane = new CustomTabbedPane();
        tabbedPane.addTab("General", general());
        tabbedPane.addTab("Controls", controls());
        tabbedPane.addTab("Notes", notes());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("Modify " + area.getName() + " - " + area.getId(), SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("AREAS", "start_maximized")) {
            setMaximized(true);
        }
    }

    public JPanel toolbar() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton save = new IconButton("Save", "save", "Save Changes");
        save.addActionListener(_ -> {

            area.setName(areaNameField.getText());
            area.setStatus(LockeStatus.valueOf(statuses.getSelectedValue()));
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
            area.save();

            dispose();
            if (refreshListener != null) {
                refreshListener.refresh();
            }
        });
        tb.add(save);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-save");
        rp.getActionMap().put("do-save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save.doClick();
            }
        });

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        if (!area.getStatus().equals(LockeStatus.BLOCKED)) {
            IconButton block = new IconButton("Block", "block", "Block Area", "/AREAS/ARCHV");
            block.addActionListener(_ -> {

                area.setStatus(LockeStatus.BLOCKED);
                area.save();

                dispose();
                if (refreshListener != null) {
                    refreshListener.refresh();
                }
            });
            tb.add(block);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton archive = new IconButton("Archive", "archive", "Archive Area", "/AREAS/ARCHV");
        archive.addActionListener(_ -> {

            if (area.getBins().isEmpty()) {

                area.setStatus(LockeStatus.ARCHIVED);
                area.save();

                dispose();
                if (refreshListener != null) {
                    refreshListener.refresh();
                }
            } else {

            }
        });
        tb.add(archive);
        tb.add(Box.createHorizontalStrut(5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Area", "/AREAS/DEL");
        delete.addActionListener(_ -> {

            if (area.getBins().isEmpty()) {

                area.setStatus(LockeStatus.DELETED);
                area.save();

                dispose();
                if (refreshListener != null) {
                    refreshListener.refresh();
                }
            } else {

            }
        });
        tb.add(delete);
        tb.add(Box.createHorizontalStrut(5));

        panel.add(tb, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel general() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        availableLocations = Elements.input(area.getLocation());
        areaNameField = Elements.input(area.getName(), 20);

        statuses = Selectables.statusTypes();
        statuses.setSelectedValue(String.valueOf(area.getStatus()));

        widthField = new UOMField();
        widthField.setValue(String.valueOf(area.getWidth()));
        widthField.setUOM(area.getWidthUOM());

        lengthField = new UOMField();
        lengthField.setValue(String.valueOf(area.getLength()));
        lengthField.setUOM(area.getLengthUOM());

        heightField = new UOMField();
        heightField.setValue(String.valueOf(area.getHeight()));
        heightField.setUOM(area.getHeightUOM());

        Form form = new Form();
        form.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(area.getId()));
        form.addInput(Elements.coloredLabel("Location", UIManager.getColor("Label.foreground")), availableLocations);
        form.addInput(Elements.coloredLabel("Area Name", Constants.colors[10]), areaNameField);
        form.addInput(Elements.coloredLabel("Status", Constants.colors[10]), statuses);
        form.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        form.addInput(Elements.coloredLabel("Length", Constants.colors[8]), lengthField);
        form.addInput(Elements.coloredLabel("Height", Constants.colors[7]), heightField);
        panel.add(form);

        return panel;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        allowsInventory = new JCheckBox("Inventory Movements", area.allowsInventory());
        allowsProduction = new JCheckBox("Allows Production", area.allowsProduction());
        allowsSales = new JCheckBox("Sales Order Processing", area.allowsSales());
        allowsPurchasing = new JCheckBox("Purchase Order Processing", area.allowsPurchasing());

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Allow Inventory", Constants.colors[0]), allowsInventory);
        form.addInput(Elements.coloredLabel("Allow Production", Constants.colors[1]), allowsProduction);
        form.addInput(Elements.coloredLabel("Allow Sales", Constants.colors[2]), allowsSales);
        form.addInput(Elements.coloredLabel("Allow Purchasing", Constants.colors[3]), allowsPurchasing);
        controls.add(form);

        return controls;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        notes.getTextArea().setText(area.getNotes());
        return notes;
    }

    private void performReview() {

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

        if (!statuses.getSelectedValue().equals("ACTIVE")) {
            addToQueue(new String[]{"WARNING", "Area Status not set to ACTIVE, are you sure?'"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}