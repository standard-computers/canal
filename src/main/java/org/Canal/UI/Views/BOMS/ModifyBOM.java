package org.Canal.UI.Views.BOMS;

import org.Canal.Models.SupplyChainUnits.BillOfMaterials;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.Models.SupplyChainUnits.Task;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Products.CreateInclusion;
import org.Canal.UI.Views.System.LockeMessages;
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
 * /BOMS/MOD/$[BOM_ID]
 */
public class ModifyBOM extends LockeState implements Includer {

    //Operating Objects
    private BillOfMaterials billOfMaterials;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //General Info Tab
    private JTextField bomNameField;
    private JTextField locationField;
    private JTextField itemId;
    private JTextField customerField;

    //Controls Tab
    private Selectable status;

    private CustomTable bomsView;
    private CustomTable stepsView;

    //Notes Tab
    private RTextScrollPane notes;

    public ModifyBOM(BillOfMaterials billOfMaterials, DesktopState desktop, RefreshListener refreshListener) {

        super("Modify Bill of Materials", "/BOMS/MOD/" + billOfMaterials.getId());
        setFrameIcon(new ImageIcon(ModifyBOM.class.getResource("/icons/modify.png")));
        this.billOfMaterials = billOfMaterials;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Components", components());
        tabs.addTab("Steps", steps());
        tabs.addTab("Controls", controls());
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel header() {

        JPanel header = new JPanel(new BorderLayout());
        JPanel itemInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        bomNameField = Elements.input(billOfMaterials.getName());
        locationField = Elements.input(billOfMaterials.getLocation());
        itemId = Elements.input(billOfMaterials.getItem());
        customerField = Elements.input(billOfMaterials.getCustomer());

        Form form = new Form();
        form.addInput(Elements.coloredLabel("BOM Name", UIManager.getColor("Label.foreground")), bomNameField);
        form.addInput(Elements.coloredLabel("Production Location", UIManager.getColor("Label.foreground")), locationField);
        form.addInput(Elements.coloredLabel("Finished Item ID", UIManager.getColor("Label.foreground")), itemId);
        form.addInput(Elements.coloredLabel("Customer", UIManager.getColor("Label.foreground")), customerField);
        itemInfo.add(form);

        header.add(itemInfo, BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.NORTH);

        return header;
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton save = new IconButton("Save", "save", "Save modifications");
        save.addActionListener(_ -> {

            billOfMaterials.setName(bomNameField.getText());
            billOfMaterials.setLocation(locationField.getText());
            billOfMaterials.setItem(itemId.getText());
            billOfMaterials.setCustomer(customerField.getText());
            billOfMaterials.setNotes(notes.getTextArea().getText());
            billOfMaterials.save();

            dispose();

            if (refreshListener != null) {
                refreshListener.refresh();
            }
            //TODO Alert and close
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

        IconButton review = new IconButton("Review", "review", "Review changes");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        toolbar.add(Elements.header("Modifying " + billOfMaterials.getId() + " " + billOfMaterials.getName(), SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private CustomTable componentsTable() {
        String[] columns = new String[]{
                "Component",
                "Item ID",
                "Item Name",
                "Vendor",
                "Req Qty",
                "Qty UOM",
                "Price",
                "Total"
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for (int s = 0; s < billOfMaterials.getComponents().size(); s++) {
            StockLine ol = billOfMaterials.getComponents().get(s);
            Item i = Engine.getItem(ol.getItem());
            data.add(new Object[]{
                    String.valueOf(s + 1),
                    ol.getItem(),
                    ol.getName(),
                    i.getVendor(),
                    ol.getQuantity(),
                    ol.getUnitOfMeasure(),
                    ol.getValue(),
                    (ol.getValue() * ol.getQuantity())
            });
        }

        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));

                    }
                }
            }
        });
        return ct;
    }

