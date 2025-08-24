package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;

/**
 * /ITS/MOD/$[ITEM_ID]
 */
public class ModifyItem extends LockeState {

    private Item item;
    private RefreshListener refreshListener;
    private JTextField  idField;
    private JTextField orgField;
    private JTextField nameField;
    private JTextField linkField;
    private JTextField vendorField;
    private JTextField colorField;
    private JCheckBox batchedField;
    private JCheckBox rentableField;
    private JCheckBox skudField;
    private JCheckBox consumableField;
    private JCheckBox virtualField;
    private JTextField upcField;
    private JTextField vendorNumberField;
    private JTextField priceField;
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;
    private JTextField taxField;
    private JTextField exciseTaxfield;
    private JTextField baseQuantityField;

    public ModifyItem(Item item, RefreshListener refreshListener) {

        super("Modify Item / " + item.getId() + " - " + item.getName(), "/ITS/" + item.getId(), true, true, true, true);
        setFrameIcon(new ImageIcon(ModifyItem.class.getResource("/icons/modify.png")));
        this.item = item;
        this.refreshListener = refreshListener;

        setLayout(new BorderLayout());
        JPanel iic = new JPanel(new BorderLayout());
        iic.add(Elements.header(item.getId() + " – " + item.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        iic.add(toolbar(), BorderLayout.SOUTH);
        add(iic, BorderLayout.NORTH);

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", itemInfo());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Bill of Materials", billOfMaterials());
        tabs.addTab("Units of Measure", unitsOfMeasure());
        tabs.addTab("Packaging", packaging());

        add(tabs, BorderLayout.CENTER);
        setMaximized(true);
    }

    private JPanel dimensional(){

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f2 = new Form();

        baseQuantityField = Elements.input(String.valueOf(item.getBaseQuantity()));

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
        f2.addInput(Elements.coloredLabel("Packaging UOM", UIManager.getColor("Label.foreground")), Elements.input(item.getPackagingUnit()));
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
        Form f = new Form();

        idField = new Copiable(item.getId());
        orgField = Elements.input(item.getOrg());
        nameField = Elements.input(item.getName());
        linkField = Elements.input(item.getLink());
        vendorField = Elements.input(item.getVendor());

        batchedField = new JCheckBox();
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

        f.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), idField);
        f.addInput(Elements.coloredLabel("Organization", UIManager.getColor("Label.foreground")), orgField);
        f.addInput(Elements.coloredLabel("Name", UIManager.getColor("Label.foreground")), nameField);
        f.addInput(Elements.coloredLabel("Link", UIManager.getColor("Label.foreground")), linkField);
        f.addInput(Elements.coloredLabel("Vendor", UIManager.getColor("Label.foreground")), vendorField);
        f.addInput(Elements.coloredLabel("Batched", UIManager.getColor("Label.foreground")), batchedField);
        f.addInput(Elements.coloredLabel("Rentable", UIManager.getColor("Label.foreground")), rentableField);
        f.addInput(Elements.coloredLabel("SKU'd", UIManager.getColor("Label.foreground")), skudField);
        f.addInput(Elements.coloredLabel("Price", UIManager.getColor("Label.foreground")), priceField);
        f.addInput(Elements.coloredLabel("UPC", UIManager.getColor("Label.foreground")), upcField);
        f.addInput(Elements.coloredLabel("Vendor Number", UIManager.getColor("Label.foreground")), vendorNumberField);
        itemInfo.add(f);
        return itemInfo;
    }

    private JPanel toolbar() {
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

        IconButton label = new IconButton("Label", "label", "Print labels for properties");
        label.addActionListener(_ -> {

        });
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));

        if((boolean) Engine.codex.getValue("ITS", "allow_archival")){
            IconButton archive = new IconButton("Archive", "archive", "Archive item");
            archive.addActionListener(_ -> {

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
        }

        return tb;
    }
}