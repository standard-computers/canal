package org.Canal.UI.Views.InboundDeliveryOrders;

import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.System.CheckboxBarcodeFrame;
import org.Canal.UI.Views.Distribution.ViewDelivery;
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
 * /TRANS/IDO
 */
public class InboundDeliveries extends LockeState implements RefreshListener {

    private CustomTable table;
    private DesktopState desktop;

    public InboundDeliveries(DesktopState desktop) {

        super("Inbound Deliveries", "/TRANS/IDO");
        setFrameIcon(new ImageIcon(InboundDeliveries.class.getResource("/icons/inbound.png")));
        this.desktop = desktop;

        setLayout(new BorderLayout());

        JPanel holder = new JPanel(new BorderLayout());
        holder.add(Elements.header("Active Inbound Deliveries", SwingConstants.LEFT), BorderLayout.CENTER);
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

        IconButton importDeliveries = new IconButton("Import", "export", "Import from CSV");
        toolbar.add(importDeliveries);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create IDO", "create", "Build an Inbound Delivery");
        toolbar.add(create);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton block = new IconButton("Block", "block", "Block/Pause IDO, can't be used");
        toolbar.add(block);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Labels", "label", "Print labels for selected");
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
        for (Delivery delivery : Engine.getInboundDeliveries()) {
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
//                        Engine.getLocation(delivery.getDestination(), "CCS").getName(),
                        "",
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
                        desktop.put(new ViewDelivery(Engine.getInboundDelivery(value)));
                    }
                }
            }
        });
        return ct;
    }

    @Override
    public void refresh() {

    }
}