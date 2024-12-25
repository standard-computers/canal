package org.Canal.UI.Views.Distribution.Trucks;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /TRANS/TRCKS
 */
public class Trucks extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Trucks() {

        super("Trucks", "/TRANS/TRCKS", true, true, true, true);
        setFrameIcon(new ImageIcon(Trucks.class.getResource("/icons/trucks.png")));

        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header("Available Transportation (Trucks)", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(Engine.router("/TRANS/TRCKS/" + value, desktop));
                    }
                }
            }
        });
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton importLocations = new IconButton("Import", "export", "Import as CSV");
        IconButton createTruck = new IconButton("New", "order", "Create a Truck", "/TRANS/TRCKS/NEW");
        IconButton modifyTruck = new IconButton("Modify", "modify", "Modify a Truck", "/TRANS/TRCKS/MOD");
        IconButton archiveTruck = new IconButton("Archive", "archive", "Archive a Truck", "/TRANS/TRCKS/ARCHV");
        IconButton removeTruck = new IconButton("Remove", "delete", "Delete a Truck", "/TRANS/TRCKS/DEL");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importLocations);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createTruck);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyTruck);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveTruck);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeTruck);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterValue);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        importLocations.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fc = new JFileChooser();

            }
        });
        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRefresh();
            }
        });
        return tb;
    }

    private CustomTable createTable() {
        String[] columns = new String[]{
                "ID",
                "Name",
                "Number",
                "Carrier",
                "Carrier Name",
                "Driver",
                "Notes",
                "Status",
                "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Truck truck : Engine.getTrucks()) {
            Location carrier = Engine.getLocation(truck.getCarrier(), "/TRANS/CRRS");
            data.add(new Object[]{
                    truck.getId(),
                    truck.getName(),
                    truck.getNumber(),
                    truck.getCarrier(),
                    carrier.getName(),
                    truck.getDriver(),
                    truck.getNotes(),
                    truck.getStatus(),
                    truck.getCreated()
            });
        }
        return new CustomTable(columns, data);
    }

    @Override
    public void onRefresh() {
        CustomTable newTable = createTable();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double click
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Get the clicked row
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(Engine.router("/TRANS/TRCKS/" + value, desktop));
                    }
                }
            }
        });
    }
}