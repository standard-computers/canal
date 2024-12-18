package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.HR.Departments.CreateDepartment;
import org.Canal.UI.Views.HR.Departments.Departments;
import org.Canal.UI.Views.HR.Departments.FindDepartment;
import org.Canal.UI.Views.HR.Employees.CreateEmployee;
import org.Canal.UI.Views.HR.Employees.Employees;
import org.Canal.UI.Views.HR.Employees.FindEmployee;
import org.Canal.UI.Views.HR.Positions.CreatePosition;
import org.Canal.UI.Views.HR.Users.ChangeUserPassword;
import org.Canal.UI.Views.HR.Users.CreateUser;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CNL/HR
 */
public class HumanResources extends LockeState {

    private DesktopState desktop;

    public HumanResources(DesktopState desktop) {
        super("ERM / Human Resources", "/CNL/HR", false, true, false, true);
        setFrameIcon(new ImageIcon(HumanResources.class.getResource("/icons/humanresources.png")));
        this.desktop = desktop;

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employees", employees());

        JPanel positionOptions = new JPanel();
        positionOptions.setBorder(new EmptyBorder(5, 5, 5, 5));
        JButton openPositions = Elements.button("Open Positions");
        openPositions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        positionOptions.add(openPositions);
        JButton createPosition = Elements.button("Create Positions");
        createPosition.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePosition(desktop));
            }
        });
        positionOptions.add(createPosition);
        tabbedPane.addTab("Positions", positionOptions);

        JPanel departmentOptions = new JPanel(new GridLayout(2, 2, 5, 5));
        departmentOptions.setBorder(new EmptyBorder(5, 5, 5, 5));
        JButton viewDepartments = Elements.button("View Departments");
        viewDepartments.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               desktop.put(new Departments(desktop));
           }
        });
        departmentOptions.add(viewDepartments);
        JButton createDepartment = Elements.button("Create Department");
        createDepartment.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               desktop.put(new CreateDepartment(desktop));
           }
        });
        departmentOptions.add(createDepartment);
        JButton findDepartment = Elements.button("Find Department");
        findDepartment.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               desktop.put(new FindDepartment(desktop));
           }
        });
        departmentOptions.add(findDepartment);
        tabbedPane.addTab("Departments", departmentOptions);

        JPanel userOptions = new JPanel(new GridLayout(2, 2, 5, 5));
        userOptions.setBorder(new EmptyBorder(5, 5, 5, 5));
        JButton createUser = Elements.button("Create User");
        userOptions.add(createUser);
        createUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateUser());
            }
        });
        JButton resetPassword = Elements.button("Reset Password");
        resetPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new ChangeUserPassword());
            }
        });
        userOptions.add(resetPassword);
        tabbedPane.addTab("Users", userOptions);

        setLayout(new BorderLayout());
        add(Elements.header("Human Resources Center", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel employees(){
        JPanel p = new JPanel(new GridLayout(2, 1));
        JPanel employeeOptions = new JPanel(new GridLayout(2, 2, 5, 5));
        employeeOptions.setBorder(new EmptyBorder(5, 5, 5, 5));
        JButton viewEmployees = Elements.button("View Employees");
        viewEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new Employees(desktop));
            }
        });
        employeeOptions.add(viewEmployees);
        JButton createEmployee = Elements.button("Create Employee");
        createEmployee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateEmployee(desktop));
            }
        });
        employeeOptions.add(createEmployee);
        JButton findEmployee = Elements.button("Find Employee");
        findEmployee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new FindEmployee(desktop));
            }
        });
        employeeOptions.add(findEmployee);
        p.add(employeeOptions);
        return p;
    }
}