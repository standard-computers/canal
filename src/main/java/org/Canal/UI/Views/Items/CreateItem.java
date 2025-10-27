package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.Models.SupplyChainUnits.Task;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Products.CreateInclusion;
import org.Canal.UI.Views.Products.CreateUoM;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ITS/NEW
 */
public class CreateItem extends LockeState implements Includer {

    private Item item = new Item();
    private RefreshListener refreshListener;
    private JTextField itemIdField;
    private JTextField itemNameField;
    private JTextField itemLinkField;
    private JTextField itemPriceField;
    private JTextField itemColorField;
    private JTextField tax;
    private JTextField exciseTax;
    private JTextField upc;
    private JTextField vendorNumber;
    private JTextField baseQtyField;
    private UOMField itemWidth;
    private UOMField itemLength;
    private UOMField itemHeight;
    private UOMField itemWeight;

    //Controls Tab
    private JCheckBox isSkud;
    private JCheckBox isBatched;
    private JCheckBox isRentable;
    private JCheckBox isConsumable;
    private JCheckBox isVirtual;
    private JCheckBox allowSales;
    private JCheckBox allowPurchasing;
    private JCheckBox keepInventory;
    private JTextField leadTime;
    private JTextField transportationTime;
    private JTextField manufacturingTime;

    private JCheckBox createBom;
    private JTextField selectedVendor;
    private Selectable packagingUnits;
    private DesktopState desktop;
    private ArrayList<Object[]> uomsData = new ArrayList<>();
    private ArrayList<Object[]> packagingData = new ArrayList<>();
    private CustomTable bomsView;
    private CustomTable uomsView;
    private CustomTable packagingView;
    private JScrollPane scrollPane;

