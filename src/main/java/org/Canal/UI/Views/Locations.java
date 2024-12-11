package org.Canal.UI.Views;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        setFrameIcon(new ImageIcon(Locations.class.getResource("/icons/distribution_centers.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header(objexType, SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> onRefresh();
        scheduler.scheduleAtFixedRate(task, 60, 30, TimeUnit.SECONDS);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton createDc = new IconButton("New", "order", "Create a DC", "/DCSS/NEW");
        IconButton modifyDc = new IconButton("Modify", "modify", "Modify a DC", "/DCSS/MOD");
        IconButton archiveDc = new IconButton("Archive", "archive", "Archive a DC", "/DCSS/ARCHV");
        IconButton removeDc = new IconButton("Remove", "delete", "Delete a DC", "/DCSS/DEL");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeDc);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterValue);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        return tb;
    }

    private CustomTable createTable() {
        String[] columns = new String[]{"ID", "Org", "Name", "Street", "City", "State", "Postal", "Country", "Status", "Tax Exempt"};
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
                    location.isTaxExempt()
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