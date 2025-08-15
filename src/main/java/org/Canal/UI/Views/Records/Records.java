package org.Canal.UI.Views.Records;

import org.Canal.Models.HumanResources.Department;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Departments.ViewDepartment;
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
 * /RCS
 */
public class Records extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Records(DesktopState desktop) {

        super("Records", "/RCS", false, true, false, true);
        setFrameIcon(new ImageIcon(Records.class.getResource("/icons/locke.png")));
        this.desktop = desktop;

        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
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
        IconButton export = new IconButton("", "export", "Export as CSV");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
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
        String[] c = new String[]{
                "ID",
                "Description",
                "Type",
                "Reference",
                "Notes",
                "Modified",
                "Status",
                "Created",
        };
        ArrayList<Object[]> d = new ArrayList<>();
        for (Department r : Engine.getOrganization().getDepartments()) {
            d.add(new Object[]{
            });
        }
        return new CustomTable(c, d);
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