package org.Canal.UI.Views.HR.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.IconButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmployeeView extends JInternalFrame {

    private Employee employee;

    public EmployeeView(Employee employee) {
        super("", false, true, false, true);
        this.employee = employee;
        setTitle("Employee / " + employee.getId() + " - " + employee.getName());
        setFrameIcon(new ImageIcon(Employees.class.getResource("/icons/employees.png")));
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
        add(info, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        setResizable(true);
        setIconifiable(true);
        setClosable(true);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton email = new IconButton("Email", "email", "Email Employee");
        IconButton giveFeedback = new IconButton("Feedback", "feedback", "Give Feedback");
        IconButton performReview = new IconButton("Review", "invoice", "New Performance Review");
        IconButton writeup = new IconButton("", "delinquent", "Writeup Employee");
        IconButton suspend = new IconButton("", "blocked", "Suspend Employee");
        IconButton label = new IconButton("", "label", "Print labels for properties");
        IconButton refresh = new IconButton("", "refresh", "Reload from store");
        tb.add(email);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(giveFeedback);
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
        performReview.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        writeup.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        return tb;
    }
}