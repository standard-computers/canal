package org.Canal.UI.Views.Singleton;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Views.Transactions.Invoices.CreateInvoice;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmployeeView extends JInternalFrame {

    private Employee employee;

    public EmployeeView(Employee employee) {
        this.employee = employee;
        setTitle("Employee / " + employee.getId() + " - " + employee.getName());
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2));
        formPanel.add(new JTextField());
        tabbedPane.addTab("Positions", formPanel);
        tabbedPane.addTab("Applications", new JScrollPane());
        JTextArea textArea = new JTextArea("This is a text area where you can write something...");
        JScrollPane textScrollPane = new JScrollPane(textArea);
        tabbedPane.addTab("Notes", textScrollPane);
        JScrollPane ordersPane = new JScrollPane(textArea);
        tabbedPane.addTab("Reviews", ordersPane);
        JPanel info = new JPanel(new BorderLayout());
        JPanel infoLabels = new JPanel(new GridLayout(2, 2));
        JTextField vnl = new JTextField(employee.getName());
        JTextField vil = new JTextField(employee.getId());
        vnl.setEditable(false);
        vil.setEditable(false);
        vnl.setFont(UIManager.getFont("h2.font"));
        infoLabels.add(vnl);
        infoLabels.add(vil);
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
        IconButton label = new IconButton("", "label", "Print labels for properties");
        IconButton delinquent = new IconButton("Delinquent", "delinquent", "Customer in dunning");
        IconButton blocked = new IconButton("Block", "blocked", "Customer is blocked. No transactions.");
        IconButton refresh = new IconButton("", "refresh", "Reload from store");
        invoice.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CreateInvoice(employee.getId());
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
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        return tb;
    }
}