    private JPanel components() {

        JPanel bom = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from item");
        copyFrom.addActionListener(_ -> {
            String iid = JOptionPane.showInputDialog("Enter Item ID");
            Item i = Engine.getItem(iid);
            bomNameField.setText(i.getName());
            itemId.setText(i.getId());
            billOfMaterials.setComponents(i.getComponents());
            refreshComponents();
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton addComponent = new IconButton("Add Component", "add_rows", "Add Component");
        addComponent.addActionListener(_ -> desktop.put(new CreateInclusion(ModifyBOM.this)));
        tb.add(addComponent);
        tb.add(Box.createHorizontalStrut(5));

        IconButton removeComponent = new IconButton("Remove Component", "delete_rows", "Remove Selected Component");
        removeComponent.addActionListener((ActionEvent _) -> {
            int selectedRow = bomsView.getSelectedRow();
            if (selectedRow != -1) {
                billOfMaterials.getComponents().remove(selectedRow);
                refreshComponents();
            }
        });
        tb.add(removeComponent);
        tb.add(Box.createHorizontalStrut(5));

        IconButton reprice = new IconButton("Refresh", "refresh", "Refresh component data");
        reprice.addActionListener(_ -> {
            for (int i = 0; i < billOfMaterials.getComponents().size(); i++) {
                Item ti = Engine.getItem(billOfMaterials.getComponents().get(i).getItem());
                billOfMaterials.getComponents().get(i).setValue(ti.getPrice());
            }
            refreshComponents();
        });
        tb.add(reprice);
        tb.add(Box.createHorizontalStrut(5));


        bom.add(tb, BorderLayout.NORTH);
        bomsView = componentsTable();
        bom.add(new JScrollPane(bomsView), BorderLayout.CENTER);

        return bom;
    }

    private CustomTable stepsTable() {
        String[] columns = new String[]{
                "Type",
                "Name",
                "Description",
                "Location",
                "Locke",
                "Objex",
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for (int s = 0; s < billOfMaterials.getSteps().size(); s++) {
            Task ol = billOfMaterials.getSteps().get(s);
            data.add(new Object[]{
                    String.valueOf(s + 1),
                    ol.getType(),
                    ol.getName(),
                    ol.getDescription(),
                    ol.getLocation(),
                    ol.getLocke(),
                    ol.getObjex(),
            });
        }

        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));

                    }
                }
            }
        });
        return ct;
    }

    private JPanel steps() {

        JPanel stps = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from BOM");
        copyFrom.addActionListener(_ -> {
            String bid = JOptionPane.showInputDialog("Enter BOM ID");
            BillOfMaterials bom = Engine.getBoM(bid);
            billOfMaterials.setSteps(bom.getSteps());
            refreshSteps();
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton addStep = new IconButton("Add Step", "add_rows", "Add Step");
        addStep.addActionListener(_ -> desktop.put(new CreateInclusion(ModifyBOM.this)));
        tb.add(addStep);
        tb.add(Box.createHorizontalStrut(5));

        IconButton removeStep = new IconButton("Remove Step", "delete_rows", "Remove Selected Step");
        removeStep.addActionListener((ActionEvent _) -> {
            int selectedRow = bomsView.getSelectedRow();
            if (selectedRow != -1) {
                billOfMaterials.getComponents().remove(selectedRow);
                refreshComponents();
            }
        });
        tb.add(removeStep);
        tb.add(Box.createHorizontalStrut(5));

        stps.add(tb, BorderLayout.NORTH);
        stepsView = stepsTable();
        stps.add(new JScrollPane(stepsView), BorderLayout.CENTER);

        return stps;
    }


    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        status = Selectables.statusTypes();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), status);
        controls.add(form);

        return controls;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        notes.getTextArea().setText(billOfMaterials.getNotes());
        return notes;
    }

    private void performReview() {

        if (bomNameField.getText().trim().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Bill of Materials name not set. Are you sure?"});
        }

        if (locationField.getText().trim().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Bill of Materials production location not set. Are you sure?"});
        }

        if (itemId.getText().trim().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Bill of Materials must have finished item ID!!!"});
        }

        if (itemId.getText().trim().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Bill of Materials customer not set. Are you sure?"});
        }

        if (billOfMaterials.getComponents().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Bill of Materials has no components. No point!"});
        }

        if (billOfMaterials.getSteps().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Bill of Materials has no steps. Are you sure?"});
        }

        if (!status.getSelectedValue().equals("ACTIVE")) {
            addToQueue(new String[]{"WARNING", "BOM status MUST be ACTIVE to use for production!"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }

    private void refreshComponents() {

        CustomTable newTable = componentsTable();
        JScrollPane scrollPane = (JScrollPane) bomsView.getParent().getParent();
        scrollPane.setViewportView(newTable);
        bomsView = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    private void refreshSteps() {

        CustomTable newTable = stepsTable();
        JScrollPane scrollPane = (JScrollPane) stepsView.getParent().getParent();
        scrollPane.setViewportView(newTable);
        stepsView = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    @Override
    public void commitInclusion(Item item, double qty, String uom) {

        StockLine newComponent = new StockLine();
        newComponent.setItem(item.getId());
        newComponent.setName(item.getName());
        newComponent.setQuantity(qty);
        newComponent.setUnitOfMeasure(uom);
        newComponent.setValue(item.getPrice());
        billOfMaterials.getComponents().add(newComponent);
        billOfMaterials.getComponents().sort((a, b) -> {
            int nameCmp = a.getName().compareToIgnoreCase(b.getName());
            if (nameCmp != 0) {
                return nameCmp;
            }
            return a.getId().compareToIgnoreCase(b.getId());
        });
        refreshComponents();
    }

    @Override
    public void commitUoM(String uom, double qty, String baseQtyUom) {
    }

    @Override
    public void commitStep(Task task) {

        billOfMaterials.getSteps().add(task);
        refreshSteps();
    }
}