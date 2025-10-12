package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Bins.ViewBin;
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

        if((boolean) Engine.codex.getValue("ITS", "start_maximized")){
            setMaximized(true);
        }
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

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Vendor ID", UIManager.getColor("Label.foreground")), new Copiable(vendor.getId()));
        form.addInput(Elements.coloredLabel("Vendor Name", UIManager.getColor("Label.foreground")), new Copiable(vendor.getName()));
        form.addInput(Elements.coloredLabel("Street", UIManager.getColor("Label.foreground")), new Copiable(vendor.getLine1()));
        form.addInput(Elements.coloredLabel("City", UIManager.getColor("Label.foreground")), new Copiable(vendor.getCity()));
        form.addInput(Elements.coloredLabel("State", UIManager.getColor("Label.foreground")), new Copiable(vendor.getState()));
        form.addInput(Elements.coloredLabel("Postal", UIManager.getColor("Label.foreground")), new Copiable(vendor.getPostal()));
        form.addInput(Elements.coloredLabel("Country", UIManager.getColor("Label.foreground")), new Copiable(vendor.getCountry()));
        form.addInput(Elements.coloredLabel("Tax Exempt", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(vendor.isTaxExempt())));
        form.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(vendor.getStatus())));
        p.add(form);

        return p;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.coloredLabel("SKU'd", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.isSkud())));
        form.addInput(Elements.coloredLabel("Batched", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.isBatched())));
        form.addInput(Elements.coloredLabel("Rentable", UIManager.getColor("Label.foreground")),new Copiable(String.valueOf(item.isRentable())));
        form.addInput(Elements.coloredLabel("Virtual", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.isVirtual())));
        form.addInput(Elements.coloredLabel("Consumable", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.isConsumable())));
        form.addInput(Elements.coloredLabel("Allow Sales", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.allowSales())));
        form.addInput(Elements.coloredLabel("Allow Purchasing", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.allowPurchasing())));
        form.addInput(Elements.coloredLabel("Keep Inventory", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.keepInventory())));
        form.addInput(Elements.coloredLabel("Lead Time", UIManager.getColor("Label.foreground")), new Copiable(item.getLeadTime() + " DAYS"));
        form.addInput(Elements.coloredLabel("Transporation Time", UIManager.getColor("Label.foreground")), new Copiable(item.getTransporationTime() + " DAYS"));
        form.addInput(Elements.coloredLabel("Manufacturing Time", UIManager.getColor("Label.foreground")), new Copiable(item.getManufacturingTime() + " DAYS"));
        controls.add(form);

        return controls;
    }

    private JPanel dimensional(){

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Packaging Base Quantity", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(item.getBaseQuantity())));
        form.addInput(Elements.coloredLabel("Packaging UOM", UIManager.getColor("Label.foreground")), new Copiable(item.getPackagingUnit()));
        form.addInput(Elements.coloredLabel("Color", UIManager.getColor("Label.foreground")), new Copiable(item.getColor()));
        form.addInput(Elements.coloredLabel("Width", UIManager.getColor("Label.foreground")), new Copiable(item.getWidth() + " " + item.getWidthUOM()));
        form.addInput(Elements.coloredLabel("Length", UIManager.getColor("Label.foreground")), new Copiable(item.getLength() + " " + item.getLengthUOM()));
        form.addInput(Elements.coloredLabel("Height", UIManager.getColor("Label.foreground")), new Copiable(item.getHeight() + " " + item.getHeightUOM()));
        form.addInput(Elements.coloredLabel("Weight", UIManager.getColor("Label.foreground")), new Copiable(item.getWeight() + " " + item.getWeightUOM()));
        form.addInput(Elements.coloredLabel("Tax (0.05 as 5%)", UIManager.getColor("Label.foreground")), new Copiable(""));
        form.addInput(Elements.coloredLabel("Excise Tax (0.05 as 5%)", UIManager.getColor("Label.foreground")), new Copiable(""));
        p.add(form);

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
        tb.add(Box.createHorizontalStrut(5));

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

                if(refreshListener != null) refreshListener.refresh();
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
                if(refreshListener != null) refreshListener.refresh();
            });
            tb.add(delete);
            tb.add(Box.createHorizontalStrut(5));
        }

        return tb;
    }
}