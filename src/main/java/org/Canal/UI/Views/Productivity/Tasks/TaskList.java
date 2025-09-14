package org.Canal.UI.Views.Productivity.Tasks;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finance.PurchaseOrders.CreatePurchaseOrder;
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
        super("Task List", "/MVMT/TSKS", true, true, true, true);
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
        IconButton createPurchaseOrder = new IconButton("Create", "create", "Build an item");
        IconButton blockPO = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        IconButton suspendPO = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
        IconButton activatePO = new IconButton("Start", "start", "Resume/Activate PO");
        IconButton archivePO = new IconButton("Archive", "archive", "Archive PO, removes");
        IconButton labels = new IconButton("Labels", "label", "Print labels for org properties");
        IconButton print = new IconButton("Print", "print", "Print selected...");
        tb.add(export);
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
        tb.add(labels);
        createPurchaseOrder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseOrder(desktop));
            }
        });
        labels.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.getPurchaseOrders().size()];
                for (int i = 0; i < Engine.getPurchaseOrders().size(); i++) {
                    printables[i] = Engine.getPurchaseOrders().get(i).getOrderId();
                }
                new CheckboxBarcodeFrame(printables);
            }
        });
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
        for (PurchaseOrder po : Engine.getPurchaseOrders()) {
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