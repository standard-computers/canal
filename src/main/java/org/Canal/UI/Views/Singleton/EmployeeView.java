package org.Canal.UI.Views.Singleton;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.IconButton;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmployeeView extends JInternalFrame {

    private Employee employee;

    public EmployeeView(Employee employee) {
        this.employee = employee;
        setTitle("Employee / " + employee.getId() + " - " + employee.getName());
        JTabbedPane tabbedPane = new JTabbedPane();

        JScrollPane positionsHeld = new JScrollPane();
        tabbedPane.addTab("Positions", positionsHeld);

        tabbedPane.addTab("Applications", new JScrollPane());

        JScrollPane textScrollPane = new JScrollPane();
        tabbedPane.addTab("Notes", textScrollPane);

        JScrollPane empReveiws = new JScrollPane();
        tabbedPane.addTab("Reviews", empReveiws);

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
        setClosable(true);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton performReview = new IconButton("Perform Review", "invoice", "Invoice customer");
        IconButton writeup = new IconButton("Writeup", "delinquent", "Customer in dunning");
        IconButton suspend = new IconButton("Suspend", "blocked", "Customer is blocked. No transactions.");
        IconButton label = new IconButton("", "label", "Print labels for properties");
        IconButton refresh = new IconButton("", "refresh", "Reload from store");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(performReview);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(writeup);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(suspend);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        return tb;
    }
}