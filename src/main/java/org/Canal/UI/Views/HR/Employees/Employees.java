package org.Canal.UI.Views.HR.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
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
        holder.add(Elements.header("Employees", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double click
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Get the clicked row
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
        IconButton createEmployee = new IconButton("+ Employee", "order", "Create Employee", "/EMPS/NEW");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createEmployee);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterValue);
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
            "ID", "Org", "Location", "Name",
            "Supervisor", "Gender", "Line 1",
            "City", "State", "Postal", "Country", "Status"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Employee employee : Engine.getEmployees()) {
            data.add(new Object[]{
                    employee.getId(),
                    employee.getOrg(),
                    employee.getLocation(),
                    employee.getName(),
                    employee.getSupervisor(),
                    employee.getGender(),
                    employee.getLine1(),
                    employee.getCity(),
                    employee.getState(),
                    employee.getPostal(),
                    employee.getCountry(),
                    employee.getStatus()
            });
        }
        return new CustomTable(columns, data);
    }
}