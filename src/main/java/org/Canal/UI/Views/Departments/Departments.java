package org.Canal.UI.Views.Departments;

import org.Canal.Models.HumanResources.Department;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Deleter;
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
 * /DPTS
 */
public class Departments extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Departments(DesktopState desktop) {

        super("Departments", "/DPTS", true, true, true, true);
        setFrameIcon(new ImageIcon(Departments.class.getResource("/icons/departments.png")));
        this.desktop = desktop;

        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 600));
        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, details());
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));
                        for(Department d : Engine.getOrganization().getDepartments()){
                            if(d.getId().equals(v)){
                                desktop.put(new ViewDepartment(d));
                            }
                        }
                    }
                }
            }
        });
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.add(Elements.header("Departments", SwingConstants.LEFT), BorderLayout.NORTH);
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton importDepartments = new IconButton("Import", "export", "Import as CSV");
        IconButton createDepartment = new IconButton("New", "create", "Create a Department", "/DPTS/NEW");
        IconButton modifyDepartment = new IconButton("Modify", "modify", "Modify a Department", "/DPTS/MOD");
        IconButton archiveDepartment = new IconButton("Archive", "archive", "Archive a Department", "/DPTS/ARCHV");
        IconButton removeDepartment = new IconButton("Remove", "delete", "Delete a Department");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importDepartments);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createDepartment);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyDepartment);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveDepartment);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeDepartment);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        importDepartments.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fc = new JFileChooser();

            }
        });
        removeDepartment.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new Deleter("/DPTS", Departments.this));
            }
        });
        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refresh();
            }
        });
        toolbar.add(tb, BorderLayout.SOUTH);
        return toolbar;
    }

    private CustomTable table() {
        String[] columns = new String[]{
                "ID",
                "Name",
                "Organization",
                "Location",
                "Department",
                "Supervisor",
                "Positions",
                "Status",
                "Created",
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Department department : Engine.getOrganization().getDepartments()) {
            data.add(new Object[]{
                    department.getId(),
                    department.getName(),
                    department.getOrganization(),
                    department.getLocation(),
                    department.getDepartment(),
                    department.getSupervisor(),
                    department.getPositions().size(),
                    department.getStatus(),
                    department.getCreated(),
            });
        }
        return new CustomTable(columns, data);
    }

    private CustomTabbedPane details() {

        JPanel p = new JPanel(new BorderLayout());
        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Departments", new JPanel());
        tabs.addTab("Teams", new JPanel());
        tabs.addTab("Positions", new JPanel());
        tabs.addTab("Employees", new JPanel());
        return tabs;
    }

    @Override
    public void refresh() {
        CustomTable newTable = table();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));
                        for(Department d : Engine.getOrganization().getDepartments()){
                            if(d.getId().equals(v)){
                                desktop.put(new ViewDepartment(d));
                            }
                        }
                    }
                }
            }
        });
    }
}