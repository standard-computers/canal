package org.Canal.UI.Views.Finance.PurchaseOrders;

import org.Canal.Models.BusinessUnits.Order;
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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ORDS/PO
 */
public class PurchaseOrders extends LockeState implements RefreshListener {

    private CustomTable table;
    private DesktopState desktop;

    public PurchaseOrders(DesktopState desktop) {

        super("Purchase Orders", "/ORDS/PO");
        setFrameIcon(new ImageIcon(PurchaseOrders.class.getResource("/icons/purchaseorders.png")));
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Purchase Orders", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        add(holder);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue("ORDS/PO", "import_enabled")){
            IconButton importPOs = new IconButton("Import", "export", "Import as CSV");
            tb.add(importPOs);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("ORDS/PO", "export_enabled")){
            IconButton export = new IconButton("Export", "export", "Export as CSV");
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton openSelected = new IconButton("Open", "open", "Open selected");
        tb.add(openSelected);
        tb.add(Box.createHorizontalStrut(5));

        IconButton createPurchaseOrder = new IconButton("New PO", "create", "Build an item", "/ORDS/PO/NEW");
        createPurchaseOrder.addActionListener(_ -> desktop.put(new CreatePurchaseOrder(desktop)));
        tb.add(createPurchaseOrder);
        tb.add(Box.createHorizontalStrut(5));

        IconButton blockPO = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        tb.add(blockPO);
        tb.add(Box.createHorizontalStrut(5));

        IconButton suspendPO = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
        tb.add(suspendPO);
        tb.add(Box.createHorizontalStrut(5));

        IconButton activatePO = new IconButton("Start", "start", "Resume/Activate PO");
        tb.add(activatePO);
        tb.add(Box.createHorizontalStrut(5));

        IconButton findPO = new IconButton("Find", "find", "Find by values", "/ORDS/PO/F");
        tb.add(findPO);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "label", "Print labels for org properties");
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selectes");
        labels.addActionListener(_ -> {
            String[] printables = new String[Engine.getPurchaseOrders().size()];
            for (int i = 0; i < Engine.getPurchaseOrders().size(); i++) {
                printables[i] = Engine.getPurchaseOrders().get(i).getOrderId();
            }
            new CheckboxBarcodeFrame(printables);
        });
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        return tb;
    }

    private CustomTable table() {
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
                "Lines",
                "Items",
                "Created",
                "Status",
        };
        ArrayList<Object[]> pos = new ArrayList<>();

        for (Order po : Engine.getPurchaseOrders()) {
            if(!po.getStatus().equals(LockeStatus.DELIVERED)
                    && !po.getStatus().equals(LockeStatus.ARCHIVED)
                    && !po.getStatus().equals(LockeStatus.DELETED)){

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
                        po.getItems().size(),
                        po.getTotalItems(),
                        po.getCreated(),
                        String.valueOf(po.getStatus())
                });
            }
        }
        CustomTable nt = new CustomTable(columns, pos);
        nt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int row = t.getSelectedRow();
                    if (row != -1) {
                        String v = String.valueOf(t.getValueAt(row, 1));
                        desktop.put(new ViewPurchaseOrder(Engine.getPurchaseOrder(v), desktop, PurchaseOrders.this));
                    }
                }
            }
        });
        return nt;
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