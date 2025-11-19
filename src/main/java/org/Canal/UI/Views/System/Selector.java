package org.Canal.UI.Views.System;

import org.Canal.Models.Objex;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /?
 */
public class Selector extends LockeState implements RefreshListener {

    private String objex;
    private CustomTable table;
    private Selection selection;

    public Selector(String objex, Selection selection) {

        super("Selection Screen", "/?");
        this.objex = objex;
        this.selection = selection;
        this.table = table();

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(550, 450));
        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);

        toolbar.add(Elements.header("Rates", SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private CustomTable table() {

        String[] columns = new String[]{
                "ID",
                "Type",
                "Name",
                "Status",
                "Created"
        };
        ArrayList<Object[]> d = new ArrayList<>();
        ArrayList<Objex> objs = (objex.isEmpty() ? Engine.getLocationsObjex() : Engine.getObjex(objex));
        for (Objex obj : objs) {
            d.add(new Object[]{
                    obj.getId(),
                    obj.getType(),
                    obj.getName(),
                    obj.getStatus(),
                    obj.getCreated(),
            });
        }
        CustomTable ct = new CustomTable(columns, d);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int row = t.getSelectedRow();
                    if (row != -1) {
                        String v = String.valueOf(t.getValueAt(row, 1));
                        selection.select(v);
                        dispose();
                    }
                }
            }
        });
        return ct;
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