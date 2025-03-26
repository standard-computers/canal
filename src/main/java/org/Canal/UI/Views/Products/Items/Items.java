package org.Canal.UI.Views.Products.Items;

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

    private CustomTable table;

    public Items(DesktopState desktop) {

        super("Items", "/ITS", true, true, true, true);
        setFrameIcon(new ImageIcon(Items.class.getResource("/icons/items.png")));

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1300, 600));
        holder.add(Elements.header("Items", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int row = t.getSelectedRow();
                    if (row != -1) {
                        String v = String.valueOf(t.getValueAt(row, 1));
                        desktop.put(new ViewItem(Engine.products.getItem(v)));
                    }
                }
            }
        });
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton importItems = new IconButton("Import", "export", "Import as CSV", "");
        IconButton createItem = new IconButton("New", "create", "Create an Item", "/ITS/NEW");
        IconButton modifyItem = new IconButton("Modify", "modify", "Modify an Item", "/ITS/MOD");
        IconButton archiveItem = new IconButton("Archive", "archive", "Archive an Item", "/ITS/ARCHV");
        IconButton findItem = new IconButton("Find", "find", "Find by Values", "/ITS/F");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importItems);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createItem);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyItem);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveItem);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(findItem);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        refresh.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                refresh();
            }
        });
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
                    item.getWeight(),
                    item.getWeightUOM(),
                    item.getTax(),
                    item.getExciseTax()
            });
        }
        return new CustomTable(columns, d);
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