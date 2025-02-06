package org.Canal.UI.Views.Users;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
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
 * /USRS
 * List of Users registerd in Canal attached to selected Organization.
 */
public class Users extends LockeState {

    private CustomTable table;

    public Users(DesktopState desktop) {

        super("Users", "/USRS", false, true, false, true);
        setFrameIcon(new ImageIcon(Users.class.getResource("/icons/employees.png")));
        setLayout(new BorderLayout());

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
        return new CustomTable(columns, data);
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton importUsers = new IconButton("Export", "export", "Export as CSV", "");
        IconButton openSelected = new IconButton("Open", "open", "Open selected");
        IconButton create = new IconButton("Create", "create", "Create a User", "/USRS/NEW");
        IconButton modify = new IconButton("Modify", "modify", "Modify a User", "/USRS/MOD");
        IconButton remove = new IconButton("Remove", "delete", "Delete a User", "/USRS/DEL");
        IconButton labels = new IconButton("Labels", "label", "Delete a User");
        IconButton print = new IconButton("Print", "print", "Print selected");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importUsers);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(openSelected);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modify);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(remove);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        return tb;
    }
}