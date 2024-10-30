package org.Canal.UI.Views.Distribution.Vendors;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Views.Orders.CreatePurchaseOrder;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VendorView extends JInternalFrame {

    private Location vendor;

    public VendorView(Location vendor) {
        this.vendor = vendor;
        setTitle("Vendor / " + vendor.getId() + " - " + vendor.getName());
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2));
        formPanel.add(new JTextField());
        tabbedPane.addTab("Items", formPanel);
        tabbedPane.addTab("Materials", new JScrollPane());
        tabbedPane.addTab("People", new JScrollPane());
        tabbedPane.addTab("Orders", new JScrollPane());
        tabbedPane.addTab("Shippments", new JScrollPane());
        tabbedPane.addTab("History", new JScrollPane());
        String[] columnNames = {"Properties", "Values"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tabbedPane.addTab("Data", tableScrollPane);
        JPanel info = new JPanel(new BorderLayout());
        JPanel infoLabels = new JPanel(new GridLayout(2, 2));
        JTextField vnl = new JTextField(vendor.getName());
        JTextField vil = new JTextField(vendor.getId());
        JTextField vl1l = new JTextField(vendor.getLine1() + ", " + vendor.getCity() + ", " + vendor.getState() + " " + vendor.getPostal() + " " + vendor.getCountry());
        JTextField val = new JTextField(vendor.getStatus());
        vnl.setEditable(false);
        vil.setEditable(false);
        vl1l.setEditable(false);
        val.setEditable(false);
        vnl.setFont(UIManager.getFont( "h2.font" ));
        infoLabels.add(vnl);
        infoLabels.add(vil);
        infoLabels.add(vl1l);
        infoLabels.add(val);
        info.add(infoLabels, BorderLayout.CENTER);
        info.add(createToolBar(), BorderLayout.SOUTH);
        info.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(info, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        setResizable(false);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton order = new IconButton("Order", "order_vendor", "Place an order from vendor");
        IconButton addPerson = new IconButton("+ Person", "add_person", "Add person to customer");
        IconButton delinquent = new IconButton("Delinquent", "delinquent", "Customer in dunning");
        IconButton blocked = new IconButton("Block", "blocked", "Customer is blocked. No transactions.");
        IconButton label = new IconButton("", "label", "Print labels for properties");
        IconButton refresh = new IconButton("", "refresh", "Reload from store");
        order.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CreatePurchaseOrder();
            }
        });
        tb.add(order);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addPerson);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(delinquent);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blocked);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        return tb;
    }
}