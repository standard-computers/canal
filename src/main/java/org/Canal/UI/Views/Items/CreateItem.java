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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ITS/NEW
 */
public class CreateItem extends LockeState implements Includer {

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
    private JCheckBox isBatched;
    private JCheckBox isRentable;
    private JCheckBox isSkud;
    private JCheckBox isConsumable;
    private JCheckBox isVirtual;
    private JCheckBox createBom;
    private Selectable orgIdField;
    private JTextField selectedVendor;
    private Selectable packagingUnits;
    private DesktopState desktop;
    private ArrayList<Object[]> componentsData = new ArrayList<>();
    private ArrayList<Object[]> uomsData = new ArrayList<>();
    private ArrayList<Object[]> packagingData = new ArrayList<>();
    private CustomTable bomsView;
    private CustomTable uomsView;
    private CustomTable packagingView;
    private JScrollPane scrollPane;

    public CreateItem(DesktopState desktop, RefreshListener refreshListener) {

        super("Create an Item", "/ITS/NEW", true, true, true, true);
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
        setMaximized(true);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));

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
                    if (componentsData.isEmpty()) {
                        addToQueue(new String[]{"CRITICAL", "BoM creation selected but no components!!!"});
                    }
                }

                double estPrice = 0.0;
                for (Object[] o : componentsData) {
                    System.out.println(o[4].toString());
                    estPrice += Double.parseDouble(o[4].toString());
                }
                if (estPrice > Double.parseDouble(itemPriceField.getText())) {
                    addToQueue(new String[]{"WARNING", "Component price is more than item price. Suggested prices is " + estPrice});
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
            for (Object[] o : componentsData) {
                System.out.println(o[4].toString());
                estPrice += (Double.parseDouble(o[2].toString()) * Double.parseDouble(o[4].toString()));
            }
            itemPriceField.setText(Double.toString(estPrice));
        });
        toolbar.add(autoprice);
        toolbar.add(Box.createHorizontalStrut(5));

        if ((boolean) Engine.codex.getValue("ITS", "import_enabled")) {
            IconButton importDetails = new IconButton("Import", "export", "");
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
        toolbar.setBorder(new EmptyBorder(5, 5, 5, 5));

        return toolbar;
    }

    protected void createItem() {

        Item item = new Item();
        item.setId(itemIdField.getText());
        item.setOrg(orgIdField.getSelectedValue());
        item.setName(itemNameField.getText());
        item.setLink(itemLinkField.getText());
        item.setUpc(upc.getText());
        item.setVendorNumber(vendorNumber.getText());
        item.setBaseQuantity(Double.parseDouble(baseQtyField.getText().trim()));
        item.setPackagingUnit(packagingUnits.getSelectedValue());
        item.setVendor(selectedVendor.getText());
        item.setColor(itemColorField.getText());
        item.setBatched(isBatched.isSelected());
        item.setRentable(isRentable.isSelected());
        item.setSkud(isSkud.isSelected());
        item.setConsumable(isConsumable.isSelected());
        item.setVirtual(isVirtual.isSelected());
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
        for (Object[] o : componentsData) {

            double quantity = Double.parseDouble(o[2].toString());
            StockLine sl = new StockLine();
            sl.setItem(o[0].toString());
            sl.setName(o[1].toString());
            sl.setQuantity(quantity);
            sl.setUnitOfMeasure(o[3].toString());

            double value = Double.parseDouble(o[4].toString());
            sl.setValue(value);
            item.addComponent(sl);
        }
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
        orgIdField = Selectables.organizations();
        itemNameField = Elements.input();
        itemLinkField = Elements.input();
        selectedVendor = Elements.input();
        itemPriceField = Elements.input("1.00");
        upc = Elements.input();
        vendorNumber = Elements.input();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("*New Item ID", UIManager.getColor("Label.foreground")), itemIdField);
        form.addInput(Elements.coloredLabel("Organization", UIManager.getColor("Label.foreground")), orgIdField);
        form.addInput(Elements.coloredLabel("Name", Constants.colors[0]), itemNameField);
        form.addInput(Elements.coloredLabel("Photo", Constants.colors[1]), selectPhoto);
        form.addInput(Elements.coloredLabel("Link", Constants.colors[2]), itemLinkField);
        form.addInput(Elements.coloredLabel("Vendor", Constants.colors[3]), selectedVendor);
        form.addInput(Elements.coloredLabel("Price", Constants.colors[4]), itemPriceField);
        form.addInput(Elements.coloredLabel("UPC", Constants.colors[5]), upc);
        form.addInput(Elements.coloredLabel("Vendor Number", Constants.colors[6]), vendorNumber);
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

        Form form = new Form();
        form.addInput(Elements.coloredLabel("SKU'd Product", Constants.colors[10]), isSkud);
        form.addInput(Elements.coloredLabel("Batched", Constants.colors[9]), isBatched);
        form.addInput(Elements.coloredLabel("Rentable", Constants.colors[8]), isRentable);
        form.addInput(Elements.coloredLabel("Virtual", Constants.colors[7]), isVirtual);
        form.addInput(Elements.coloredLabel("Consumable", Constants.colors[6]), isConsumable);
        controls.add(form);

        return controls;
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));

        baseQtyField = Elements.input("1");

        packagingUnits = Selectables.packagingUoms();

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
        form.addInput(Elements.coloredLabel("Packaging Base Quantity", Constants.colors[1]), baseQtyField);
        form.addInput(Elements.coloredLabel("Packaging UOM", Constants.colors[2]), packagingUnits);
        form.addInput(Elements.coloredLabel("Color", Constants.colors[3]), itemColorField);
        form.addInput(Elements.coloredLabel("Width", Constants.colors[4]), itemWidth);
        form.addInput(Elements.coloredLabel("Length", Constants.colors[5]), itemLength);
        form.addInput(Elements.coloredLabel("Height", Constants.colors[6]), itemHeight);
        form.addInput(Elements.coloredLabel("Weight", Constants.colors[7]), itemWeight);
        form.addInput(Elements.coloredLabel("Tax (0.05 as 5%)", Constants.colors[8]), tax);
        form.addInput(Elements.coloredLabel("Excise Tax (0.05 as 5%)", Constants.colors[9]), exciseTax);
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

    private JPanel billOfMaterials() {

        JPanel bom = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton addComponents = new IconButton("Add", "add_rows", "Add Component");
        addComponents.addActionListener(_ -> desktop.put(new CreateInclusion(CreateItem.this)));
        tb.add(addComponents);
        tb.add(Box.createHorizontalStrut(5));

        IconButton removeUoM = new IconButton("Remove Selected", "delete_rows", "Remove Selected Component");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeUoM);

        createBom = new JCheckBox("Create separate BoM");
        createBom.setToolTipText("Create a separate Objex as /BOMS");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createBom);

        bom.add(tb, BorderLayout.NORTH);
        bomsView = new CustomTable(new String[]{
                "Item ID",
                "Item Name",
                "Req Qty",
                "Qty UOM",
                "Price",
                "Total"
        }, componentsData);
        scrollPane = new JScrollPane(bomsView);
        bom.add(scrollPane, BorderLayout.CENTER);
        return bom;
    }

    @Override
    public void commitInclusion(Item item, double qty, String uom) {

        componentsData.add(new Object[]{
                item.getId(),
                item.getName(),
                qty,
                uom,
                item.getPrice(),
                (qty * item.getPrice())
        });

        CustomTable newBoms = new CustomTable(new String[]{
                "Item ID",
                "Item Name",
                "Req Qty",
                "Qty UOM",
                "Price",
                "Total"
        }, componentsData);
        JScrollPane scrollPane = (JScrollPane) bomsView.getParent().getParent();
        scrollPane.setViewportView(newBoms);
        bomsView = newBoms;
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    @Override
    public void commitUoM(String uom, double qty, String baseQtyUom) {

        uomsData.add(new Object[]{1, uom, qty, baseQtyUom});
        CustomTable newUoms = new CustomTable(new String[]{
                "Qty",
                "UOM",
                "Base Qty",
                "Base Qty UOM"
        }, uomsData);
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