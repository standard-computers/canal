package org.Canal.UI.Views.Departments;

import org.Canal.Models.HumanResources.Department;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ViewDepartment extends LockeState {

    public ViewDepartment(Department department) {
        super("Department " + department.getId(), "/DPTS/$", true, true, true, true);
        setFrameIcon(new ImageIcon(ViewDepartment.class.getResource("/icons/departments.png")));
        JTabbedPane tabbedPane = new JTabbedPane();

        JScrollPane positionsList = new JScrollPane();
        tabbedPane.addTab("Positions", positionsList);

        JScrollPane employeesList = new JScrollPane();
        tabbedPane.addTab("Employees", employeesList);

        JScrollPane heirarchy = new JScrollPane();
        tabbedPane.addTab("Heirarchy", heirarchy);

        JScrollPane documentsList = new JScrollPane();
        tabbedPane.addTab("Documents", documentsList);

        JPanel info = new JPanel(new BorderLayout());
        JPanel infoLabels = new JPanel(new GridLayout(2, 1));
        JTextField vnl = new Copiable(department.getName());
        JTextField vil = new Copiable(department.getId());
        vnl.setFont(UIManager.getFont("h2.font"));
        infoLabels.add(vnl);
        infoLabels.add(vil);
        info.add(infoLabels, BorderLayout.CENTER);
        info.add(createToolBar(), BorderLayout.SOUTH);
        info.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(info, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton performReview = new IconButton("Perform Review", "invoice", "Invoice customer");
        IconButton writeup = new IconButton("Writeup", "delinquent", "Customer in dunning");
        IconButton suspend = new IconButton("+ Docs", "documents", "Add documents for department");
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