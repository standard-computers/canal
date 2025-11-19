package org.Canal.UI.Views.Tasks;

import org.Canal.Models.BusinessUnits.Order;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.System.CheckboxBarcodeFrame;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /MVMT/TSKS
 */
public class TaskList extends LockeState {

    private JTable table;
    private DesktopState desktop;
    private Location location;

    public TaskList(Location location, DesktopState desktop) {
        super("Task List", "/MVMT/TSKS");
        this.desktop = desktop;
        JPanel tb = toolbar();
        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(tableScrollPane, BorderLayout.CENTER);
        holder.add(tb, BorderLayout.NORTH);
        add(holder);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton export = new IconButton("", "export", "Export as CSV");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));

        IconButton createPurchaseOrder = new IconButton("Create", "create", "Build an item");
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

        IconButton archivePO = new IconButton("Archive", "archive", "Archive PO, removes");
        tb.add(archivePO);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "barcodes", "Print labels for org properties");
        labels.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.getPurchaseOrders().size()];
                for (int i = 0; i < Engine.getPurchaseOrders().size(); i++) {
                    printables[i] = Engine.getPurchaseOrders().get(i).getOrderId();
                }
                new CheckboxBarcodeFrame(printables);
            }
        });
        tb.add(labels);

        return tb;
    }

    private JTable table() {
        String[] columns = new String[]{
                "ID",
                "Owner",
                "Supplier",
                "Ship To",
                "Bill To",
                "Sold To",
                "Customer",
                "Status"
        };
        ArrayList<String[]> pos = new ArrayList<>();
        for (Order po : Engine.getPurchaseOrders()) {
            pos.add(new String[]{
                    po.getOrderId(),
                    po.getOwner(),
                    po.getVendor(),
                    po.getShipTo(),
                    po.getBillTo(),
                    po.getSoldTo(),
                    po.getCustomer(),
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
        return table;
    }
}