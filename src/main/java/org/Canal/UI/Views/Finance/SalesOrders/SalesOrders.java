package org.Canal.UI.Views.Finance.SalesOrders;

import org.Canal.Models.BusinessUnits.SalesOrder;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finance.PurchaseRequisitions.CreatePurchaseRequisition;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ORDS/SO
 */
public class SalesOrders extends LockeState {

    private CustomTable table;

    public SalesOrders(DesktopState desktop) {
        super("Sales Orders", "/ORDS/SO", true, true, true, true);
        setFrameIcon(new ImageIcon(SalesOrders.class.getResource("/icons/salesorders.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Sales Orders", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton openSelected = new IconButton("Open", "open", "Open selected");
        IconButton createSO = new IconButton("New", "order", "Build an item");
        IconButton blockSO = new IconButton("Block", "block", "Block/Pause SO, can't be used");
        IconButton suspendSO = new IconButton("Suspend", "suspend", "Suspend SO, can't be used");
        IconButton activateSO = new IconButton("Activate", "start", "Resume/Activate SO");
        IconButton archiveSO = new IconButton("Archive", "archive", "Archive SO, removes");
        IconButton findSO = new IconButton("Find", "find", "Find by values", "/ORDS/SO/F");
        IconButton label = new IconButton("Labels", "label", "Print labels for org properties");
        IconButton print = new IconButton("Print", "print", "Print selected");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(openSelected);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createSO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blockSO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(suspendSO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(activateSO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveSO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(findSO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        openSelected.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new CreatePurchaseRequisition();
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
                "Supplier",
                "Ship To",
                "Bill To",
                "Sold To",
                "Customer",
                "Total",
                "Status"
        };
        ArrayList<Object[]> sos = new ArrayList<>();
        for (SalesOrder so : Engine.orderProcessing.getSalesOrders()) {
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