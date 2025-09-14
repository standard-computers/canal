package org.Canal.UI.Views.Controllers;

import org.Canal.Models.HumanResources.Department;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finder;
import org.Canal.UI.Views.Departments.CreateDepartment;
import org.Canal.UI.Views.Departments.Departments;
import org.Canal.UI.Views.Employees.CreateEmployee;
import org.Canal.UI.Views.Employees.Employees;
import org.Canal.UI.Views.Positions.CreatePosition;
import org.Canal.UI.Views.Users.ChangeUserPassword;
import org.Canal.UI.Views.Users.CreateUser;
import org.Canal.UI.Views.ViewLocation;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /HR
 */
public class HumanResources extends LockeState {

    private DesktopState desktop;

    public HumanResources(DesktopState desktop) {

        super("ERM / Human Resources", "/HR", true, true, true, true);
        setFrameIcon(new ImageIcon(HumanResources.class.getResource("/icons/humanresources.png")));
        this.desktop = desktop;

        CustomTabbedPane tabbedPane = new CustomTabbedPane();
        tabbedPane.addTab("Users", new ImageIcon(ViewLocation.class.getResource("/icons/users.png")), users());
        tabbedPane.addTab("People", new ImageIcon(ViewLocation.class.getResource("/icons/employees.png")), employees());
        tabbedPane.addTab("Positions", new ImageIcon(ViewLocation.class.getResource("/icons/positions.png")), positions());
        tabbedPane.addTab("Departments", new ImageIcon(ViewLocation.class.getResource("/icons/departments.png")), departments());
        tabbedPane.addTab("Timesheets", timesheets());

        setLayout(new BorderLayout());
        add(Elements.header("Human Resource Center", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        setMaximized(true);
    }

    private JPanel users() {

        JPanel userOptions = new JPanel(new GridLayout(2, 2, 5, 5));
        userOptions.setBorder(new EmptyBorder(5, 5, 5, 5));
        JButton createUser = Elements.button("Create User");
        userOptions.add(createUser);
        createUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateUser(desktop, null));
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
        return userOptions;
    }

    private JPanel positions() {

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
                desktop.put(new CreatePosition(desktop, null));
            }
        });
        positionOptions.add(createPosition);
        return positionOptions;
    }

    private JPanel departments() {

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
                desktop.put(new CreateDepartment(desktop, null));
            }
        });
        departmentOptions.add(createDepartment);
        JButton findDepartment = Elements.button("Find Department");
        findDepartment.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new Finder("/DPTS", new Department(), desktop));
            }
        });
        departmentOptions.add(findDepartment);
        return departmentOptions;
    }

    private JPanel employees(){

        JPanel p = new JPanel(new GridLayout(2, 1));
        JPanel employeeOptions = new JPanel(new GridLayout(2, 2, 5, 5));
        employeeOptions.setBorder(new EmptyBorder(5, 5, 5, 5));
        JButton viewEmployees = Elements.button("View People");
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
                desktop.put(new CreateEmployee(desktop, null));
            }
        });
        employeeOptions.add(createEmployee);
        JButton findEmployee = Elements.button("Find Employee");
        findEmployee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new Finder("/EMPS", new Employee(), desktop));
            }
        });
        employeeOptions.add(findEmployee);
        p.add(employeeOptions);
        return p;
    }

    private JPanel timesheets(){
        JPanel p = new JPanel();

        return p;
    }
}