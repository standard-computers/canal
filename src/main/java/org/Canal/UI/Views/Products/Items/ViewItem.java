package org.Canal.UI.Views.Products.Items;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * /ITS/$[ITEM_ID]
 */
public class ViewItem extends LockeState {

    private Item item;
    private Copiable idField, orgField, nameField, vendorField, colorField, batchedField, rentableField, skudField, consumableField, priceField, widthField, lengthField, heightField, weightField, taxField, exciseTaxfield;
    private Copiable vendorIdField, vendorNameField, vendorStreetField, vendorCityField, vendorStateField, vendorPostalField, vendorCountryField, vendorTaxExemptField, vendorStatusField;

    public ViewItem(Item item) {

        super("Item / " + item.getId() + " - " + item.getName(), "/ITS/$", true, true, true, true);
        setFrameIcon(new ImageIcon(ViewItem.class.getResource("/icons/items.png")));
        this.item = item;

        setLayout(new BorderLayout());
        JPanel iic = new JPanel(new BorderLayout());
        iic.add(Elements.header(item.getId() + " – " + item.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        iic.add(toolbar(), BorderLayout.SOUTH);
        add(iic, BorderLayout.NORTH);

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Vendor Information", vendorInfo());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Batch Data", batchData());
        tabs.addTab("Bill of Materials", billOfMaterials());
        tabs.addTab("Units of Measure", unitsOfMeasure());
        tabs.addTab("Packaging", packaging());
        tabs.addTab("Variants", variants());

        add(tabs, BorderLayout.CENTER);
        setMaximized(true);
    }

    private JPanel vendorInfo(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Location vendor = Engine.getLocation(item.getVendor(), "VEND");
        if (vendor == null) {
            JOptionPane.showMessageDialog(null, "A Vendor has not been allocated for this item. Please contact the data team.");
        }
        Form vi = new Form();
        vendorIdField = new Copiable(vendor.getId());
        vendorNameField = new Copiable(vendor.getName());
        vendorStreetField = new Copiable(vendor.getLine1());
        vendorCityField = new Copiable(vendor.getCity());
        vendorStateField = new Copiable(vendor.getState());
        vendorPostalField = new Copiable(vendor.getPostal());
        vendorCountryField = new Copiable(vendor.getCountry());
        vendorTaxExemptField = new Copiable(String.valueOf(vendor.isTaxExempt()));
        vendorStatusField = new Copiable(String.valueOf(vendor.getStatus()));
        vi.addInput(Elements.coloredLabel("Vendor ID", UIManager.getColor("Label.foreground")), vendorIdField);
        vi.addInput(Elements.coloredLabel("Vendor Name", UIManager.getColor("Label.foreground")), vendorNameField);
        vi.addInput(Elements.coloredLabel("Street", UIManager.getColor("Label.foreground")), vendorStreetField);
        vi.addInput(Elements.coloredLabel("City", UIManager.getColor("Label.foreground")), vendorCityField);
        vi.addInput(Elements.coloredLabel("State", UIManager.getColor("Label.foreground")), vendorStateField);
        vi.addInput(Elements.coloredLabel("Postal", UIManager.getColor("Label.foreground")), vendorPostalField);
        vi.addInput(Elements.coloredLabel("Country", UIManager.getColor("Label.foreground")), vendorCountryField);
        vi.addInput(Elements.coloredLabel("Tax Exempt", UIManager.getColor("Label.foreground")), vendorTaxExemptField);
        vi.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), vendorStatusField);
        p.add(vi);
        return p;
    }

    private JPanel dimensional(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f2 = new Form();
        f2.addInput(Elements.coloredLabel("Packaging Base Quantity", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.getBaseQuantity())));
        f2.addInput(Elements.coloredLabel("Packaging UOM", UIManager.getColor("Label.foreground")), new Copiable(item.getPackagingUnit()));
        f2.addInput(Elements.coloredLabel("Consumable?", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.isConsumable())));
        f2.addInput(Elements.coloredLabel("Color", UIManager.getColor("Label.foreground")), new Copiable(item.getColor()));
        f2.addInput(Elements.coloredLabel("Width", UIManager.getColor("Label.foreground")), new Copiable(item.getWidth() + " " + item.getWidthUOM()));
        f2.addInput(Elements.coloredLabel("Length", UIManager.getColor("Label.foreground")), new Copiable(item.getLength() + " " + item.getLengthUOM()));
        f2.addInput(Elements.coloredLabel("Height", UIManager.getColor("Label.foreground")), new Copiable(item.getHeight() + " " + item.getHeightUOM()));
        f2.addInput(Elements.coloredLabel("Weight", UIManager.getColor("Label.foreground")), new Copiable(item.getWeight() + " " + item.getWeightUOM()));
        f2.addInput(Elements.coloredLabel("Tax (0.05 as 5%)", UIManager.getColor("Label.foreground")), new Copiable(""));
        f2.addInput(Elements.coloredLabel("Excise Tax (0.05 as 5%)", UIManager.getColor("Label.foreground")), new Copiable(""));
        p.add(f2);
        return p;
    }

    private JPanel batchData(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
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

    private JPanel variants(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return p;
    }

    private JPanel itemInfo() {
        Form f = new Form();
        idField = new Copiable(item.getId());
        orgField = new Copiable(item.getOrg());
        nameField = new Copiable(item.getName());
        vendorField = new Copiable(item.getVendor());
        colorField = new Copiable(item.getColor());
        batchedField = new Copiable(String.valueOf(item.isBatched()));
        rentableField = new Copiable(String.valueOf(item.isRentable()));
        skudField = new Copiable(String.valueOf(item.isSkud()));
        consumableField = new Copiable(String.valueOf(item.isConsumable()));
        priceField = new Copiable(String.valueOf(item.getPrice()));
        widthField = new Copiable(String.valueOf(item.getWidth()));
        lengthField = new Copiable(String.valueOf(item.getLength()));
        heightField = new Copiable(String.valueOf(item.getHeight()));
        weightField = new Copiable(String.valueOf(item.getWeight()));
        taxField = new Copiable(String.valueOf(item.getTax()));
        exciseTaxfield = new Copiable(String.valueOf(item.getExciseTax()));
        f.addInput(Elements.coloredLabel("ID", Constants.colors[0]), idField);
        f.addInput(Elements.coloredLabel("Organization", Constants.colors[1]), orgField);
        f.addInput(Elements.coloredLabel("Name", Constants.colors[2]), nameField);
        f.addInput(Elements.coloredLabel("Vendor", Constants.colors[3]), vendorField);
        f.addInput(Elements.coloredLabel("Color", Constants.colors[4]), colorField);
        f.addInput(Elements.coloredLabel("Batched", Constants.colors[5]), batchedField);
        f.addInput(Elements.coloredLabel("Rentable", Constants.colors[6]), rentableField);
        f.addInput(Elements.coloredLabel("SKU'd", Constants.colors[6]), skudField);
        f.addInput(Elements.coloredLabel("Consumable", Constants.colors[7]), consumableField);
        f.addInput(Elements.coloredLabel("Price", Constants.colors[8]), priceField);
        f.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[10]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[0]), heightField);
        f.addInput(Elements.coloredLabel("Weight", Constants.colors[1]), weightField);
        f.addInput(Elements.coloredLabel("Tax", Constants.colors[2]), taxField);
        f.addInput(Elements.coloredLabel("Excise Tax", Constants.colors[3]), exciseTaxfield);
        return f;
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton inventory = new IconButton("Inventory", "inventory", "Check stock of item");
        IconButton label = new IconButton("", "label", "Print labels for properties");
        tb.add(inventory);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        return tb;
    }
}