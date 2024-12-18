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
        setFrameIcon(new ImageIcon(Locations.class.getResource("/icons/distribution_centers.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
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
                if (e.getClickCount() == 2) { // Detect double click
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Get the clicked row
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(Engine.router(objexType + "/" + value, desktop));
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
        IconButton createDc = new IconButton("New", "order", "Create a Location", objexType + "/NEW");
        IconButton modifyDc = new IconButton("Modify", "modify", "Modify a Location", objexType + "/MOD");
        IconButton archiveDc = new IconButton("Archive", "archive", "Archive a Location", objexType + "/ARCHV");
        IconButton removeDc = new IconButton("Remove", "delete", "Delete a Location", objexType + "/DEL");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importLocations);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeDc);
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
        String[] columns = new String[]{"ID", "Org", "Name", "Street", "City", "State", "Postal", "Country", "Status", "Tax Exempt", "Phone", "Email"};
        ArrayList<Object[]> data = new ArrayList<>();
        for (Location location : Engine.getLocations(objexType)) {
            data.add(new Object[]{
                    location.getId(),
                    location.getOrganization(),
                    location.getName(),
                    location.getLine1(),
                    location.getCity(),
                    location.getState(),
                    location.getPostal(),
                    location.getCountry(),
                    location.getStatus(),
                    location.isTaxExempt(),
                    location.getPhone(),
                    location.getEmail(),
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
                        desktop.put(Engine.router(objexType + "/" + value, desktop));
                    }
                }
            }
        });
    }
}