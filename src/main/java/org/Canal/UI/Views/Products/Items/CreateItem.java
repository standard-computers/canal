package org.Canal.UI.Views.Products.Items;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.SupplyChainUnits.Item;
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
    private JCheckBox createBom;
    private JCheckBox allowProduction;
    private Selectable orgIdField;
    private Selectable selectedVendor;
    private Selectable packagingUnits;
    private DesktopState desktop;
    private ArrayList<Object[]> components = new ArrayList<>();
    private CustomTable boms;
    private JPanel bomPanel;
    private JScrollPane scrollPane;

    public CreateItem(DesktopState desktop){

        super("Item Builder", "/ITS/NEW", true, true, true, true);
        setFrameIcon(new ImageIcon(CreateItem.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        this.desktop = desktop;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Units of Measure", unitsOfMeasure());
        bomPanel = billOfMaterials();
        tabs.addTab("Bill of Materials", bomPanel);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(Elements.header("Create New Item", SwingConstants.LEFT), BorderLayout.CENTER);
        topBar.add(actionsBar(), BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel actionsBar() {

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        IconButton copy = new IconButton("Copy From", "open", "Copy from Component");
        IconButton review = new IconButton("Review", "review", "Review for warnings or potential errors");
        IconButton addVariant = new IconButton("+ Variant", "autoprice", "");
        IconButton autoprice = new IconButton("Autoprice", "autoprice", "");
        IconButton importDetails = new IconButton("Import", "export", "");
        IconButton create = new IconButton("Create", "execute", "");
        copy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String itemId = JOptionPane.showInputDialog(CreateItem.this, "Item ID of Item to copy");
            }
        });
        review.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(Double.parseDouble(itemPriceField.getText()) <= 1){
                    addToQueue(new String[]{"WARNING", "Item price is less than or equal to 1."});
                }
                double estPrice = 0.0;
                for(Object[] o : components){
                    System.out.println(o[4].toString());
                    estPrice += Double.parseDouble(o[4].toString());
                }
                if(estPrice > Double.parseDouble(itemPriceField.getText())){
                    addToQueue(new String[]{"WARNING", "Component price is more than item price. Suggested prices is " + estPrice });
                }
                desktop.put(new LockeMessages(getQueue()));
                purgeQueue();
            }
        });
        autoprice.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                double estPrice = 0.0;
                for(Object[] o : components){
                    System.out.println(o[4].toString());
                    estPrice += Double.parseDouble(o[4].toString());
                }
                itemPriceField.setText(Double.toString(estPrice));
            }
        });
        addVariant.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateInclusion("Add Price Variant", CreateItem.this));
            }
        });
        create.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int ccc = JOptionPane.showConfirmDialog(null, "Confirm item creation and ensure all information is correct.", "Confirm?", JOptionPane.YES_NO_OPTION);
                if(ccc == JOptionPane.YES_OPTION) {
                    createItem();
                }
            }
        });
        toolbar.add(copy);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(review);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(autoprice);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(addVariant);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(importDetails);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(create);
        toolbar.setBorder(new EmptyBorder(5, 5, 5, 5));
        return toolbar;
    }

    protected void createItem() {

        Item item = new Item();
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
        for(Object[] o : components){
            OrderLineItem ol = new OrderLineItem(o[0].toString(), o[1].toString(), Double.parseDouble(o[2].toString()), Double.parseDouble(o[2].toString()));
            item.addComponent(ol);
        }
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
        baseQtyField = Elements.input("1");
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

    private JPanel billOfMaterials() {
        JPanel bom = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton materials = new IconButton("+ Materials", "materials", "Add an area cost center");
        IconButton addComponents = new IconButton("+ Components", "components", "Print labels for properties");
        createBom = new JCheckBox("Create BoM");
        allowProduction = new JCheckBox("Allow Production");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(materials);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addComponents);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createBom);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(allowProduction);
        materials.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateInclusion("Include Material", CreateItem.this));
            }
        });
        addComponents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateInclusion("Include Component", CreateItem.this));
            }
        });
        bom.add(tb, BorderLayout.NORTH);
        boms = new CustomTable(new String[]{
                "Item ID", "Item Name", "Req Qty", "Qty UOM", "Price",
        }, components);
        scrollPane = new JScrollPane(boms);
        bom.add(new JScrollPane(scrollPane), BorderLayout.CENTER);
        return bom;
    }

    @Override
    public void commitInclusion(Item item, double qty, String uom) {

        components.add(new Object[]{
                item.getId(),
                item.getName(),
                qty,
                uom,
                item.getPrice(),
        });
        CustomTable newBoms = new CustomTable(new String[]{
                "Item ID", "Item Name", "Req Qty", "Qty UOM", "Price",
        }, components);
        JScrollPane scrollPane = (JScrollPane) boms.getParent().getParent();
        scrollPane.setViewportView(newBoms);
        boms = newBoms;
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}