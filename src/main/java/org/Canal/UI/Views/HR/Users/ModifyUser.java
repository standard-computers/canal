package org.Canal.UI.Views.HR.Users;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;

/**
 * /USRS/MOD
 */
public class ModifyUser extends LockeState {

    private User user;

    public ModifyUser(User user) {
        super("Modify User", "/USRS/MOD", false, true, false, true);
        this.user = user;
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyUser.class.getResource("/icons/modify.png")));
        setLayout(new BorderLayout());
        JPanel userInfo = new JPanel(new GridLayout(4, 1));
        Employee emp = Engine.getEmployee(user.getEmployee());
        JTextField empName = new Copiable(emp.getName());
        JTextField vnl = new Copiable(user.getId());
        JTextField vil = new Copiable(user.getEmployee());
        userInfo.add(empName);
        userInfo.add(vnl);
        userInfo.add(vil);
        userInfo.add(buttonBar());
        add(userInfo, BorderLayout.NORTH);
        JTable table = createTable();
        JScrollPane accessHolder = new JScrollPane(table);
        add(accessHolder, BorderLayout.CENTER);
    }

    private JPanel buttonBar() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        IconButton suspend = new IconButton("Suspend", "suspend", "");
        IconButton remove = new IconButton("Remove", "delete_rows", "");
        IconButton archive = new IconButton("Archive", "archive", "");
        IconButton save = new IconButton("Save", "save", "");
        buttonPane.add(suspend);
        buttonPane.add(remove);
        buttonPane.add(archive);
        buttonPane.add(save);
        return buttonPane;
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