package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /ITS/NEW
 */
public class CreateItem extends LockeState implements Includer {

    private JTextField itemIdField;
    private JTextField itemNameField;
    private JTextField itemPriceField;
    private JTextField itemColor;
    private JTextField tax;
    private JTextField exciseTax;
    private JTextField upc;
    private JTextField baseQtyField;
    private UOMField itemWidth, itemLength, itemHeight, itemWeight;
    private JCheckBox isBatched, isRentable, isSkud, isConsumable;
    private Selectable orgIdField, selectedVendor, packagingUnits;
    private DesktopState desktop;

    public CreateItem(DesktopState desktop){
        super("Item Builder", "/ITS/NEW", false, true, false, true);
        this.desktop = desktop;
        Constants.checkLocke(this, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        Form f1 = new Form();
        Form f2 = new Form();
        selectedVendor = Selectables.vendors();
        packagingUnits = Selectables.packagingUoms();
        itemIdField = Elements.input("X0" + (1000 + (Engine.getItems().size() + 1)));
        orgIdField = Selectables.organizations();
        itemNameField = Elements.input("Black Shirt");
        itemPriceField = Elements.input("1.00");
        isBatched = new JCheckBox(" Item expires");
        isRentable = new JCheckBox(" Item can be rented");
        isSkud = new JCheckBox(" Item has unique SKU");
        upc = Elements.input();
        baseQtyField = Elements.input();
        f1.addInput(new Label("*New Item ID", new Color(240, 240, 240)), itemIdField);
        f1.addInput(new Label("*Organization", new Color(240, 240, 240)), orgIdField);
        f1.addInput(new Label("Item Name", Constants.colors[0]), itemNameField);
        f1.addInput(new Label("Vendor", Constants.colors[1]), selectedVendor);
        f1.addInput(new Label("Packaging UOM Qty.", Constants.colors[2]), baseQtyField);
        f1.addInput(new Label("Packaging UOM", Constants.colors[3]), packagingUnits);
        f1.addInput(new Label("Batched", Constants.colors[4]), isBatched);
        f1.addInput(new Label("Rentable", Constants.colors[5]), isRentable);
        f1.addInput(new Label("Price", Constants.colors[6]), itemPriceField);
        f1.addInput(new Label("SKU'd Product", Constants.colors[7]), isSkud);
        f1.addInput(new Label("UPC", Constants.colors[8]), upc);
        itemWidth = new UOMField();
        itemLength = new UOMField();
        itemHeight = new UOMField();
        itemWeight = new UOMField();
        tax = Elements.input("0");
        exciseTax = Elements.input("0");
        itemColor = Elements.input("Black");
        isConsumable = new JCheckBox();
        f2.addInput(new Label("Consumable?", UIManager.getColor("Label.foreground")), isConsumable);
        f2.addInput(new Label("Color", UIManager.getColor("Label.foreground")), itemColor);
        f2.addInput(new Label("Width", UIManager.getColor("Label.foreground")), itemWidth);
        f2.addInput(new Label("Length", UIManager.getColor("Label.foreground")), itemLength);
        f2.addInput(new Label("Height", UIManager.getColor("Label.foreground")), itemHeight);
        f2.addInput(new Label("Weight", UIManager.getColor("Label.foreground")), itemWeight);
        f2.addInput(new Label("Tax", UIManager.getColor("Label.foreground")), tax);
        f2.addInput(new Label("Excise Tax", UIManager.getColor("Label.foreground")), exciseTax);
        JPanel biPanel = new JPanel(new GridLayout(1, 2));
        f1.setBorder(new EmptyBorder(5, 5, 5, 5));
        f2.setBorder(new EmptyBorder(5, 5, 5, 5));
        biPanel.add(f1);
        biPanel.add(f2);
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(Elements.header("Item Builder", SwingConstants.LEFT), BorderLayout.CENTER);
        topBar.add(modifiers(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(biPanel, BorderLayout.CENTER);
        add(actionsBar(), BorderLayout.SOUTH);
    }

    private JPanel modifiers(){
        JPanel tb = new JPanel(new BorderLayout());
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        JLabel materialModifer = Elements.link("Materials (0)", "Add/Remove Materials");
        JLabel componentModifier = Elements.link("Components (0)", "Add/Remove Components");
        JLabel deviations = Elements.link("Deviations (0)", "Add/Remove Deviations");
        JLabel priceVariants = Elements.link("Price Variants (0)", "Add/Remove Price Variance");
        tb.add(materialModifer);
        tb.add(componentModifier);
        tb.add(deviations);
        tb.add(priceVariants);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        materialModifer.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            }
        });
        componentModifier.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            }
        });
        deviations.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            }
        });
        priceVariants.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            }
        });
        return tb;
    }

    private JPanel actionsBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton materials = new IconButton("+ Materials", "areas", "Add an area cost center");
        IconButton components = new IconButton("+ Components", "component", "Print labels for properties");
        IconButton addPriceVariance = new IconButton("+ Price Var.", "autoprice", "");
        IconButton autoprice = new IconButton("Autoprice", "autoprice", "");
        IconButton generateUPC = new IconButton("Make UPC", "label", "");
        IconButton saveitem = new IconButton("Create Item", "start", "");
        materials.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateInclusion("Include Material"));
            }
        });
        components.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateInclusion("Include Component"));
            }
        });
        addPriceVariance.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateInclusion("Add Price Variant"));
            }
        });
        saveitem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                commitItem();
            }
        });
        tb.add(materials);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(components);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoprice);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addPriceVariance);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(generateUPC);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(saveitem);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    protected void commitItem() {
        Item ni = new Item();
        ni.setId(itemIdField.getText());
        ni.setOrg(orgIdField.getSelectedValue());
        ni.setName(itemNameField.getText());
        ni.setUpc(upc.getText());
        ni.setBaseQuantity(Double.parseDouble(baseQtyField.getText().trim()));
        ni.setPackagingUnit(packagingUnits.getSelectedValue());
        ni.setVendor(selectedVendor.getSelectedValue());
        ni.setColor(itemColor.getText());
        ni.setBatched(isBatched.isSelected());
        ni.setRentable(isRentable.isSelected());
        ni.setSkud(isSkud.isSelected());
        ni.setConsumable(isConsumable.isSelected());
        ni.setPrice(Double.parseDouble(itemPriceField.getText()));
        ni.setWidth(Double.parseDouble(itemWidth.getValue()));
        ni.setWidthUOM(itemWidth.getUOM());
        ni.setLength(Double.parseDouble(itemLength.getValue()));
        ni.setLengthUOM(itemLength.getUOM());
        ni.setHeight(Double.parseDouble(itemHeight.getValue()));
        ni.setHeightUOM(itemHeight.getUOM());
        ni.setWeight(Double.parseDouble(itemWeight.getValue()));
        ni.setWeightUOM(itemWeight.getUOM());
        ni.setTax(Double.parseDouble(tax.getText()));
        ni.setExciseTax(Double.parseDouble(exciseTax.getText()));
        Pipe.save("/ITS", ni);
        dispose();
        JOptionPane.showMessageDialog(this, "Item has been created");
    }

    @Override
    public void commitInclusion(String component, String use, String uom) {

    }
}