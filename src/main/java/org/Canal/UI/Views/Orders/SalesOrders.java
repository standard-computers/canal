package org.Canal.UI.Views.Orders;

import org.Canal.UI.Elements.IconButton;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SalesOrders extends JInternalFrame {

    private JTable table;

    public SalesOrders(Desktop desktop) {
        super("Sales Orders");
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(tableScrollPane, BorderLayout.CENTER);
        holder.add(tb, BorderLayout.NORTH);
        add(holder);
        setIconifiable(true);
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton addPo = new IconButton("+ Order", "order", "Build an item");
        IconButton blockPo = new IconButton("", "block", "Block/Pause PO, can't be used");
        IconButton suspendPo = new IconButton("", "suspend", "Suspend PO, can't be used");
        IconButton activatePO = new IconButton("", "start", "Resume/Activate PO");
        IconButton archivePo = new IconButton("", "archive", "Archive PO, removes");
        IconButton label = new IconButton("", "label", "Print labels for org properties");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addPo);
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
        addPo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new CreatePurchaseRequisition();
            }
        });
        return tb;
    }

    private JTable createTable() {
        String[] columns = new String[]{"ID", "number", "Owner", "Supplier", "Buyer", "Amount", "Create", "Valid From", "Valid To", "Status"};
        ArrayList<String[]> pos = new ArrayList<>();
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