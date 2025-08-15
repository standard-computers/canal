package org.Canal.UI.Views.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /EMPS
 */
public class Employees extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Employees(DesktopState desktop) {

        super("Employees", "/EMPS", true, true, true, true);
        setFrameIcon(new ImageIcon(Employees.class.getResource("/icons/employees.png")));
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(750, 600));
        holder.add(Elements.header("Employees", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        if((boolean) Engine.codex.getValue("EMPS", "start_maximized")){
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue("EMPS", "import_enabled")){
            IconButton importEmployees = new IconButton("Import", "export", "Import as CSV", "");
            importEmployees.addActionListener(e -> {

            });
            tb.add(importEmployees);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("EMPS", "export_enabled")){
            IconButton export = new IconButton("", "export", "Export as CSV", "");
            export.addActionListener(e -> table.exportToCSV());
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton open = new IconButton("Open", "open", "Open with ID", "/EMPS/O");
        open.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eid = JOptionPane.showInputDialog("Employee ID");
                Employee em = Engine.getEmployee(eid);
                if(em != null){
                    desktop.put(new ViewEmployee(em, desktop, Employees.this));
                }
            }
        });
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create a User", "/EMPS/NEW");
        create.addActionListener(_ -> desktop.put(new CreateEmployee(desktop, Employees.this)));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Values", "/EMPS/F");
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "label", "Delete a User");
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Refresh", "print", "Print selected");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Referesh", "refresh", "Refresh data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        return tb;
    }

    private CustomTable table() {
        String[] columns = new String[]{
                "ID",
                "Org",
                "Location",
                "First Name",
                "Middle Name",
                "Last Name",
                "Supervisor",
                "Position",
                "Pos. Name",
                "Ethnicity",
                "Gender",
                "Line 1",
                "Line 2",
                "City",
                "State",
                "Postal",
                "Country",
                "Email",
                "Phone",
                "Disability",
                "Veteran",
                "Status"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Employee employee : Engine.getEmployees()) {
            Position p = Engine.getPosition(employee.getPosition());
            data.add(new Object[]{
                    employee.getId(),
                    employee.getOrg(),
                    employee.getLocation(),
                    employee.getFirstName(),
                    employee.getMiddleName(),
                    employee.getLastName(),
                    employee.getSupervisor(),
                    employee.getPosition(),
                    (p != null ? p.getName() : ""),
                    employee.getEthnicity(),
                    employee.getGender(),
                    employee.getLine1(),
                    employee.getLine2(),
                    employee.getCity(),
                    employee.getState(),
                    employee.getPostal(),
                    employee.getCountry(),
                    employee.getEmail(),
                    employee.getPhone(),
                    employee.isDisability(),
                    employee.isVeteran(),
                    employee.getStatus()
            });
        }
        CustomTable ct =  new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));
                        for (Employee d : Engine.getEmployees()) {
                            if (d.getId().equals(v)) {
                                desktop.put(new ViewEmployee(d, desktop, Employees.this));
                            }
                        }
                    }
                }
            }
        });
        return ct;
    }

    @Override
    public void refresh() {
        CustomTable newTable = table();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}