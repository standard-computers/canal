package org.Canal.UI.Views.Finance.PurchaseOrders;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.System.CheckboxBarcodeFrame;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ORDS/PO
 */
public class PurchaseOrders extends LockeState {

    private CustomTable table;
    private DesktopState desktop;

    public PurchaseOrders(DesktopState desktop) {
        super("Purchase Orders", "/ORDS/PO", true, true, true, true);
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(PurchaseOrders.class.getResource("/icons/purchaseorders.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Purchase Orders", SwingConstants.LEFT), BorderLayout.CENTER);
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
        IconButton openSelected = new IconButton("Open", "open", "Open selected");
        IconButton createPurchaseOrder = new IconButton("New PO", "create", "Build an item", "/ORDS/PO/NEW");
        IconButton blockPO = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        IconButton suspendPO = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
        IconButton activatePO = new IconButton("Start", "start", "Resume/Activate PO");
        IconButton archivePO = new IconButton("Archive", "archive", "Archive PO, removes", "/ORDS/PO/ARCHV");
        IconButton findPO = new IconButton("Find", "find", "Find by values", "/ORDS/PO/F");
        IconButton labels = new IconButton("Labels", "label", "Print labels for org properties");
        IconButton print = new IconButton("Print", "print", "Print selectes");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(openSelected);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createPurchaseOrder);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blockPO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(suspendPO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(activatePO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archivePO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(findPO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        labels.addMouseListener(new MouseAdapter() {
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
                "Owner",
                "Ordered",
                "Expexcted Deliv.",
                "Purchase Req.",
                "Supplier",
                "Ship To",
                "Bill To",
                "Sold To",
                "Customer",
                "Total",
                "Status"
        };
        ArrayList<Object[]> pos = new ArrayList<>();
        for (PurchaseOrder po : Engine.orderProcessing.getPurchaseOrder()) {
            pos.add(new Object[]{
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
                    po.getTotal(),
                    String.valueOf(po.getStatus())
            });
        }
        return new CustomTable(columns, pos);
    }
}