package org.Canal.UI.Views.Trucks;

import org.Canal.Models.SupplyChainUnits.Delivery;
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

    public Trucks(DesktopState desktop) {

        super("Trucks", "/TRANS/TRCKS");
        setFrameIcon(new ImageIcon(Trucks.class.getResource("/icons/windows/trucks.png")));
        this.desktop = desktop;

        JPanel tb = toolbar();
        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header("Available Transportation (Trucks)", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton export = new IconButton("Export", "export", "Export as CSV");
        export.addActionListener(_ -> table.exportToCSV());
        toolbar.add(export);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton importTrucks = new IconButton("Import", "export", "Import as CSV");
        toolbar.add(importTrucks);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton createTruck = new IconButton("New", "create", "Create a Truck", "/TRANS/TRCKS/NEW");
        createTruck.addActionListener(_ -> desktop.put(new CreateTruck(desktop)));
        toolbar.add(createTruck);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(_ -> refresh());
        toolbar.add(refresh);
        toolbar.add(Box.createHorizontalStrut(5));

        return toolbar;
    }

    private CustomTable table() {

        String[] columns = new String[]{
                "ID",
                "Name",
                "Number",
                "Carrier",
                "Carrier Name",
                "Delivery",
                "Driver",
                "Pallets",
                "Value",
                "Est Weight",
                "wUOM",
                "Status",
                "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Truck truck : Engine.getTrucks()) {

            Location carrier = Engine.getLocation(truck.getCarrier(), "TRANS/CRRS");
            Delivery delivery = null;
            if(truck.getDelivery().startsWith(String.valueOf(Engine.codex.getValue("TRANS/IDO", "prefix")))){

                delivery = Engine.getInboundDelivery(truck.getDelivery());
            }else if(truck.getDelivery().startsWith(String.valueOf(Engine.codex.getValue("TRANS/ODO", "prefix")))){

                delivery = Engine.getOutboundDelivery(truck.getDelivery());
            }
            data.add(new Object[]{
                    truck.getId(),
                    truck.getName(),
                    truck.getNumber(),
                    truck.getCarrier(),
                    (!carrier.getName().isEmpty() ? carrier.getName() : ""),
                    truck.getDelivery(),
                    truck.getDriver(),
                    (delivery == null ? 0 : delivery.getPallets().size()),
                    "0.0",
                    "",
                    "LBS",
                    truck.getStatus(),
                    truck.getCreated()
            });
        }
        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        Truck truck = Engine.getTruck(value);
                        desktop.put(new ViewTruck(truck, desktop, Trucks.this));
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