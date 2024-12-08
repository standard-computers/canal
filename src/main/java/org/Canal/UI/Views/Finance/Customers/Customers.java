package org.Canal.UI.Views.Finance.Customers;

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
 * /CSTS
 */
public class Customers extends LockeState implements RefreshListener {

    private CustomTable table;

    public Customers(DesktopState desktop) {
        super("Customers", "/CSTS", true, true, true, true);
        setFrameIcon(new ImageIcon(Customers.class.getResource("/icons/customers.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header("Customers", SwingConstants.LEFT), BorderLayout.CENTER);
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
                        desktop.put(new CustomerView(Engine.getCustomer(value)));
                    }
                }
            }
        });
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> onRefresh();
        scheduler.scheduleAtFixedRate(task, 60, 30, TimeUnit.SECONDS);
//        Runnable shutdownTask = () -> {
//            System.out.println("Shutting down scheduler...");
//            scheduler.shutdown();
//        };
//        scheduler.schedule(shutdownTask, 30, TimeUnit.SECONDS);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton createCustomer = new IconButton("New Customer", "order", "Create a Customer", "/CSTS/NEW");
        IconButton modifyCustomer = new IconButton("Modify", "modify", "Modify a Customer", "/CSTS/MOD");
        IconButton archiveCostCenter = new IconButton("Archive", "archive", "Archive a Cost Center", "/CCS/ARCHV");
        IconButton removeCustomer = new IconButton("Remove", "delete", "Delete a Customer", "/CSTS/DEL");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createCustomer);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyCustomer);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveCostCenter);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeCustomer);
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
        String[] columns = new String[]{"ID", "Org", "Name", "Street", "City", "State", "Postal", "Country", "Status", "Tax Exempt", "Phone", "Email"};
        ArrayList<Object[]> data = new ArrayList<>();
        for (Location csts : Engine.getCustomers()) {
            data.add(new Object[]{
                    csts.getId(),
                    csts.getTie(),
                    csts.getName(),
                    csts.getLine1(),
                    csts.getCity(),
                    csts.getState(),
                    csts.getPostal(),
                    csts.getCountry(),
                    csts.getStatus(),
                    csts.isTaxExempt(),
                    csts.getPhone(),
                    csts.getEmail()
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