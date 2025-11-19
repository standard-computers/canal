package org.Canal.UI.Views.Deliveries;

import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.System.CheckboxBarcodeFrame;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /TRANS/ODO
 * View active Outbound Deliveries.
 * Deliveries must not be DELIVERED, DELETED, or ARCHIVED.
 */
public class OutboundDeliveries extends LockeState implements RefreshListener {

    private CustomTable table;
    private DesktopState desktop;

    public OutboundDeliveries(DesktopState desktop) {

        super("Outbound Deliveries", "/TRANS/ODO");
        this.desktop = desktop;

        setLayout(new BorderLayout());

        JPanel holder = new JPanel(new BorderLayout());
        holder.add(Elements.header("Active Outbound Deliveries", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        add(holder, BorderLayout.NORTH);

        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        add(tableScrollPane, BorderLayout.CENTER);

    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton export = new IconButton("Export", "export", "Export as CSV");
        toolbar.add(export);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton importDeliveries = new IconButton("Import", "import", "Import from CSV");
        toolbar.add(importDeliveries);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create ODO", "create", "Build an item");
        toolbar.add(create);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton activatePO = new IconButton("Start", "start", "Resume/Activate PO");
        toolbar.add(activatePO);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Barcodes", "barcodes", "Print labels for org properties");
        label.addActionListener(_ -> {
            String[] printables = new String[Engine.getPurchaseOrders().size()];
            for (int i = 0; i < Engine.getPurchaseOrders().size(); i++) {
                printables[i] = Engine.getPurchaseOrders().get(i).getOrderId();
            }
            new CheckboxBarcodeFrame(printables);
        });
        toolbar.add(label);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selected");
        toolbar.add(print);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh");
        refresh.addActionListener(_ -> refresh());
        toolbar.add(refresh);
        toolbar.add(Box.createHorizontalStrut(5));

        return toolbar;
    }

    private CustomTable table() {
        String[] columns = new String[]{
                "ID",
                "Description",
                "Type",
                "Sales Order",
                "Pur. Order",
                "Exp. Delivery",
                "Origin",
                "Destination",
                "Destination Name",
                "Dest. Area",
                "Dest. Door",
                "Value",
                "Truck ID",
                "Pallet Cnt.",
                "Status",
                "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Delivery delivery : Engine.getOutboundDeliveries()) {
            if (!delivery.getStatus().equals(LockeStatus.DELETED)
                    && !delivery.getStatus().equals(LockeStatus.ARCHIVED)
                    && !delivery.getStatus().equals(LockeStatus.DELIVERED)) {
                data.add(new Object[]{
                        delivery.getId(),
                        delivery.getName(),
                        delivery.getType(),
                        delivery.getSalesOrder(),
                        delivery.getPurchaseOrder(),
                        delivery.getExpectedDelivery(),
                        delivery.getOrigin(),
                        delivery.getDestination(),
                        Engine.getLocation(delivery.getDestination(), "CCS").getName(),
                        delivery.getDestinationArea(),
                        delivery.getDestinationDoor(),
                        delivery.getTotal(),
                        "",
                        delivery.getPallets().size(),
                        delivery.getStatus(),
                        delivery.getCreated()
                });
            }
        }
        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double click
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Get the clicked row
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(new ViewDelivery(Engine.getOutboundDelivery(value)));
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