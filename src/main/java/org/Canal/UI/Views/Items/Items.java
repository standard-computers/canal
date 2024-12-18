package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.HR.Employees.EmployeeView;
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
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1300, 600));
        holder.add(Elements.header("Items", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double click
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Get the clicked row
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(new EmployeeView(Engine.getEmployee(value)));
                    }
                }
            }
        });
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton createItem = new IconButton("New", "order", "Create a Item", "/ITS/NEW");
        IconButton modifyItem = new IconButton("Modify", "modify", "Modify a Item", "/ITS/MOD");
        IconButton archiveItem = new IconButton("Archive", "archive", "Archive a Item", "/ITS/ARCHV");
        IconButton removeItem = new IconButton("Remove", "delete", "Delete a Item", "/ITS/DEL");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createItem);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyItem);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveItem);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeItem);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterValue);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        return tb;
    }

    private CustomTable createTable() {
        String[] columns = new String[]{
            "ID", "Org", "Vendor", "Vendor Name", "Name", "Color",
            "UPC", "Base Qty", "Pack. Unit", "Batched", "Rentable",
            "SKU'd", "Consumable", "Price", "Width", "Width UOM",
            "Length", "Length UOM", "Height", "Height UOM", "Weight",
            "Weight UOM", "Tax", "Excise Tax"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Item item : Engine.getItems()) {
            Location vendor = Engine.getLocation(item.getVendor(), "VEND");
            data.add(new Object[]{
                    item.getId(),
                    item.getOrg(),
                    item.getVendor(),
                    vendor.getName(),
                    item.getName(),
                    item.getColor(),
                    item.getUpc(),
                    item.getBaseQuantity(),
                    item.getPackagingUnit(),
                    item.isBatched(),
                    item.isRentable(),
                    item.isSkud(),
                    item.isConsumable(),
                    item.getPrice(),
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
        return new CustomTable(columns, data);
    }

    @Override
    public void onRefresh() {

    }
}