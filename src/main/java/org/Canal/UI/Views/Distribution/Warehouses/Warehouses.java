package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.Models.SupplyChainUnits.Warehouse;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /WHS
 * View a list of Warehouses for Selected Organization
 */
public class Warehouses extends LockeState {

    private CustomTable table;

    public Warehouses() {
        super("Warehouses", "/WHS", true, true, true, true);
        setFrameIcon(new ImageIcon(Warehouses.class.getResource("/icons/warehouses.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Warehouses", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton createWarehouse = new IconButton("New Warehouse", "order", "Create a Warehouse", "/WHS/NEW");
        IconButton modifyWarehouse = new IconButton("Modify", "modify", "Modify a Warehouse", "/WHS/MOD");
        IconButton archiveWarehouse = new IconButton("Archive", "archive", "Archive a Warehouse", "/WHS/ARCHV");
        IconButton removeWarehouse = new IconButton("Remove", "delete", "Delete a Warehouse", "/WHS/DEL");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createWarehouse);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyWarehouse);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveWarehouse);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeWarehouse);
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
        String[] columns = new String[]{"ID", "Org", "Name", "Street", "City", "State", "Postal", "Country", "Tax ID", "Area", "Area UOM", "Status", "Tax Exempt"};
        ArrayList<Object[]> data = new ArrayList<>();
        for (Warehouse warehouse : Engine.getWarehouses()) {
            data.add(new Object[]{
                    warehouse.getId(),
                    warehouse.getOrg(),
                    warehouse.getName(),
                    warehouse.getLine1(),
                    warehouse.getCity(),
                    warehouse.getState(),
                    warehouse.getPostal(),
                    warehouse.getCountry(),
                    warehouse.getTaxId(),
                    warehouse.getArea(),
                    warehouse.getAreaUOM(),
                    warehouse.getStatus(),
                    warehouse.isTaxExempt()
            });
        }
        return new CustomTable(columns, data);
    }
}