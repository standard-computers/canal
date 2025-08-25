package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * /ITS/MOD/$[ITEM_ID]
 */
public class ModifyItem extends LockeState {

    //Operating Objects
    private Item item;
    private RefreshListener refreshListener;

    //General Info Tab
    private JTextField  idField;
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

    public ModifyItem(Item item, RefreshListener refreshListener) {

        super("Modify Item / " + item.getId() + " - " + item.getName(), "/ITS/" + item.getId(), true, true, true, true);
        setFrameIcon(new ImageIcon(ModifyItem.class.getResource("/icons/modify.png")));
        this.item = item;
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

    private JPanel dimensional(){

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f2 = new Form();

        baseQuantityField = Elements.input(String.valueOf(item.getBaseQuantity()));

        packagingUomField = Selectables.packagingUoms();
        packagingUomField.setSelectedValue(item.getPackagingUnit());

        colorField = Elements.input(item.getColor());

        consumableField = new JCheckBox();
        if(item.isConsumable()){
            consumableField.setSelected(true);
        }

        virtualField = new JCheckBox("Like software");
        if(item.isVirtual()){
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

    private JPanel billOfMaterials(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return p;
    }


    private JPanel unitsOfMeasure(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return p;
    }

    private JPanel packaging(){
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

        batchedField = new JCheckBox( " Item expires");
        if(item.isBatched()){
            batchedField.setSelected(true);
        }

        rentableField = new JCheckBox(" Item can be rented");
        if(item.isRentable()){
            rentableField.setSelected(true);
        }

        skudField = new JCheckBox(" Item has unique SKU");
        if(item.isSkud()){
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
            item.setUpc(upcField.getText());
            item.setVendorNumber(vendorNumberField.getText());

            item.setBaseQuantity(Double.parseDouble(baseQuantityField.getText()));
            item.setPackagingUnit(packagingUomField.getSelectedValue());

            item.setTax(Double.parseDouble(taxField.getText()));
            item.setExciseTax(Double.parseDouble(exciseTaxfield.getText()));

            item.setWidth(Double.parseDouble(widthField.getValue()));
            item.setWidthUOM(widthField.getUOM());

            item.setLength(Double.parseDouble(lengthField.getValue()));
            item.setLengthUOM(lengthField.getUOM());

            item.setHeight(Double.parseDouble(heightField.getValue()));
            item.setHeightUOM(heightField.getUOM());

            item.setWeight(Double.parseDouble(weightField.getValue()));
            item.setWeightUOM(weightField.getUOM());

            item.save();

            if((boolean) Engine.codex.getValue("ITS", "dispose_on_save")){
                dispose();
            }

            if(refreshListener != null){
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
            @Override public void actionPerformed(ActionEvent e) {
                save.doClick();
            }
        });

        IconButton review = new IconButton("Review", "review", "Review changes");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Label", "label", "Print labels for properties");
        label.addActionListener(_ -> {

        });
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));

        if((boolean) Engine.codex.getValue("ITS", "allow_archival")){
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

        if((boolean) Engine.codex.getValue("ITS", "allow_deletion")){
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
                @Override public void actionPerformed(ActionEvent e) {
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
}