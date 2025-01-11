package org.Canal.UI.Views.Customers;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finance.Invoices.CreateInvoice;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CSTS/$[CUSTOMER_ID]
 */
public class ViewCustomer extends LockeState {

    private Location customer;

    public ViewCustomer(Location customer) {

        super("Customer / " + customer.getId() + " - " + customer.getName(), "/CSTS/$", false, true, false, true);
        setFrameIcon(new ImageIcon(ViewCustomer.class.getResource("/icons/customers.png")));

        this.customer = customer;
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2));
        formPanel.add(new JTextField());
        tabbedPane.addTab("Items", formPanel);
        tabbedPane.addTab("Payments", new JScrollPane());
        JTextArea textArea = new JTextArea("This is a text area where you can write something...");
        JScrollPane textScrollPane = new JScrollPane(textArea);
        tabbedPane.addTab("People", textScrollPane);
        JScrollPane ordersPane = new JScrollPane(textArea);
        tabbedPane.addTab("Orders", ordersPane);
        JScrollPane shippmentsPane = new JScrollPane(textArea);
        tabbedPane.addTab("Shippments", ordersPane);
        JScrollPane historyPane = new JScrollPane(textArea);
        tabbedPane.addTab("History", ordersPane);
        tabbedPane.addTab("Inventory", new JScrollPane());
        String[] columnNames = {"Properties", "Values"};
        Object[][] data = {
                {"Name", "John"},
                {"Age", "30"},
                {"Occupation", "Developer"}
        };
        JTable table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tabbedPane.addTab("Data", tableScrollPane);
        JPanel info = new JPanel(new BorderLayout());
        JPanel infoLabels = new JPanel(new GridLayout(2, 2));
        JTextField vnl = new JTextField(customer.getName());
        JTextField vil = new JTextField(customer.getId());
        JTextField vl1l = new JTextField(customer.getLine1() + ", " + customer.getCity() + ", " + customer.getState() + " " + customer.getPostal() + " " + customer.getCountry());
        JTextField val = new JTextField(String.valueOf(customer.getStatus()));
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
        setResizable(true);
        setIconifiable(true);
        setMaximizable(true);
        setClosable(true);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton invoice = new IconButton("Invoice", "invoice", "Invoice customer");
        IconButton addPerson = new IconButton("+ Person", "add_person", "Add person to customer");
        IconButton delinquent = new IconButton("Delinquent", "delinquent", "Customer in dunning");
        IconButton blocked = new IconButton("Block", "blocked", "Customer is blocked. No transactions.");
        IconButton refresh = new IconButton("Refresh", "refresh", "Reload from store");
        invoice.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CreateInvoice(customer.getId());
            }
        });
        tb.add(invoice);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addPerson);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(delinquent);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blocked);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        return tb;
    }
}