    public CreateItem(DesktopState desktop, RefreshListener refreshListener) {

        super("Create an Item", "/ITS/NEW");
        setFrameIcon(new ImageIcon(CreateItem.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Bill of Materials", billOfMaterials());
        tabs.addTab("Units of Measure", unitsOfMeasure());
        tabs.addTab("Packaging", packaging());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(Elements.header("New Item", SwingConstants.LEFT), BorderLayout.CENTER);
        topBar.add(toolbar(), BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("ITS", "start_maximized")) {
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton copy = new IconButton("Copy From", "open", "Copy from Component");
        copy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String itemId = JOptionPane.showInputDialog(CreateItem.this, "Item ID of Item to copy");
                Item i = Engine.getItem(itemId);
                if (i != null) {

                    //Copy General Info
                    itemNameField.setText(i.getName());
                    itemLinkField.setText(i.getLink());
                    selectedVendor.setText(i.getVendor());
                    itemColorField.setText(i.getColor());
                    upc.setText(i.getUpc());
                    baseQtyField.setText(String.valueOf(i.getBaseQuantity()));
                    packagingUnits.setSelectedValue(i.getPackagingUnit());
                    itemPriceField.setText(String.valueOf(i.getPrice()));

                    //Copy Controls Selections
                    isSkud.setSelected(i.isSkud());
                    isBatched.setSelected(i.isBatched());
                    isRentable.setSelected(i.isRentable());
                    isVirtual.setSelected(i.isVirtual());
                    isConsumable.setSelected(i.isConsumable());
                    allowSales.setSelected(i.allowSales());
                    allowPurchasing.setSelected(i.allowPurchasing());
                    keepInventory.setSelected(i.keepInventory());

                    //Copy Dimensional Info
                    itemWidth.setValue(String.valueOf(i.getWidth()));
                    itemWidth.setUOM(i.getWidthUOM());
                    itemLength.setValue(String.valueOf(i.getLength()));
                    itemLength.setUOM(i.getLengthUOM());
                    itemHeight.setValue(String.valueOf(i.getHeight()));
                    itemHeight.setUOM(i.getHeightUOM());
                    itemWeight.setValue(String.valueOf(i.getWeight()));
                    itemWeight.setUOM(i.getWeightUOM());
                    tax.setText(String.valueOf(i.getTax()));
                    exciseTax.setText(String.valueOf(i.getExciseTax()));

                    item.setComponents(i.getComponents());

                    refreshComponents();

                } else {
                    JOptionPane.showMessageDialog(CreateItem.this, "Item does not exist");
                }
            }
        });
        toolbar.add(copy);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review for warnings or potential errors");
        review.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (Double.parseDouble(itemPriceField.getText()) <= 1) {
                    addToQueue(new String[]{"WARNING", "Item price is less than or equal to 1."});
                }

                if (itemNameField.getText().isEmpty()) {
                    addToQueue(new String[]{"CRITICAL", "Item NAME is blank"});
                }

                if (itemColorField.getText().isEmpty()) {
                    addToQueue(new String[]{"CRITICAL", "Item COLOR is blank"});
                }

                if (createBom.isSelected()) {
                    if (item.getComponents().isEmpty()) {
                        addToQueue(new String[]{"CRITICAL", "BoM creation selected but no components!!!"});
                    }
                }

                double estPrice = 0.0;
                for (StockLine sl : item.getComponents()) {
                    estPrice += sl.getValue();
                }
                if (estPrice > Double.parseDouble(itemPriceField.getText())) {
                    addToQueue(new String[]{"WARNING", "Component price is more than item price."});
                    addToQueue(new String[]{"WARNING", "Suggested prices is " + estPrice});
                }

                if (isRentable.isSelected() && !isSkud.isSelected()) {
                    addToQueue(new String[]{"WARNING", "Item is rentable but not SKU'd. Are you sure?"});

                }

                desktop.put(new LockeMessages(getQueue()));
                purgeQueue();
            }
        });
        toolbar.add(review);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton autoprice = new IconButton("Autoprice", "autoprice", "");
        autoprice.addActionListener(_ -> {
            double estPrice = 0.0;
            for (StockLine o : item.getComponents()) {
                estPrice += o.getValue();
            }
            itemPriceField.setText(Double.toString(estPrice));
        });
        toolbar.add(autoprice);
        toolbar.add(Box.createHorizontalStrut(5));

        if ((boolean) Engine.codex.getValue("ITS", "import_enabled")) {
            IconButton importDetails = new IconButton("Import", "import", "");
            toolbar.add(importDetails);
            toolbar.add(Box.createHorizontalStrut(5));
        }

        IconButton create = new IconButton("Create", "create", "");
        create.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int ccc = JOptionPane.showConfirmDialog(null, "Confirm item creation and ensure all information is correct.", "Confirm?", JOptionPane.YES_NO_OPTION);
                if (ccc == JOptionPane.YES_OPTION) {
                    createItem();
                }
            }
        });
        toolbar.add(create);
        toolbar.add(Box.createHorizontalStrut(5));

        return toolbar;
    }

    protected void createItem() {

        //Set General Info
        item.setId(itemIdField.getText());
        item.setName(itemNameField.getText());
        item.setLink(itemLinkField.getText());
        item.setVendorNumber(vendorNumber.getText());
        item.setPrice(Double.parseDouble(itemPriceField.getText()));
        item.setUpc(upc.getText());
        item.setVendor(selectedVendor.getText());

        //Set Controls Properties
        item.setSkud(isSkud.isSelected());
        item.setBatched(isBatched.isSelected());
        item.setRentable(isRentable.isSelected());
        item.setConsumable(isConsumable.isSelected());
        item.setVirtual(isVirtual.isSelected());
        item.allowSales(allowSales.isSelected());
        item.allowPurchasing(allowPurchasing.isSelected());
        item.keepInventory(keepInventory.isSelected());
        item.setLeadTime(Double.parseDouble(leadTime.getText()));
        item.setTransporationTime(Double.parseDouble(transportationTime.getText()));
        item.setManufacturingTime(Double.parseDouble(manufacturingTime.getText()));

        //Set Dimensional Properties
        item.setBaseQuantity(Double.parseDouble(baseQtyField.getText().trim()));
        item.setPackagingUnit(packagingUnits.getSelectedValue());
        item.setColor(itemColorField.getText());
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
        item.setUoms(uomsData);
        Pipe.save("/ITS", item);

        if ((boolean) Engine.codex.getValue("ITS", "item_created_alert")) {
            JOptionPane.showMessageDialog(this, "Item has been created");
        }
        dispose();

        if (refreshListener != null) {
            refreshListener.refresh();
        }

        if ((boolean) Engine.codex.getValue("ITS", "auto_open_new")) {
            desktop.put(new ViewItem(item, desktop, refreshListener));
        }
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton selectPhoto = Elements.button("Select Photo");

        itemIdField = Elements.input(((String) Engine.codex("ITS", "prefix")) + (1000 + (Engine.getItems().size() + 1)));
        itemNameField = Elements.input();
        itemLinkField = Elements.input();
        selectedVendor = Elements.input();
        itemPriceField = Elements.input("1.00");
        upc = Elements.input();
        vendorNumber = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Item ID"), itemIdField);
        form.addInput(Elements.inputLabel("Name"), itemNameField);
        form.addInput(Elements.inputLabel("Photo"), selectPhoto);
        form.addInput(Elements.inputLabel("Link"), itemLinkField);
        form.addInput(Elements.inputLabel("Vendor"), selectedVendor);
        form.addInput(Elements.inputLabel("Price"), itemPriceField);
        form.addInput(Elements.inputLabel("UPC"), upc);
        form.addInput(Elements.inputLabel("Vendor Number"), vendorNumber);
        general.add(form);

        return general;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        isSkud = new JCheckBox(" Item has unique SKU");
        isBatched = new JCheckBox(" Item expires");
        isRentable = new JCheckBox(" Item can be rented");
        isVirtual = new JCheckBox(" Item not physical");
        isConsumable = new JCheckBox(" Item qty used (raw materials)");
        allowSales = new JCheckBox(" Item can be sold");
        allowPurchasing = new JCheckBox(" Item can be purchased");
        keepInventory = new JCheckBox(" Keep inventory of item");
        leadTime = Elements.input("1.0");
        transportationTime = Elements.input("1.0");
        manufacturingTime = Elements.input("1.0");

        Form form = new Form();
        form.addInput(Elements.inputLabel("SKU'd Product"), isSkud);
        form.addInput(Elements.inputLabel("Batched"), isBatched);
        form.addInput(Elements.inputLabel("Rentable"), isRentable);
        form.addInput(Elements.inputLabel("Virtual"), isVirtual);
        form.addInput(Elements.inputLabel("Consumable"), isConsumable);
        form.addInput(Elements.inputLabel("Allow Sales"), allowSales);
        form.addInput(Elements.inputLabel("Allow Purchasing"), allowPurchasing);
        form.addInput(Elements.inputLabel("Keep Inventory"), keepInventory);
        form.addInput(Elements.inputLabel("Lead Time"), leadTime);
        form.addInput(Elements.inputLabel("Transportation Time"), transportationTime);
        form.addInput(Elements.inputLabel("Manufacturing Time"), manufacturingTime);
        controls.add(form);

        return controls;
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));

        baseQtyField = Elements.input("1");

        packagingUnits = Selectables.allPackagingUoms();

        itemWidth = new UOMField();
        if (Engine.codex.getValue("ITS", "default_width_uom") != null) {
            itemWidth.setUOM((String) Engine.codex.getValue("ITS", "default_width_uom"));
        }

        itemLength = new UOMField();
        if (Engine.codex.getValue("ITS", "default_length_uom") != null) {
            itemLength.setUOM((String) Engine.codex.getValue("ITS", "default_length_uom"));
        }

        itemHeight = new UOMField();
        if (Engine.codex.getValue("ITS", "default_height_uom") != null) {
            itemHeight.setUOM((String) Engine.codex.getValue("ITS", "default_height_uom"));
        }

        if (Engine.codex.getValue("/ITS", "default_width_uom") != null) {
            itemWeight = new UOMField((String) Engine.codex.getValue("/ITS", "default_width_uom"), false);
        } else {
            itemWeight = new UOMField("OZ", false);
        }

        tax = Elements.input("0.0");
        exciseTax = Elements.input("0.0");
        itemColorField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Packaging Base Quantity"), baseQtyField);
        form.addInput(Elements.inputLabel("Packaging UOM"), packagingUnits);
        form.addInput(Elements.inputLabel("Color"), itemColorField);
        form.addInput(Elements.inputLabel("Width"), itemWidth);
        form.addInput(Elements.inputLabel("Length"), itemLength);
        form.addInput(Elements.inputLabel("Height"), itemHeight);
        form.addInput(Elements.inputLabel("Weight"), itemWeight);
        form.addInput(Elements.inputLabel("Tax (0.05 as 5%)"), tax);
        form.addInput(Elements.inputLabel("Excise Tax (0.05 as 5%)"), exciseTax);
        dimensional.add(form);

        return dimensional;
    }

    private JPanel unitsOfMeasure() {

        JPanel unitsOfMeasure = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton addUoM = new IconButton("Add UoM", "add_rows", "Add Unit of Measure");
        addUoM.addActionListener(_ -> desktop.put(new CreateUoM(CreateItem.this)));
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
        }, uomsData);
        unitsOfMeasure.add(new JScrollPane(uomsView));
        return unitsOfMeasure;
    }

    private JPanel packaging() {

        JPanel packaging = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton addPackaging = new IconButton("Add", "add_rows", "Add Unit of Packaing");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addPackaging);

        IconButton removePackaging = new IconButton("Remove Selected", "delete_rows", "Remove Unit of Packing");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removePackaging);

        IconButton autoMakePackaging = new IconButton("AutoMake", "automake", "Remove Unit of Packaging");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMakePackaging);

        packaging.setLayout(new BorderLayout());
        packaging.add(tb, BorderLayout.NORTH);
        packagingView = new CustomTable(new String[]{
                "Packaging", "Base Qty", "Base Qty UOM", "Cost", "Price", "Weight", "Weight UOM"
        }, packagingData);
        packaging.add(new JScrollPane(packagingView));
        return packaging;
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
        tb.add(Box.createHorizontalStrut(5));

        IconButton addComponent = new IconButton("Add Component", "add_rows", "Add Component");
        addComponent.addActionListener(_ -> desktop.put(new CreateInclusion(CreateItem.this)));
        tb.add(addComponent);
        tb.add(Box.createHorizontalStrut(5));

        IconButton removeComponent = new IconButton("Remove Component", "delete_rows", "Remove Selected Component");
        removeComponent.addActionListener(_ -> {
            int selectedRow = bomsView.getSelectedRow();
            if (selectedRow != -1) {
                item.getComponents().remove(selectedRow);
                refreshComponents();
            }
        });
        tb.add(removeComponent);
        tb.add(Box.createHorizontalStrut(5));

        createBom = new JCheckBox("Create separate BoM");
        createBom.setToolTipText("Create a separate Objex as /BOMS");
        tb.add(createBom);
        tb.add(Box.createHorizontalStrut(5));

        bom.add(tb, BorderLayout.NORTH);
        bomsView = componentsTable();
        bom.add(new JScrollPane(bomsView), BorderLayout.CENTER);
        return bom;
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
        newComponent.setValue(item.getBasePrice());
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

        uomsData.add(new Object[]{1, uom, qty, baseQtyUom});
        refreshComponents();
    }

    @Override
    public void commitStep(Task task) {

    }
}