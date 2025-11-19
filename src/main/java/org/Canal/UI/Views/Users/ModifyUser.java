package org.Canal.UI.Views.Users;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * /USRS/MOD/$[USER_ID]
 * Modify a User provided User ID
 * User Controller
 */
public class ModifyUser extends LockeState {

    //Operating Objects
    private User user;

    public ModifyUser(User user) {

        super("Modify " + user.getId(), "/USRS/MOD/" + user.getId(), false, true, false, true);
        this.user = user;

        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        CustomTable table = table();
        JScrollPane accessHolder = new JScrollPane(table);
        add(accessHolder, BorderLayout.CENTER);
    }

    private JPanel header() {

        JPanel header = new JPanel(new BorderLayout());

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        Employee emp = Engine.getEmployee(user.getEmployee());

        JTextField vnl = new Copiable(user.getId());
        userInfo.add(vnl);

        JTextField vil = new Copiable(user.getEmployee());
        userInfo.add(vil);
        header.add(Elements.header("User for " + emp.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(userInfo, BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.SOUTH);

        return header;
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton save = new IconButton("Save", "save", "Save Changes");
        save.addActionListener(_ -> {

        });
        tb.add(save);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-save");
        rp.getActionMap().put("do-save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save.doClick();
            }
        });

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }

    private CustomTable table() {

        String[] columns = new String[]{"Locke Code"};
        ArrayList<Object[]> data = new ArrayList<>();
        for (String access : user.getAccesses()) {
            data.add(new Object[]{access});
        }
        CustomTable table = new CustomTable(columns, data);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return table;
    }

    private void refresh() {

    }
}