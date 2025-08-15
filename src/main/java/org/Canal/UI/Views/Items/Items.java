package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ITS
 */
public class Items extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Items(DesktopState desktop) {

        super("Items", "/ITS", true, true, true, true);
        setFrameIcon(new ImageIcon(Items.class.getResource("/icons/items.png")));
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1300, 600));
        holder.add(Elements.header("Items", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue("ITS", "import_enabled")) {
            IconButton importItems = new IconButton("Import", "export", "Import from CSV", "");
            importItems.addActionListener(e -> {

            });
            tb.add(importItems);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("ITS", "export_enabled")) {
            IconButton export = new IconButton("Export", "export", "Export as CSV", "");
            export.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    table.exportToCSV();
                }
            });
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton open = new IconButton("Open", "open", "Open an Item", "/ITS/O");
        open.addActionListener(e -> {
            desktop.put(Engine.router("/ITS/O", desktop));
        });
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("New", "create", "Create an Item", "/ITS/NEW");
        create.addActionListener(e -> {
            desktop.put(new CreateItem(desktop, this));
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Values", "/ITS/F");
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "label", "Print barcode for an Area");
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selected");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(e -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    private CustomTable table() {

        String[] columns = new String[]{
            "ID",
            "Name",
            "Org",
            "Vendor",
            "Vendor Name",
            "Color",
            "UPC",
            "Vendor Number",
            "Base Qty",
            "Pack. Unit",
            "Batched",
            "Rentable",
            "SKU'd",
            "Consumable",
            "Price",
            "Components",
            "Width",
            "wUOM",
            "Length",
            "lUOM",
            "Height",
            "hUOM",
            "Volume",
            "vUOM",
            "Sur. Area",
            "saUOM",
            "Weight",
            "wtUOM",
            "Tax",
            "Excise Tax"
        };
        ArrayList<Object[]> d = new ArrayList<>();
        for (Item item : Engine.products.getItems()) {
            Location vendor = Engine.getLocation(item.getVendor(), "VEND");
            d.add(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getOrg(),
                    item.getVendor(),
                    vendor.getName(),
                    item.getColor(),
                    item.getUpc(),
                    item.getVendorNumber(),
                    item.getBaseQuantity(),
                    item.getPackagingUnit(),
                    item.isBatched(),
                    item.isRentable(),
                    item.isSkud(),
                    item.isConsumable(),
                    item.getPrice(),
                    item.getComponents().size(),
                    item.getWidth(),
                    item.getWidthUOM(),
                    item.getLength(),
                    item.getLengthUOM(),
                    item.getHeight(),
                    item.getHeightUOM(),
                    item.getVolume(),
                    item.getVolumeUOM(),
                    item.getSurfaceArea(),
                    item.getSurfaceAreaUOM(),
                    item.getWeight(),
                    item.getWeightUOM(),
                    item.getTax(),
                    item.getExciseTax()
            });
        }
        CustomTable ct = new CustomTable(columns, d);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int row = t.getSelectedRow();
                    if (row != -1) {
                        String v = String.valueOf(t.getValueAt(row, 1));
                        desktop.put(new ViewItem(Engine.products.getItem(v), desktop, Items.this));
                    }
                }
            }
        });
        return ct;
    }

    @Override
    public void refresh() {
        CustomTable newTable = table();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}