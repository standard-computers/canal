package org.Canal.UI.Views.Finance.Accounts;

import org.Canal.Models.HumanResources.Department;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Departments.ViewDepartment;
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
 * /ACCS
 */
public class Accounts extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Accounts(DesktopState desktop) {

        super("Accounts", "/ACCS", true, true, true, true);
        setFrameIcon(new ImageIcon(Accounts.class.getResource("/icons/employees.png")));
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(750, 600));
        holder.add(Elements.header("Accounts", SwingConstants.LEFT), BorderLayout.CENTER);
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
                        desktop.put(new ViewEmployee(Engine.getEmployee(value), desktop, Accounts.this));
                    }
                }
            }
        });
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton open = new IconButton("Open", "open", "Open Account", "/ACCS/O");
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("New", "create", "Create Account", "/ACCS/NEW");
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Account", "/ACCS/F");
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton export = new IconButton("", "export", "Export as CSV", "");
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("", "refresh", "Refresh data");
        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refresh();
            }
        });
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    private CustomTable table() {
        String[] columns = new String[]{};
        ArrayList<Object[]> data = new ArrayList<>();
        for (Employee employee : Engine.getEmployees()) {
            Position p = Engine.getPosition(employee.getPosition());
            data.add(new Object[]{
            });
        }
        return new CustomTable(columns, data);
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