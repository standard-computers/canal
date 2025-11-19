package org.Canal.UI.Views.Customers;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * /CSTS/$[CUSTOMER_ID]
 */
public class ViewCustomer extends LockeState {

    private Location customer;

    public ViewCustomer(Location customer) {

        super("Customer / " + customer.getId() + " - " + customer.getName(), "/CSTS/$", false, true, false, true);

        this.customer = customer;
        CustomTabbedPane tabbedPane = new CustomTabbedPane();
        tabbedPane.addTab("Items", items());
        tabbedPane.addTab("Invoices", invoices());
        tabbedPane.addTab("Payments", payments());
        tabbedPane.addTab("People", people());

        JPanel info = new JPanel(new BorderLayout());
        JPanel infoLabels = new JPanel(new GridLayout(2, 2));

        JTextField vnl = new JTextField(customer.getName());
        vnl.setEditable(false);
        vnl.setFont(UIManager.getFont( "h2.font" ));
        infoLabels.add(vnl);

        JTextField vil = new JTextField(customer.getId());
        vil.setEditable(false);
        infoLabels.add(vil);

        JTextField vl1l = new JTextField(customer.getLine1() + ", " + customer.getCity() + ", " + customer.getState() + " " + customer.getPostal() + " " + customer.getCountry());
        vl1l.setEditable(false);
        infoLabels.add(vl1l);

        JTextField val = new JTextField(String.valueOf(customer.getStatus()));
        val.setEditable(false);
        infoLabels.add(val);

        info.add(infoLabels, BorderLayout.CENTER);
        info.add(toolbar(), BorderLayout.SOUTH);
        info.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(info, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        setResizable(true);
        setIconifiable(true);
        setMaximizable(true);
        setClosable(true);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton invoice = new IconButton("Invoice", "invoices", "Invoice customer");
        tb.add(invoice);
        tb.add(Box.createHorizontalStrut(5));

        IconButton addPerson = new IconButton("+ Person", "add_person", "Add person to customer");
        tb.add(addPerson);
        tb.add(Box.createHorizontalStrut(5));

        IconButton delinquent = new IconButton("Delinquent", "delinquent", "Customer in dunning");
        tb.add(delinquent);
        tb.add(Box.createHorizontalStrut(5));

        IconButton blocked = new IconButton("Block", "blocked", "Customer is blocked. No transactions.");
        tb.add(blocked);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Reload from store");
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }

    private JPanel items() {

        JPanel items = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return items;
    }

    private JPanel payments() {

        JPanel payments = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return payments;
    }

    private JPanel invoices() {

        JPanel invoices = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return invoices;
    }

    private JPanel people() {

        JPanel people = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return people;
    }

    private JPanel history() {

        JPanel history = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return history;
    }

    private JPanel inventory() {

        JPanel inventory = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return inventory;
    }

    private JPanel notes() {

        JPanel notes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return notes;
    }
}