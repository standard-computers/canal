package org.Canal.UI.Views.Distribution.InboundDeliveryOrders;

import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.System.CheckboxBarcodeFrame;
import org.Canal.UI.Views.Distribution.ViewDelivery;
import org.Canal.UI.Views.Finance.PurchaseOrders.CreatePurchaseOrder;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /TRANS/IDO
 */
public class InboundDeliveries extends LockeState {

    private CustomTable table;
    private DesktopState desktop;

    public InboundDeliveries(DesktopState desktop) {

        super("Inbound Deliveries", "/TRANS/IDO", false, true, false, true);
        setFrameIcon(new ImageIcon(InboundDeliveries.class.getResource("/icons/inbound.png")));

        this.desktop = desktop;
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(tableScrollPane, BorderLayout.CENTER);
        holder.add(tb, BorderLayout.NORTH);
        add(holder);
        table.addMouseListener(new MouseAdapter() {
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
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton createIDO = new IconButton("Create IDO", "create", "Build an Inbound Delivery");
        IconButton blockIDO = new IconButton("Block", "block", "Block/Pause IDO, can't be used");
        IconButton suspendIDO = new IconButton("Suspend", "suspend", "Suspend IDO, can't be used");
        IconButton activateIDO = new IconButton("Start", "start", "Resume/Activate IDO");
        IconButton archiveIDO = new IconButton("Archive", "archive", "Archive IDO");
        IconButton label = new IconButton("Labels", "label", "Print labels for selected");
        IconButton print = new IconButton("Print", "print", "Print selected");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(Elements.h3("Inbound Deliveries"));
        tb.add(Box.createHorizontalStrut(5));
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createIDO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blockIDO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(suspendIDO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(activateIDO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveIDO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));
        createIDO.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseOrder(desktop));
            }
        });
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.orders.getPurchaseOrder().size()];
                for (int i = 0; i < Engine.orders.getPurchaseOrder().size(); i++) {
                    printables[i] = Engine.orders.getPurchaseOrder().get(i).getOrderId();
                }
                new CheckboxBarcodeFrame(printables);
            }
        });
        return tb;
    }

    private CustomTable createTable() {
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
            if(!delivery.getStatus().equals(LockeStatus.DELIVERED)) {
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
        return new CustomTable(columns, data);
    }
}