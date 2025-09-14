package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Bins.ViewBin;
import org.Canal.UI.Views.Products.CreateUoM;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ITS/$[ITEM_ID]
 */
public class ViewItem extends LockeState {

    private Item item;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    public ViewItem(Item item, DesktopState desktop, RefreshListener refreshListener) {

        super("Item / " + item.getId() + " - " + item.getName(), "/ITS/" + item.getId());
        setFrameIcon(new ImageIcon(ViewItem.class.getResource("/icons/items.png")));
        this.item = item;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        setLayout(new BorderLayout());
        JPanel iic = new JPanel(new BorderLayout());
        iic.add(Elements.header(item.getId() + " – " + item.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        iic.add(toolbar(), BorderLayout.SOUTH);
        add(iic, BorderLayout.NORTH);

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", info());
        tabs.addTab("Vendor", vendorInfo());
        tabs.addTab("Controls", controls());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Batch Data", batchData());
        tabs.addTab("Bill of Materials", billOfMaterials());
        tabs.addTab("Units of Measure", unitsOfMeasure());
        tabs.addTab("Packaging", packaging());

        add(tabs, BorderLayout.CENTER);
        setMaximized(true);
    }

    private JPanel info() {

        JPanel itemInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(item.getId()));
        form.addInput(Elements.coloredLabel("Organization", UIManager.getColor("Label.foreground")), new Copiable(item.getOrg()));
        form.addInput(Elements.coloredLabel("Name", UIManager.getColor("Label.foreground")), new Copiable(item.getName()));
        form.addInput(Elements.coloredLabel("Link", UIManager.getColor("Label.foreground")), new Copiable(item.getLink()));
        form.addInput(Elements.coloredLabel("Vendor", UIManager.getColor("Label.foreground")), new Copiable(item.getVendor()));
        form.addInput(Elements.coloredLabel("Price", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.getPrice())));
        form.addInput(Elements.coloredLabel("UPC", UIManager.getColor("Label.foreground")), new Copiable(item.getUpc()));
        form.addInput(Elements.coloredLabel("Vendor Number", UIManager.getColor("Label.foreground")), new Copiable(item.getVendorNumber()));
        itemInfo.add(form);

        return itemInfo;
    }

