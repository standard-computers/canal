package org.Canal.UI.Views.Products.Items;

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
    private UOMField itemWidth;
    private UOMField itemLength;
    private UOMField itemHeight;
    private UOMField itemWeight;
    private JCheckBox isBatched;
    private JCheckBox isRentable;
    private JCheckBox isSkud;
    private JCheckBox isConsumable;
    private Selectable orgIdField;
    private Selectable selectedVendor;
    private Selectable packagingUnits;
    private DesktopState desktop;

    public CreateItem(DesktopState desktop){

        super("Item Builder", "/ITS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        this.desktop = desktop;

        Form f1 = new Form();
        JButton selectPhoto = Elements.button("Select Photo");
        selectedVendor = Selectables.vendors();
        itemIdField = Elements.input("X0" + (1000 + (Engine.getItems().size() + 1)));
        orgIdField = Selectables.organizations();
        itemNameField = Elements.input("Black Shirt");
        itemPriceField = Elements.input("1.00");
        isBatched = new JCheckBox(" Item expires");
        isRentable = new JCheckBox(" Item can be rented");
        isSkud = new JCheckBox(" Item has unique SKU");
        upc = Elements.input();
        f1.addInput(new Label("*New Item ID", new Color(240, 240, 240)), itemIdField);
        f1.addInput(new Label("*Organization", new Color(240, 240, 240)), orgIdField);
        f1.addInput(new Label("Item Name", Constants.colors[0]), selectPhoto);
        f1.addInput(new Label("Item Name", Constants.colors[1]), itemNameField);
        f1.addInput(new Label("Vendor", Constants.colors[2]), selectedVendor);
        f1.addInput(new Label("Batched", Constants.colors[3]), isBatched);
        f1.addInput(new Label("Rentable", Constants.colors[4]), isRentable);
        f1.addInput(new Label("Price", Constants.colors[5]), itemPriceField);
        f1.addInput(new Label("SKU'd Product", Constants.colors[6]), isSkud);
        f1.addInput(new Label("UPC", Constants.colors[7]), upc);
        f1.setBorder(new EmptyBorder(5, 5, 5, 5));

        Form f2 = new Form();
        baseQtyField = Elements.input();
        packagingUnits = Selectables.packagingUoms();
        itemWidth = new UOMField();
        itemLength = new UOMField();
        itemHeight = new UOMField();
        itemWeight = new UOMField();
        tax = Elements.input("0");
        exciseTax = Elements.input("0");
        itemColor = Elements.input("Black");
        isConsumable = new JCheckBox();
        f2.addInput(new Label("Packaging UOM Qty.", Constants.colors[10]), baseQtyField);
        f2.addInput(new Label("Packaging UOM", Constants.colors[9]), packagingUnits);
        f2.addInput(new Label("Consumable?", Constants.colors[8]), isConsumable);
        f2.addInput(new Label("Color", Constants.colors[7]), itemColor);
        f2.addInput(new Label("Width", Constants.colors[6]), itemWidth);
        f2.addInput(new Label("Length", Constants.colors[5]), itemLength);
        f2.addInput(new Label("Height", Constants.colors[4]), itemHeight);
        f2.addInput(new Label("Weight", Constants.colors[3]), itemWeight);
        f2.addInput(new Label("Tax (0.05 as 5%)", Constants.colors[2]), tax);
        f2.addInput(new Label("Excise Tax (0.05 as 5%)", Constants.colors[1]), exciseTax);
        f2.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel biPanel = new JPanel(new GridLayout(1, 2));
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
        IconButton review = new IconButton("Review", "review", "Review for warnings or potential errors");
        IconButton materials = new IconButton("+ Materials", "materials", "Add an area cost center");
        IconButton components = new IconButton("+ Components", "components", "Print labels for properties");
        IconButton addPriceVariance = new IconButton("+ Price Var.", "autoprice", "");
        IconButton autoprice = new IconButton("Autoprice", "autoprice", "");
        IconButton importDetails = new IconButton("Import", "import", "");
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
                int ccc = JOptionPane.showConfirmDialog(null, "Confirm item creation and ensure all information is correct.", "Confirm item creation?", JOptionPane.YES_NO_CANCEL_OPTION);
                if(ccc == JOptionPane.YES_OPTION) {
                    commitItem();
                }
            }
        });
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(materials);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(components);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoprice);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addPriceVariance);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importDetails);
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