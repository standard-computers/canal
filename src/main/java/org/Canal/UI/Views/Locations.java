package org.Canal.UI.Views;

import org.Canal.Models.SupplyChainUnits.Location;
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
 * /$
 */
public class Locations extends LockeState implements RefreshListener {

    private String objexType;
    private DesktopState desktop;
    private CustomTable table;

    public Locations(String objexType, DesktopState desktop) {

        super("Locations", objexType, true, true, true, true);
        this.objexType = objexType;
        this.desktop = desktop;
        String oo = objexType;
        if(oo.startsWith("/")){
            oo = oo.substring(1);
        }
        JPanel tb = toolbar();
        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header(((String) Engine.codex.getValue(oo, "name")), SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));
                        desktop.put(Engine.router(objexType + "/" + v, desktop));
                        dispose();
                    }
                }
            }
        });
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue(objexType, "import_enabled")){
            IconButton importLocations = new IconButton("Import", "export", "Import as CSV");
            importLocations.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFileChooser fc = new JFileChooser();

                }
            });
            tb.add(importLocations);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue(objexType, "export_enabled")){
            IconButton export = new IconButton("Export", "export", "Export as CSV");
            export.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    table.exportToCSV();
                }
            });
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton open = new IconButton("Open", "open", "Open Location");
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("New", "create", "Create a Location", objexType + "/NEW");
        create.addActionListener(e -> {
           desktop.put(new CreateLocation(objexType, desktop, this));
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Values", objexType + "/F");
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "label", "Print labels for selected");
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selected");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));


        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(e -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));

        return tb;
    }

    private CustomTable table() {

        String[] columns = new String[]{
                "ID",
                "Org",
                "Name",
                "Line 1",
                "Line 2",
                "City",
                "State",
                "Postal",
                "Country",
                "Tax ID",
                "Tax Exempt",
                "Phone",
                "Email",
                "Departments",
                "Width",
                "wUOM",
                "Length",
                "lUOM",
                "Height",
                "hUOM",
                "Area",
                "aUOM",
                "Volume",
                "vUOM",
                "Status",
                "Created",
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Location location : Engine.getLocations(objexType)) {
            data.add(new Object[]{
                    location.getId(),
                    location.getOrganization(),
                    location.getName(),
                    location.getLine1(),
                    location.getLine2(),
                    location.getCity(),
                    location.getState(),
                    location.getPostal(),
                    location.getCountry(),
                    location.getEin(),
                    location.isTaxExempt(),
                    location.getPhone(),
                    location.getEmail(),
                    location.getDepartments().size(),
                    location.getWidth(),
                    location.getWidthUOM(),
                    location.getLength(),
                    location.getLengthUOM(),
                    location.getHeight(),
                    location.getHeightUOM(),
                    location.getArea(),
                    location.getAreaUOM(),
                    location.getVolume(),
                    location.getVolumeUOM(),
                    location.getStatus(),
                    location.getCreated(),
            });
        }
        return new CustomTable(columns, data);
    }

    @Override
    public void refresh() {
        CustomTable newTable = table();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(Engine.router(objexType + "/" + value, desktop));
                    }
                }
            }
        });
    }
}