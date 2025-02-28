package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * /INV
 */
public class Inventory extends LockeState {

    private JTable table;

    public Inventory() {

        super("Inventory Control", "/INV", true, true, true, true);

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        table = table();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as XLSX");
        JButton performSTO = Elements.button("Stock Transfer Order");
        JButton stockCheck = Elements.button("Stock Check");
        JButton blockImc = Elements.button("Block Stock");
        JButton physicalInventory = Elements.button("Conduct PI");
        blockImc.setToolTipText("Block Item, Material, Component");
        HashMap<String, String> types = new HashMap<>();
        types.put("All", "/");
        types.put("Items", "/ITS");
        types.put("Materials", "/MTS");
        types.put("Components", "/CMPS");
        Selectable filterType = new Selectable(types);
        Selectable filterLocation = Selectables.allLocations();
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(performSTO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(stockCheck);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blockImc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(physicalInventory);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterType);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterLocation);
        return tb;
    }

    private JTable table() {

        String[] columns = new String[]{
                "ID",
                "Name",
                "Price",
                "Qty",
                "Value",
                "Location",
                "Batch"
        };
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