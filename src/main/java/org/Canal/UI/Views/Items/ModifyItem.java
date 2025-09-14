package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.Models.SupplyChainUnits.Task;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Products.CreateInclusion;
import org.Canal.UI.Views.Products.CreateUoM;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ITS/MOD/$[ITEM_ID]
 */
public class ModifyItem extends LockeState implements Includer {

    //Operating Objects
    private Item item;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //General Info Tab
    private JTextField idField;
    private JTextField orgField;
    private JTextField nameField;
    private JTextField linkField;
    private JTextField vendorField;
    private JCheckBox batchedField;
    private JCheckBox rentableField;
    private JCheckBox skudField;
    private JCheckBox consumableField;
    private JCheckBox virtualField;
    private JTextField priceField;
    private JTextField upcField;
    private JTextField vendorNumberField;

    //Dimensional Tab
    private JTextField baseQuantityField;
    private Selectable packagingUomField;
    private JTextField colorField;
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;
    private JTextField taxField;
    private JTextField exciseTaxfield;

    private CustomTable bomsView;
    private CustomTable uomsView;
    private CustomTable packagingView;

    public ModifyItem(Item item, DesktopState desktop, RefreshListener refreshListener) {

        super("Modify Item / " + item.getId() + " - " + item.getName(), "/ITS/" + item.getId());
        setFrameIcon(new ImageIcon(ModifyItem.class.getResource("/icons/modify.png")));
        this.item = item;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", itemInfo());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Bill of Materials", billOfMaterials());
        tabs.addTab("Units of Measure", unitsOfMeasure());
        tabs.addTab("Packaging", packaging());

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        setMaximized(true);
    }

    private JPanel dimensional() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f2 = new Form();

        baseQuantityField = Elements.input(String.valueOf(item.getBaseQuantity()));

        packagingUomField = Selectables.packagingUoms();
        packagingUomField.setSelectedValue(item.getPackagingUnit());

        colorField = Elements.input(item.getColor());

        consumableField = new JCheckBox();
        if (item.isConsumable()) {
            consumableField.setSelected(true);
        }

        virtualField = new JCheckBox("Like software");
        if (item.isVirtual()) {
            virtualField.setSelected(true);
        }

        widthField = new UOMField();
        widthField.setValue(String.valueOf(item.getWidth()));
        widthField.setUOM(item.getWidthUOM());

        lengthField = new UOMField();
        lengthField.setValue(String.valueOf(item.getLength()));
        lengthField.setUOM(item.getLengthUOM());

        heightField = new UOMField();
        heightField.setValue(String.valueOf(item.getHeight()));
        heightField.setUOM(item.getHeightUOM());

        weightField = new UOMField();
        weightField.setValue(String.valueOf(item.getWeight()));
        weightField.setUOM(item.getWeightUOM());

        taxField = Elements.input(String.valueOf(item.getTax()));
        exciseTaxfield = Elements.input(String.valueOf(item.getExciseTax()));

        f2.addInput(Elements.coloredLabel("Packaging Base Quantity", UIManager.getColor("Label.foreground")), baseQuantityField);
        f2.addInput(Elements.coloredLabel("Packaging UOM", UIManager.getColor("Label.foreground")), packagingUomField);
        f2.addInput(Elements.coloredLabel("Consumable?", UIManager.getColor("Label.foreground")), consumableField);
        f2.addInput(Elements.coloredLabel("Color", UIManager.getColor("Label.foreground")), colorField);
        f2.addInput(Elements.coloredLabel("Width", UIManager.getColor("Label.foreground")), widthField);
        f2.addInput(Elements.coloredLabel("Length", UIManager.getColor("Label.foreground")), lengthField);
        f2.addInput(Elements.coloredLabel("Height", UIManager.getColor("Label.foreground")), heightField);
        f2.addInput(Elements.coloredLabel("Weight", UIManager.getColor("Label.foreground")), weightField);
        f2.addInput(Elements.coloredLabel("Tax", UIManager.getColor("Label.foreground")), taxField);
        f2.addInput(Elements.coloredLabel("Excise Tax", UIManager.getColor("Label.foreground")), exciseTaxfield);
        p.add(f2);
        return p;
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
        for (int s = 0; s < item.getComponents().size(); s++) {
            StockLine ol = item.getComponents().get(s);
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

    private JPanel billOfMaterials() {

        JPanel bom = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton addComponent = new IconButton("Add", "add_rows", "Add Component");
        addComponent.addActionListener(_ -> desktop.put(new CreateInclusion(ModifyItem.this)));
        tb.add(addComponent);
        tb.add(Box.createHorizontalStrut(5));

        IconButton removeComponent = new IconButton("Remove Selected", "delete_rows", "Remove Selected Component");
        removeComponent.addActionListener((ActionEvent _) -> {
            int selectedRow = bomsView.getSelectedRow();
            if (selectedRow != -1) {
                item.getComponents().remove(selectedRow);
                refreshComponents();
            }
        });
        tb.add(removeComponent);
        tb.add(Box.createHorizontalStrut(5));

        IconButton reprice = new IconButton("Refresh", "refresh", "Refresh item data");
        reprice.addActionListener(_ -> {
            for (int i = 0; i < item.getComponents().size(); i++) {
                Item ti = Engine.getItem(this.item.getComponents().get(i).getItem());
                this.item.getComponents().get(i).setName(ti.getName());
                this.item.getComponents().get(i).setValue(ti.getPrice());
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


    private JPanel unitsOfMeasure() {

        JPanel unitsOfMeasure = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton addUoM = new IconButton("Add UoM", "add_rows", "Add Unit of Measure");
        addUoM.addActionListener(_ -> desktop.put(new CreateUoM(ModifyItem.this)));
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addUoM);

        IconButton removeUoM = new IconButton("Remove UoM", "delete_rows", "Remove Selected");
        removeUoM.addActionListener(_ -> {
        });
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeUoM);

        unitsOfMeasure.setLayout(new BorderLayout());
        unitsOfMeasure.add(tb, BorderLayout.NORTH);

        uomsView = new CustomTable(new String[]{
                "Qty",
                "UOM",
                "Base Qty",
                "Base Qty UOM"
        }, item.getUoms());
        unitsOfMeasure.add(new JScrollPane(uomsView));
        return unitsOfMeasure;
    }

    private JPanel packaging() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return p;
    }

    private JPanel itemInfo() {

        JPanel itemInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        idField = new Copiable(item.getId());
        orgField = Elements.input(item.getOrg());
        nameField = Elements.input(item.getName());
        linkField = Elements.input(item.getLink());
        vendorField = Elements.input(item.getVendor());

        batchedField = new JCheckBox(" Item expires");
        if (item.isBatched()) {
            batchedField.setSelected(true);
        }

        rentableField = new JCheckBox(" Item can be rented");
        if (item.isRentable()) {
            rentableField.setSelected(true);
        }

        skudField = new JCheckBox(" Item has unique SKU");
        if (item.isSkud()) {
            skudField.setSelected(true);
        }

        priceField = Elements.input(String.valueOf(item.getPrice()));
        upcField = Elements.input(item.getUpc());
        vendorNumberField = Elements.input(item.getVendorNumber());

        Form form = new Form();
        form.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), idField);
        form.addInput(Elements.coloredLabel("Organization", UIManager.getColor("Label.foreground")), orgField);
        form.addInput(Elements.coloredLabel("Name", UIManager.getColor("Label.foreground")), nameField);
        form.addInput(Elements.coloredLabel("Link", UIManager.getColor("Label.foreground")), linkField);
        form.addInput(Elements.coloredLabel("Vendor", UIManager.getColor("Label.foreground")), vendorField);
        form.addInput(Elements.coloredLabel("Batched", UIManager.getColor("Label.foreground")), batchedField);
        form.addInput(Elements.coloredLabel("Rentable", UIManager.getColor("Label.foreground")), rentableField);
        form.addInput(Elements.coloredLabel("SKU'd", UIManager.getColor("Label.foreground")), skudField);
        form.addInput(Elements.coloredLabel("Price", UIManager.getColor("Label.foreground")), priceField);
        form.addInput(Elements.coloredLabel("UPC", UIManager.getColor("Label.foreground")), upcField);
        form.addInput(Elements.coloredLabel("Vendor Number", UIManager.getColor("Label.foreground")), vendorNumberField);
        itemInfo.add(form);

        return itemInfo;
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton save = new IconButton("Save", "save", "Save modifications");
        save.addActionListener(_ -> {

            item.setName(nameField.getText());
            item.setLink(linkField.getText());
            item.setVendor(vendorField.getText());
            item.setColor(colorField.getText());
            item.setBatched(batchedField.isSelected());
            item.setRentable(rentableField.isSelected());
            item.setSkud(skudField.isSelected());
            item.setConsumable(consumableField.isSelected());
            item.setPrice(Double.parseDouble(priceField.getText()));
            item.setUpc(upcField.getText());
            item.setVendorNumber(vendorNumberField.getText());

            item.setBaseQuantity(Double.parseDouble(baseQuantityField.getText()));
            item.setPackagingUnit(packagingUomField.getSelectedValue());

            item.setTax(Double.parseDouble(taxField.getText()));
            item.setExciseTax(Double.parseDouble(exciseTaxfield.getText()));

            //Load dimensional info
            item.setWidth(Double.parseDouble(widthField.getValue()));
            item.setWidthUOM(widthField.getUOM());
            item.setLength(Double.parseDouble(lengthField.getValue()));
            item.setLengthUOM(lengthField.getUOM());
            item.setHeight(Double.parseDouble(heightField.getValue()));
            item.setHeightUOM(heightField.getUOM());
            item.setWeight(Double.parseDouble(weightField.getValue()));
            item.setWeightUOM(weightField.getUOM());

            item.save();

            if ((boolean) Engine.codex.getValue("ITS", "dispose_on_save")) {
                dispose();
            }

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

        IconButton autoprice = new IconButton("Autoprice", "autoprice", "");
        autoprice.addActionListener(_ -> {
            double estPrice = 0.0;
            for (StockLine o : item.getComponents()) {
                estPrice += o.getValue() * o.getQuantity();
            }
            priceField.setText(Double.toString(estPrice));
        });
        tb.add(autoprice);
        tb.add(Box.createHorizontalStrut(5));

        if ((boolean) Engine.codex.getValue("ITS", "allow_archival")) {
            IconButton archive = new IconButton("Archive", "archive", "Archive item");
            archive.addActionListener(_ -> {
                //TODO Codex for confirmation
                item.setStatus(LockeStatus.ARCHIVED);
                item.save();
                dispose();
            });
            tb.add(archive);
            tb.add(Box.createHorizontalStrut(5));
        }

        if ((boolean) Engine.codex.getValue("ITS", "allow_deletion")) {
            IconButton delete = new IconButton("Delete", "delete", "Delete item");
            delete.addActionListener(_ -> {

            });
            tb.add(delete);
            tb.add(Box.createHorizontalStrut(5));
            int mask2 = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
            KeyStroke ks2 = KeyStroke.getKeyStroke(KeyEvent.VK_D, mask2);
            JRootPane rp2 = getRootPane();
            rp2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks2, "do-delete");
            rp2.getActionMap().put("do-delete", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (delete != null && delete.isEnabled()) {
                        delete.doClick();
                    }
                }
            });
        }

        toolbar.add(Elements.header("Modifying " + item.getId() + " – " + item.getName(), SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private void performReview() {

    }

    private void refreshComponents() {
        CustomTable newTable = componentsTable();
        JScrollPane scrollPane = (JScrollPane) bomsView.getParent().getParent();
        scrollPane.setViewportView(newTable);
        bomsView = newTable;
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
        this.item.addComponent(newComponent);
        this.item.getComponents().sort((a, b) -> {
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

        item.getUoms().add(new Object[]{1, uom, qty, baseQtyUom});
        CustomTable newUoms = new CustomTable(new String[]{
                "Qty",
                "UOM",
                "Base Qty",
                "Base Qty UOM"
        }, item.getUoms());
        JScrollPane scrollPane = (JScrollPane) uomsView.getParent().getParent();
        scrollPane.setViewportView(newUoms);
        uomsView = newUoms;
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    @Override
    public void commitStep(Task task) {

    }
}