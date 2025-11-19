package org.Canal.UI.Views.BOMS;

import org.Canal.Models.SupplyChainUnits.BillOfMaterials;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finder;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /BOMS
 */
public class BOMs extends LockeState implements RefreshListener {

    private ArrayList<BillOfMaterials> boms;
    private DesktopState desktop;
    private CustomTable table;

    public BOMs(ArrayList<BillOfMaterials> boms, DesktopState desktop) {

        super("Bill of Materials", "/BOMS");
        this.boms = boms;
        this.desktop = desktop;

        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1300, 600));

        JPanel holder = new JPanel(new BorderLayout());
        holder.add(Elements.header("Bill of Materials", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("ITS", "start_maximized")) {
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        if ((boolean) Engine.codex.getValue("ITS", "export_enabled")) {
            IconButton export = new IconButton("Export", "export", "Export as CSV", "");
            export.addActionListener(_ -> table.exportToCSV());
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton create = new IconButton("New", "create", "Create an Item", "/BOMS/NEW");
        create.addActionListener(_ -> desktop.put(new CreateBOM(desktop, this)));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton open = new IconButton("Open", "open", "Open an Bill of Materials", "/BOMS/O");
        open.addActionListener(_ -> desktop.put(Engine.router("/BOMS/O", desktop)));
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Values", "/BOMS/F");
        find.addActionListener(_ -> desktop.put(new Finder("/BOMS", BillOfMaterials.class, desktop)));
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "barcodes", "Print barcode for an Area");
        tb.add(labels);
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

    private CustomTable table() {

        String[] columns = new String[]{
                "ID",
                "Name",
                "Location",
                "Customer",
                "Components",
                "Steps",
                "Status",
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for (BillOfMaterials bom : boms) {
            bom = Engine.getBoM(bom.getId());
            data.add(new Object[]{
                    bom.getId(),
                    bom.getName(),
                    bom.getLocation(),
                    bom.getCustomer(),
                    bom.getComponents().size(),
                    bom.getSteps().size(),
                    bom.getStatus(),
            });
        }
        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int row = t.getSelectedRow();
                    if (row != -1) {
                        String v = String.valueOf(t.getValueAt(row, 1));
                        desktop.put(new ViewBOM(Engine.getBoM(v), desktop, BOMs.this));
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