    private JPanel vendorInfo(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Location vendor = Engine.getLocation(item.getVendor(), "VEND");
        if (vendor == null) {
            JOptionPane.showMessageDialog(null, "A Vendor has not been allocated for this item. Please contact the data team.");
        }
        Form vi = new Form();

        vi.addInput(Elements.coloredLabel("Vendor ID", UIManager.getColor("Label.foreground")), new Copiable(vendor.getId()));
        vi.addInput(Elements.coloredLabel("Vendor Name", UIManager.getColor("Label.foreground")), new Copiable(vendor.getName()));
        vi.addInput(Elements.coloredLabel("Street", UIManager.getColor("Label.foreground")), new Copiable(vendor.getLine1()));
        vi.addInput(Elements.coloredLabel("City", UIManager.getColor("Label.foreground")), new Copiable(vendor.getCity()));
        vi.addInput(Elements.coloredLabel("State", UIManager.getColor("Label.foreground")), new Copiable(vendor.getState()));
        vi.addInput(Elements.coloredLabel("Postal", UIManager.getColor("Label.foreground")), new Copiable(vendor.getPostal()));
        vi.addInput(Elements.coloredLabel("Country", UIManager.getColor("Label.foreground")), new Copiable(vendor.getCountry()));
        vi.addInput(Elements.coloredLabel("Tax Exempt", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(vendor.isTaxExempt())));
        vi.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(vendor.getStatus())));
        p.add(vi);

        return p;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JCheckBox skud = new JCheckBox(" Item has unique SKU", item.isSkud());
        skud.setEnabled(false);
        JCheckBox batched = new JCheckBox(" Item Expires", item.isBatched());
        batched.setEnabled(false);
        JCheckBox rentable =  new JCheckBox(" Item can be rented", item.isRentable());
        rentable.setEnabled(false);
        JCheckBox virtual = new JCheckBox(" Item can be rented", item.isVirtual());
        virtual.setEnabled(false);
        JCheckBox consumable = new JCheckBox(" Item qty used (raw materials)", item.isConsumable());
        consumable.setEnabled(false);

        Form form = new Form();
        form.addInput(Elements.coloredLabel("SKU'd", UIManager.getColor("Label.foreground")), skud);
        form.addInput(Elements.coloredLabel("Batched", UIManager.getColor("Label.foreground")), batched);
        form.addInput(Elements.coloredLabel("Rentable", UIManager.getColor("Label.foreground")),rentable);
        form.addInput(Elements.coloredLabel("Virtual", UIManager.getColor("Label.foreground")), virtual);
        form.addInput(Elements.coloredLabel("Consumable", UIManager.getColor("Label.foreground")), consumable);
        controls.add(form);

        return controls;
    }

    private JPanel dimensional(){

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f2 = new Form();

        f2.addInput(Elements.coloredLabel("Packaging Base Quantity", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.getBaseQuantity())));
        f2.addInput(Elements.coloredLabel("Packaging UOM", UIManager.getColor("Label.foreground")), new Copiable(item.getPackagingUnit()));
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

    private JScrollPane billOfMaterials(){

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
        for(int s = 0; s < item.getComponents().size(); s++){
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
                        for (Area area : Engine.getAreas()) {
                            for (Bin bin : area.getBins()) {
                                if (v.equals(bin.getId())) {
                                    bin.setArea(area.getId());
                                    desktop.put(new ViewBin(bin, desktop, null));
                                }
                            }
                        }
                    }
                }
            }
        });

        return new JScrollPane(ct);
    }


    private JScrollPane unitsOfMeasure() {

        CustomTable uomsView = new CustomTable(new String[]{
                "Qty",
                "UOM",
                "Base Qty",
                "Base Qty UOM"
        }, item.getUoms());
        return new JScrollPane(uomsView);
    }

    private JPanel packaging(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return p;
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton inventory = new IconButton("Inventory", "inventory", "Check stock of item");
        inventory.addActionListener(_ -> {

        });
        tb.add(inventory);
        tb.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Label", "label", "Print labels for properties");
        label.addActionListener(_ -> {

        });
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));

        if((boolean) Engine.codex.getValue("ITS", "allow_modification")) {
            IconButton modify = new IconButton("Modify", "modify", "Modify item");
            modify.addActionListener(_ -> {
                dispose();
                desktop.put(new ModifyItem(item, desktop, refreshListener));
            });
            tb.add(modify);
            tb.add(Box.createHorizontalStrut(5));
            int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
            KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_E, mask);
            JRootPane rp = getRootPane();
            rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-modify");
            rp.getActionMap().put("do-modify", new AbstractAction() {
                @Override public void actionPerformed(ActionEvent e) {
                    modify.doClick();
                }
            });
        }

        if((boolean) Engine.codex.getValue("ITS", "allow_archival")){
            IconButton archive = new IconButton("Archive", "archive", "Archive item");
            archive.addActionListener(_ -> {
                item.setStatus(LockeStatus.ARCHIVED);
                item.save();
                if(refreshListener != null) {
                    refreshListener.refresh();
                }
                dispose();
            });
            tb.add(archive);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("ITS", "allow_deletion")){
            IconButton delete = new IconButton("Delete", "delete", "Delete item");
            delete.addActionListener(_ -> {
                item.setStatus(LockeStatus.DELETED);
                item.save();
                dispose();
                if(refreshListener != null) {
                    refreshListener.refresh();
                }
            });
            tb.add(delete);
            tb.add(Box.createHorizontalStrut(5));
        }

        return tb;
    }
}