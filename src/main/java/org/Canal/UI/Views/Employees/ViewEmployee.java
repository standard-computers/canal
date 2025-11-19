package org.Canal.UI.Views.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;

/**
 * /EMPS/$[EMPLOYEE_ID]
 */
public class ViewEmployee extends LockeState {

    private Employee employee;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    public ViewEmployee(Employee employee, DesktopState desktop, RefreshListener refreshListener) {

        super(employee.getId() + " - " + employee.getName(), "/EMPS/" + employee.getId(), false, true, false, true);
        this.employee = employee;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabbedPane = new CustomTabbedPane();

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
        info.add(toolbar(), BorderLayout.SOUTH);
        add(info, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue("EMPS", "enable_email_option")) {
            if(!employee.getEmail().isEmpty()){

                IconButton email = new IconButton("Email", "email", "Email Employee");
                email.addActionListener(_ -> {
                    try {
                        String mailto = "mailto:" + employee.getEmail();
                        Desktop.getDesktop().mail(new java.net.URI(mailto));
                    } catch (Exception d) {
                        d.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Unable to open the email client.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                tb.add(email);
                tb.add(Box.createHorizontalStrut(5));
            }
        }

        IconButton giveFeedback = new IconButton("Feedback", "feedback", "Give Feedback");
        tb.add(giveFeedback);
        tb.add(Box.createHorizontalStrut(5));

        IconButton performReview = new IconButton("Review", "invoices", "New Performance Review");
        tb.add(performReview);
        tb.add(Box.createHorizontalStrut(5));

        IconButton writeup = new IconButton("Writeup", "delinquent", "Writeup Employee");
        tb.add(writeup);
        tb.add(Box.createHorizontalStrut(5));

        IconButton modify = new IconButton("Modify", "modify", "Modify Employee");
        modify.addActionListener(_ -> {
            dispose();
            desktop.put(new ModifyEmployee(employee, desktop, refreshListener));
        });
        tb.add(modify);
        tb.add(Box.createHorizontalStrut(5));

        IconButton suspend = new IconButton("Suspend", "blocked", "Suspend Employee");
        suspend.addActionListener(_ -> {
            employee.setStatus(LockeStatus.SUSPENDED);
            employee.save();
        });
        tb.add(suspend);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }
}