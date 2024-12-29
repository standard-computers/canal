package org.Canal.UI.Views.Distribution.OutboundDeliveryOrders;

import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.CheckboxBarcodeFrame;
import org.Canal.UI.Views.Distribution.InboundDeliveryOrders.DeliveryView;
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
 * /TRANS/ODO
 */
public class OutboundDeliveries extends LockeState {

    private CustomTable table;
    private DesktopState desktop;

    public OutboundDeliveries(DesktopState desktop) {

        super("Outbound Deliveries", "/TRANS/ODO", true, true, true, true);
        setFrameIcon(new ImageIcon(OutboundDeliveries.class.getResource("/icons/outbound.png")));

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
                        desktop.put(new DeliveryView(Engine.getOutboundDelivery(value)));
                    }
                }
            }
        });
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton createPurchaseOrder = new IconButton("New PO", "order", "Build an item");
        IconButton blockPo = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        IconButton suspendPo = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
        IconButton activatePO = new IconButton("Start", "start", "Resume/Activate PO");
        IconButton archivePo = new IconButton("Archive", "archive", "Archive PO, removes");
        IconButton label = new IconButton("Barcodes", "label", "Print labels for org properties");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(Elements.h3("Outbound Deliveries"));
        tb.add(Box.createHorizontalStrut(5));
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createPurchaseOrder);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blockPo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(suspendPo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(activatePO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archivePo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        createPurchaseOrder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseOrder());
            }
        });
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.orderProcessing.getPurchaseOrder().size()];
                for (int i = 0; i < Engine.orderProcessing.getPurchaseOrder().size(); i++) {
                    printables[i] = Engine.orderProcessing.getPurchaseOrder().get(i).getOrderId();
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
        for (Delivery delivery : Engine.getOutboundDeliveries()) {
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