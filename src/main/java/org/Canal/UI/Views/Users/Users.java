package org.Canal.UI.Views.Users;

import org.Canal.Models.HumanResources.Department;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
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
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /USRS
 * List of Users registerd in Canal attached to selected Organization.
 */
public class Users extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Users(DesktopState desktop) {

        super("Users", "/USRS", true, true, true, true);
        setFrameIcon(new ImageIcon(Users.class.getResource("/icons/employees.png")));
        setLayout(new BorderLayout());
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Users", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private CustomTable table() {
        String[] columns = new String[]{
                "ID",
                "Employee ID",
                "Name",
                "Employee Name",
                "Accesses",
                "Status",
                "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (User users : Engine.getUsers()) {
            Employee e = Engine.getEmployee(users.getEmployee() );
            data.add(new Object[]{
                    users.getId(),
                    users.getEmployee(),
                    users.getName(),
                    e.getName(),
                    users.getAccesses().size(),
                    users.getStatus(),
                    users.getCreated(),
            });
        }
        CustomTable newTable = new CustomTable(columns, data);
        newTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));
                        for (User u : Engine.getUsers()) {
                            if (u.getId().equals(v)) {
                                desktop.put(new ViewUser(desktop, u));
                            }
                        }
                    }
                }
            }
        });
        return newTable;
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton importUsers = new IconButton("Import", "import", "Import from CSV", "");
        importUsers.addActionListener(_ -> table.exportToCSV());
        tb.add(importUsers);
        tb.add(Box.createHorizontalStrut(5));

        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        export.addActionListener(_ -> table.exportToCSV());
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));

        IconButton openSelected = new IconButton("Open", "open", "Open selected");
        openSelected.addActionListener(e -> {
            String uid = JOptionPane.showInputDialog("Enter User ID");
            if (uid != null) {
                User u =  Engine.getUser(uid);
                desktop.put(new ViewUser(desktop, u));
            }
        });
        tb.add(openSelected);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create a User", "/USRS/NEW");
        create.addActionListener(_ -> desktop.put(new CreateUser(desktop, this)));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "label", "Delete a User");
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selected");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        refresh.addActionListener(e -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
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