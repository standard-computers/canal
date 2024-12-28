package org.Canal.UI.Views.Users;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Modifier;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /USRS/$[USER_ID]
 * View User provided User ID
 * User Controller
 */
public class UserView extends LockeState {

    private User user;
    private DesktopState desktop;

    public UserView(DesktopState desktop, User user) {
        super("Users / " + user.getId(), "/USRS/$", false, true, false, true);
        this.desktop = desktop;
        this.user = user;
        setFrameIcon(new ImageIcon(UserView.class.getResource("/icons/employees.png")));
        setLayout(new BorderLayout());
        JPanel userInfo = new JPanel(new GridLayout(4, 1));
        Employee emp = Engine.getEmployee(user.getEmployee());
        JTextField vnl = new Copiable(user.getId());
        JTextField vil = new Copiable(user.getEmployee());
        userInfo.add(vnl);
        userInfo.add(vil);
        userInfo.add(buttonBar());
        JPanel p = new JPanel(new BorderLayout());
        p.add(Elements.header(emp.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        p.add(userInfo, BorderLayout.CENTER);
        add(p, BorderLayout.NORTH);
        JTable table = createTable();
        JScrollPane accessHolder = new JScrollPane(table);
        add(accessHolder, BorderLayout.CENTER);
    }

    private JPanel buttonBar(){
        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        IconButton modify = new IconButton("Modify", "modify", "Modify User");
        IconButton viewEmployee = new IconButton("View Employee", "employees", "View Attached Employee");
        IconButton suspend = new IconButton("Suspend", "suspend", "Suspend User");
        buttonBar.add(modify);
        buttonBar.add(viewEmployee);
        buttonBar.add(suspend);
        modify.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new Modifier("/USRS", new User(), user));
            }
        });
        viewEmployee.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(Engine.router("/EMPS/" + user.getEmployee(), desktop));
            }
        });
        return buttonBar;
    }

    private JTable createTable() {
        String[] columns = new String[]{"Locke Code"};
        String[][] data = new String[user.getAccesses().size()][columns.length];
        for(int i = 0; i < user.getAccesses().size(); i++) {
            data[i] = new String[]{user.getAccess(i)};
        }
        JTable table = new JTable(data, columns);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Engine.adjustColumnWidths(table);
        return table;
    }
}