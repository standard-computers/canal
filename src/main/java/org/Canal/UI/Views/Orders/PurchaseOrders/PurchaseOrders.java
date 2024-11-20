package org.Canal.UI.Views.Orders.PurchaseOrders;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Views.Controllers.CheckboxBarcodeFrame;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ORDS, /ORDS/PO
 */
public class PurchaseOrders extends JInternalFrame {

    private JTable table;
    private DesktopState desktop;

    public PurchaseOrders(DesktopState desktop) {
        super("Purchase Orders", true, true, true, true);
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(PurchaseOrders.class.getResource("/icons/purchaseorders.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("All Purchase Orders", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        add(holder);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
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
        JTextField filterValue = Elements.input("Search", 10);
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
        tb.add(filterValue);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
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

    private JTable createTable() {
        String[] columns = new String[]{
                "ID", "Owner", "Ordered", "Expexcted Deliv.", "Purchase Req.",
                "Supplier", "Ship To", "Bill To", "Sold To", "Customer", "Total", "Status"
        };
        ArrayList<String[]> pos = new ArrayList<>();
        for (PurchaseOrder po : Engine.orderProcessing.getPurchaseOrder()) {
            pos.add(new String[]{
                    po.getOrderId(),
                    po.getOwner(),
                    po.getOrderedOn(),
                    po.getExpectedDelivery(),
                    po.getPurchaseRequisition(),
                    po.getVendor(),
                    po.getShipTo(),
                    po.getBillTo(),
                    po.getSoldTo(),
                    po.getCustomer(),
                    String.valueOf(po.getTotal()),
                    String.valueOf(po.getStatus())
            });
        }
        String[][] data = new String[pos.size()][columns.length];
        for (int i = 0; i < pos.size(); i++) {
            data[i] = pos.get(i);
        }
        JTable table = new JTable(data, columns);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Engine.adjustColumnWidths(table);
        return table;
    }
}