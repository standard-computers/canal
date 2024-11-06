package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
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
public class HumanResources extends JInternalFrame {

    public HumanResources(DesktopState desktop) {
        super("ERM / Human Resources", false, true, false, true);
        setFrameIcon(new ImageIcon(HumanResources.class.getResource("/icons/humanresources.png")));
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel employeeOptions = new JPanel(new GridLayout(2, 2, 5, 5));
        employeeOptions.setBorder(new EmptyBorder(5, 5, 5, 5));
        Button viewEmployees = new Button("View Employees");
        viewEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new Employees(desktop));
            }
        });
        employeeOptions.add(viewEmployees);
        Button createEmployee = new Button("Create Employee");
        createEmployee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateEmployee(desktop));
            }
        });
        employeeOptions.add(createEmployee);
        Button findEmployee = new Button("Find Employee");
        findEmployee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new FindEmployee(desktop));
            }
        });
        employeeOptions.add(findEmployee);
        tabbedPane.addTab("Employees", employeeOptions);

        JPanel positionOptions = new JPanel();
        positionOptions.setBorder(new EmptyBorder(5, 5, 5, 5));
        Button openPositions = new Button("Open Positions");
        openPositions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        positionOptions.add(openPositions);
        Button createPosition = new Button("Create Positions");
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
        Button viewDepartments = new Button("View Departments");
        viewDepartments.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               desktop.put(new Departments(desktop));
           }
        });
        departmentOptions.add(viewDepartments);
        Button createDepartment = new Button("Create Department");
        createDepartment.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               desktop.put(new CreateDepartment(desktop));
           }
        });
        departmentOptions.add(createDepartment);
        Button findDepartment = new Button("Find Department");
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
        Button createUser = new Button("Create User");
        userOptions.add(createUser);
        createUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateUser());
            }
        });
        Button resetPassword = new Button("Reset Password");
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
}