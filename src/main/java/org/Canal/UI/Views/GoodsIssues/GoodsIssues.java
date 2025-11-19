package org.Canal.UI.Views.GoodsIssues;

import org.Canal.Models.BusinessUnits.GoodsReceipt;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * /GI
 * View Goods Issue Receipts
 * Goods Issue(s) Controller
 */
public class GoodsIssues extends LockeState {

    private JTable table;
    private DesktopState desktop;

    public GoodsIssues(DesktopState desktop) {

        super("Goods Issues", "/GI");
        if(Engine.getGoodsReceipts().isEmpty()){
            dispose();
            JOptionPane.showMessageDialog(this, "No Goods Receipts Posted!");
        }

        this.desktop = desktop;
        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("All Goods Receipts", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        add(holder);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton importGoods = new IconButton("Import", "import", "Import from CSV");
        tb.add(importGoods);
        tb.add(Box.createHorizontalStrut(5));

        IconButton export = new IconButton("", "export", "Export as CSV");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));

        IconButton blockPo = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        tb.add(blockPo);
        tb.add(Box.createHorizontalStrut(5));

        IconButton suspendPo = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
        tb.add(suspendPo);
        tb.add(Box.createHorizontalStrut(5));

        IconButton activatePO = new IconButton("Start", "start", "Resume/Activate PO");
        tb.add(activatePO);
        tb.add(Box.createHorizontalStrut(5));

        IconButton archivePo = new IconButton("Archive", "archive", "Archive PO, removes");
        tb.add(archivePo);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }

    private JTable table() {

        String[] columns = new String[]{
                "ID",
                "Purchase Order",
                "Received",
                "Receiver",
                "Location",
                "Items",
                "Status"
        };
        ArrayList<String[]> goodsReceipts = new ArrayList<>();
        for (GoodsReceipt gr : Engine.getGoodsReceipts()) {
            goodsReceipts.add(new String[]{
                    gr.getId(),
                    gr.getPurchaseOrder(),
                    gr.getReceived(),
                    gr.getReceiver(),
                    gr.getLocation(),
                    String.valueOf(gr.getTotalItems()),
                    String.valueOf(gr.getStatus())
            });
        }
        String[][] data = new String[goodsReceipts.size()][columns.length];
        for (int i = 0; i < goodsReceipts.size(); i++) {
            data[i] = goodsReceipts.get(i);
        }
        JTable table = new JTable(data, columns);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return table;
    }
}