package org.Canal.UI.Views.People;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Employees.ViewEmployee;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /PPL
 */
public class People extends LockeState implements RefreshListener {

    private CustomTable table;

    public People(DesktopState desktop) {
        super("People", "/PPL");
        setFrameIcon(new ImageIcon(People.class.getResource("/icons/windows/employees.png")));

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(750, 600));
        holder.add(Elements.header("People", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
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
                        desktop.put(new ViewEmployee(Engine.getEmployee(value), desktop, People.this));
                    }
                }
            }
        });
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("", "export", "Export as CSV", "");
        IconButton importEmployees = new IconButton("Import", "export", "Import as CSV", "");
        IconButton createEmployee = new IconButton("New", "create", "Create Employee", "/PPL/NEW");
        IconButton modifyEmployee = new IconButton("Modify", "modify", "Modify an Employee", "/PPL/MOD");
        IconButton archiveEmployee = new IconButton("Archive", "archive", "Archive an Employee", "/PPL/ARCHV");
        IconButton advancedFine = new IconButton("Find", "find", "Find by Values", "/PPL/F");
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

    private CustomTable table() {
        String[] columns = new String[]{
            "ID",
            "Org",
            "Location",
            "First",
            "Middle",
            "Last",
            "Supervisor",
            "Position",
            "Pos. Name",
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
        for (Employee person : Engine.getPeople()) {
            Position p = Engine.getPosition(person.getPosition());
            data.add(new Object[]{
                    person.getId(),
                    person.getOrg(),
                    person.getLocation(),
                    person.getFirstName(),
                    person.getMiddleName(),
                    person.getLastName(),
                    person.getSupervisor(),
                    person.getPosition(),
                    (p != null ? p.getName() : ""),
                    person.getGender(),
                    person.getLine1(),
                    person.getLine2(),
                    person.getCity(),
                    person.getState(),
                    person.getPostal(),
                    person.getCountry(),
                    person.getEmail(),
                    person.getPhone(),
                    person.getStatus()
            });
        }
        return new CustomTable(columns, data);
    }

    @Override
    public void refresh() {

    }
}