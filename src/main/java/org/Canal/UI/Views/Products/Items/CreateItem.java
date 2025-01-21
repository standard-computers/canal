package org.Canal.UI.Views.Products.Items;

import org.Canal.Models.SupplyChainUnits.OrderLineItem;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Products.CreateInclusion;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
    private ArrayList<Object[]> components = new ArrayList<>();

    public CreateItem(DesktopState desktop){

        super("Item Builder", "/ITS/NEW", true, true, true, true);
        setFrameIcon(new ImageIcon(CreateItem.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        this.desktop = desktop;

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Units of Measure", unitsOfMeasure());
        tabs.addTab("Bill of Materials", billOfMaterials());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(Elements.header("Item Builder", SwingConstants.LEFT), BorderLayout.CENTER);
        topBar.add(actionsBar(), BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JScrollPane actionsBar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton review = new IconButton("Review", "review", "Review for warnings or potential errors");
        IconButton addPriceVariance = new IconButton("+ Price Variant", "autoprice", "");
        IconButton autoprice = new IconButton("Autoprice", "autoprice", "");
        IconButton importDetails = new IconButton("Import", "export", "");
        IconButton saveitem = new IconButton("Create Item", "execute", "");
        review.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(Double.parseDouble(itemPriceField.getText()) <= 1){
                    addToQueue(new String[]{"WARNING", "Item price is less than or equal to 1, are you sure you can make money off that?"});
                }
                desktop.put(new LockeMessages(getQueue()));
                purgeQueue();
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
                    createItem();
                }
            }
        });
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoprice);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addPriceVariance);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importDetails);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(saveitem);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));
        return Elements.scrollPane(tb);
    }

    protected void createItem() {

        OrderLineItem item = new OrderLineItem();
        item.setId(itemIdField.getText());
        item.setOrg(orgIdField.getSelectedValue());
        item.setName(itemNameField.getText());
        item.setUpc(upc.getText());
        item.setBaseQuantity(Double.parseDouble(baseQtyField.getText().trim()));
        item.setPackagingUnit(packagingUnits.getSelectedValue());
        item.setVendor(selectedVendor.getSelectedValue());
        item.setColor(itemColor.getText());
        item.setBatched(isBatched.isSelected());
        item.setRentable(isRentable.isSelected());
        item.setSkud(isSkud.isSelected());
        item.setConsumable(isConsumable.isSelected());
        item.setPrice(Double.parseDouble(itemPriceField.getText()));
        item.setWidth(Double.parseDouble(itemWidth.getValue()));
        item.setWidthUOM(itemWidth.getUOM());
        item.setLength(Double.parseDouble(itemLength.getValue()));
        item.setLengthUOM(itemLength.getUOM());
        item.setHeight(Double.parseDouble(itemHeight.getValue()));
        item.setHeightUOM(itemHeight.getUOM());
        item.setWeight(Double.parseDouble(itemWeight.getValue()));
        item.setWeightUOM(itemWeight.getUOM());
        item.setTax(Double.parseDouble(tax.getText()));
        item.setExciseTax(Double.parseDouble(exciseTax.getText()));
        Pipe.save("/ITS", item);
        dispose();
        JOptionPane.showMessageDialog(this, "Item has been created");
        desktop.put(new ViewItem(item));
    }

    private JPanel general(){

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f1 = new Form();
        JButton selectPhoto = Elements.button("Select Photo");
        selectedVendor = Selectables.vendors();
        itemIdField = Elements.input(((String) Engine.codex("ITS", "prefix")) + (1000 + (Engine.products.getItems().size() + 1)));
        orgIdField = Selectables.organizations();
        itemNameField = Elements.input("Black Shirt");
        itemPriceField = Elements.input("1.00");
        isBatched = new JCheckBox(" Item expires");
        isRentable = new JCheckBox(" Item can be rented");
        isSkud = new JCheckBox(" Item has unique SKU");
        upc = Elements.input();
        f1.addInput(Elements.coloredLabel("*New Item ID", UIManager.getColor("Label.foreground")), itemIdField);
        f1.addInput(Elements.coloredLabel("Organization", UIManager.getColor("Label.foreground")), orgIdField);
        f1.addInput(Elements.coloredLabel("Item Photo", Constants.colors[0]), selectPhoto);
        f1.addInput(Elements.coloredLabel("Item Name", Constants.colors[1]), itemNameField);
        f1.addInput(Elements.coloredLabel("Vendor", Constants.colors[2]), selectedVendor);
        f1.addInput(Elements.coloredLabel("Batched", Constants.colors[3]), isBatched);
        f1.addInput(Elements.coloredLabel("Rentable", Constants.colors[4]), isRentable);
        f1.addInput(Elements.coloredLabel("Price", Constants.colors[5]), itemPriceField);
        f1.addInput(Elements.coloredLabel("SKU'd Product", Constants.colors[6]), isSkud);
        f1.addInput(Elements.coloredLabel("UPC", Constants.colors[7]), upc);
        general.add(f1);
        return general;
    }

    private JPanel dimensional(){

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        f2.addInput(Elements.coloredLabel("Packaging Base Quantity", Constants.colors[10]), baseQtyField);
        f2.addInput(Elements.coloredLabel("Packaging UOM", Constants.colors[9]), packagingUnits);
        f2.addInput(Elements.coloredLabel("Consumable?", Constants.colors[8]), isConsumable);
        f2.addInput(Elements.coloredLabel("Color", Constants.colors[7]), itemColor);
        f2.addInput(Elements.coloredLabel("Width", Constants.colors[6]), itemWidth);
        f2.addInput(Elements.coloredLabel("Length", Constants.colors[5]), itemLength);
        f2.addInput(Elements.coloredLabel("Height", Constants.colors[4]), itemHeight);
        f2.addInput(Elements.coloredLabel("Weight", Constants.colors[3]), itemWeight);
        f2.addInput(Elements.coloredLabel("Tax (0.05 as 5%)", Constants.colors[2]), tax);
        f2.addInput(Elements.coloredLabel("Excise Tax (0.05 as 5%)", Constants.colors[1]), exciseTax);
        dimensional.add(f2);
        return dimensional;
    }

    private JPanel unitsOfMeasure(){
        JPanel unitsOfMeasure = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return unitsOfMeasure;
    }

    private JPanel billOfMaterials(){

        JPanel bom = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton materials = new IconButton("+ Materials", "materials", "Add an area cost center");
        IconButton addComponents = new IconButton("+ Components", "components", "Print labels for properties");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(materials);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addComponents);
        materials.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateInclusion("Include Material"));
            }
        });
        addComponents.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateInclusion("Include Component"));
            }
        });
        bom.add(tb, BorderLayout.NORTH);

        CustomTable boms = new CustomTable(new String[]{
                "Item ID",
                "Item Name",
                "Req Qty",
                "Qty UOM",
        }, components);
        bom.add(new JScrollPane(boms), BorderLayout.CENTER);
        return bom;
    }

    @Override
    public void commitInclusion(String component, String use, String uom) {

    }
}