package org.Canal.UI.Views.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /EMPS/$[EMPLOYEE_ID]
 */
public class EmployeeView extends LockeState {

    private Employee employee;

    public EmployeeView(Employee employee) {
        super(employee.getId() + " - " + employee.getName(), "/EMPS/$", false, true, false, true);
        this.employee = employee;
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
        JTextField vnl = new Copiable(employee.getName());
        JTextField vil = new Copiable(employee.getId());
        vnl.setFont(UIManager.getFont("h2.font"));
        infoLabels.add(vnl);
        infoLabels.add(vil);
        info.add(infoLabels, BorderLayout.CENTER);
        info.add(createToolBar(), BorderLayout.SOUTH);
        add(info, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton email = new IconButton("Email", "email", "Email Employee");
        IconButton giveFeedback = new IconButton("Feedback", "feedback", "Give Feedback");
        IconButton performReview = new IconButton("Review", "invoice", "New Performance Review");
        IconButton writeup = new IconButton("Writeup", "delinquent", "Writeup Employee");
        IconButton suspend = new IconButton("Suspend", "blocked", "Suspend Employee");
        IconButton label = new IconButton("Labels", "label", "Print labels for properties (like for badges)");
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
        email.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                try {
                    String mailto = "mailto:" + employee.getEmail();
                    Desktop.getDesktop().mail(new java.net.URI(mailto));
                } catch (Exception d) {
                    d.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Unable to open the email client.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        giveFeedback.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

            }
        });
        performReview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        writeup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        suspend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        return tb;
    }
}