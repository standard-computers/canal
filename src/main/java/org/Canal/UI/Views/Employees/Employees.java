package org.Canal.UI.Views.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /EMPS
 */
public class Employees extends LockeState {

    private CustomTable table;

    public Employees(DesktopState desktop) {
        super("Employees", "/EMPS", true, true, true, true);
        setFrameIcon(new ImageIcon(Employees.class.getResource("/icons/employees.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(750, 600));
        holder.add(Elements.header("Employees", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(new EmployeeView(Engine.getEmployee(value)));
                    }
                }
            }
        });
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton importEmployees = new IconButton("Import", "export", "Import as CSV", "");
        IconButton createEmployee = new IconButton("New", "order", "Create Employee", "/EMPS/NEW");
        IconButton modifyEmployee = new IconButton("Modify", "modify", "Modify an Employee", "/EMPS/MOD");
        IconButton archiveEmployee = new IconButton("Archive", "archive", "Archive an Employee", "/EMPS/ARCHV");
        IconButton removeEmployee = new IconButton("Remove", "delete", "Delete an Employee", "/EMPS/DEL");
        IconButton advancedFine = new IconButton("Find", "find", "Find by Values", "/EMPS/F");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importEmployees);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createEmployee);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyEmployee);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveEmployee);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeEmployee);
        tb.add(removeEmployee);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(advancedFine);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        return tb;
    }

    private CustomTable createTable() {
        String[] columns = new String[]{
            "ID",
            "Org",
            "Location",
            "Name",
            "Supervisor",
            "Position",
            "Gender",
            "Line 1",
            "Line 2",
            "City",
            "State",
            "Postal",
            "Country",
            "Email",
            "Phone",
            "Status"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Employee employee : Engine.getEmployees()) {
            data.add(new Object[]{
                    employee.getId(),
                    employee.getOrg(),
                    employee.getLocation(),
                    employee.getName(),
                    employee.getSupervisor(),
                    employee.getPosition(),
                    employee.getGender(),
                    employee.getLine1(),
                    employee.getLine2(),
                    employee.getCity(),
                    employee.getState(),
                    employee.getPostal(),
                    employee.getCountry(),
                    employee.getEmail(),
                    employee.getPhone(),
                    employee.getStatus()
            });
        }
        return new CustomTable(columns, data);
    }
}