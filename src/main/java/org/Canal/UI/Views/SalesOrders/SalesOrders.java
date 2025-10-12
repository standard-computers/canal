package org.Canal.UI.Views.SalesOrders;

import org.Canal.Models.BusinessUnits.Order;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finder;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * /ORDS/SO
 */
public class SalesOrders extends LockeState {

    private DesktopState desktop;
    private CustomTable table;

    public SalesOrders(DesktopState desktop) {

        super("Sales Orders", "/ORDS/SO");
        setFrameIcon(new ImageIcon(SalesOrders.class.getResource("/icons/salesorders.png")));
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Sales Orders", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add( toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        if((boolean) Engine.codex.getValue("ORDS/SO", "import_enabled")) {
            IconButton importSalesOrders = new IconButton("Import", "export", "Import from CSV");
            tb.add(importSalesOrders);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("ORDS/SO", "export_enabled")) {
            IconButton export = new IconButton("Export", "export", "Export as CSV");
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton openSelected = new IconButton("Open", "open", "Open selected");
        tb.add(openSelected);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Build an item", "/ORDS/SO/NEW");
        create.addActionListener(_ -> desktop.put(new CreateSalesOrder(desktop)));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by values", "/ORDS/SO/F");
        find.addActionListener(_ -> desktop.put(new Finder("/ORDS/SO", Order.class, desktop)));
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Labels", "label", "Print labels for org properties");
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selected");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        tb.add(refresh);

        return tb;
    }

    private CustomTable table() {
        String[] columns = new String[]{
                "ID",
                "Owner",
                "Ordered",
                "Expexcted Deliv.",
                "Supplier",
                "Ship To",
                "Bill To",
                "Sold To",
                "Customer",
                "Total",
                "Status"
        };
        ArrayList<Object[]> sos = new ArrayList<>();
        for (Order so : Engine.getSalesOrders()) {
            sos.add(new Object[]{
                    so.getOrderId(),
                    so.getOwner(),
                    so.getOrderedOn(),
                    so.getExpectedDelivery(),
                    so.getVendor(),
                    so.getShipTo(),
                    so.getBillTo(),
                    so.getSoldTo(),
                    so.getCustomer(),
                    so.getTotal(),
                    String.valueOf(so.getStatus())
            });
        }
        return new CustomTable(columns, sos);
    }
}