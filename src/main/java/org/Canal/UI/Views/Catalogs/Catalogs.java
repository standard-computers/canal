package org.Canal.UI.Views.Catalogs;

import org.Canal.Models.SupplyChainUnits.BillOfMaterials;
import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finder;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /CATS
 */
public class Catalogs extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Catalogs(DesktopState desktop) {

        super("Catalogs", "/CATS", false, true, false, true);
        this.desktop = desktop;

        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));

        JPanel holder = new JPanel(new BorderLayout());
        holder.add(Elements.header("Catalogs", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private CustomTable table() {
        String[] columns = new String[]{
                "ID",
                "Name",
                "Description",
                "Period",
                "Valid From",
                "Valid To",
                "Avl CCS",
                "Avl CSTS",
                "Avl VEND",
                "Items",
                "Status",
                "Created",
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Catalog catalog : Engine.getCatalogs()) {
            data.add(new Object[]{
                    catalog.getId(),
                    catalog.getName(),
                    catalog.getDescription(),
                    catalog.getPeriod(),
                    catalog.getValidFrom(),
                    catalog.getValidTo(),
                    catalog.getCostCenters().size(),
                    catalog.getCustomers().size(),
                    catalog.getVendors().size(),
                    catalog.getItems().size(),
                    catalog.getStatus(),
                    catalog.getCreated(),
            });
        }
        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(new ViewCatalog(Engine.getCatalog(value)));
                    }
                }
            }
        });
        return ct;
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        if ((boolean) Engine.codex.getValue("ITS", "export_enabled")) {
            IconButton export = new IconButton("Export", "export", "Export as CSV", "/CATS/EXP");
            export.addActionListener(_ -> table.exportToCSV());
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton create = new IconButton("New", "create", "Create a Catalog", "/CATS/NEW");
        create.addActionListener(_ -> desktop.put(new CreateCatalog(desktop)));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton open = new IconButton("Open", "open", "Open a Catalog", "/CATS/O");
        open.addActionListener(_ -> desktop.put(Engine.router("/CATS/O", desktop)));
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Values", "/CATS/F");
        find.addActionListener(_ -> desktop.put(new Finder("/CATS", BillOfMaterials.class, desktop)));
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selected");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }

    @Override
    public void refresh() {

    }
}