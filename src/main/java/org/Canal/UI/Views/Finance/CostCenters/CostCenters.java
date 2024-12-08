package org.Canal.UI.Views.Finance.CostCenters;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.UI.Views.Finance.Customers.CustomerView;
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
 * /CCS
 */
public class CostCenters extends LockeState implements RefreshListener {

    private CustomTable table;

    public CostCenters(DesktopState desktop) {
        super("Cost Centers", "/CCS", true, true, true, true);
        setFrameIcon(new ImageIcon(CostCenters.class.getResource("/icons/cost_centers.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Cost Centers", SwingConstants.LEFT), BorderLayout.CENTER);
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
                        desktop.put(new CostCenterView(Engine.getCostCenter(value), desktop));
                    }
                }
            }
        });
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> onRefresh();
        scheduler.scheduleAtFixedRate(task, 60, 30, TimeUnit.SECONDS);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton createCostCenter = new IconButton("New Cost Center", "order", "Create a Vendor", "/VEND/NEW");
        IconButton modifyCostCenter = new IconButton("Modify", "modify", "Modify a Cost Center", "/CCS/MOD");
        IconButton archiveCostCenter = new IconButton("Archive", "archive", "Archive a Cost Center", "/CCS/ARCHV");
        IconButton removeCostCenter = new IconButton("Remove", "delete", "Delete a Cost Center", "/CCS/DEL");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createCostCenter);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyCostCenter);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveCostCenter);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeCostCenter);
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
        for (Location location : Engine.getCostCenters()) {
            data.add(new Object[]{
                    location.getId(),
                    location.getTie(),
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
    }
}