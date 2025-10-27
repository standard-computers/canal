package org.Canal.UI.Views.Users;

import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * /USRS/MOD/ALKS
 */
public class AddLocke extends LockeState {

    private JTextField userId;
    private JTextField lockeCode;

    public AddLocke() {

        super("Create User", "/USRS/MOD/ALKS", false, true, false, true);
        setFrameIcon(new ImageIcon(AddLocke.class.getResource("/icons/create.png")));
        if (Engine.getEmployees().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No employees to attach to!");
            try {
                setClosed(true);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }

        userId = Elements.input();
        lockeCode = Elements.input(20);

        Form form = new Form();
        form.addInput(Elements.inputLabel("User ID"), userId);
        form.addInput(Elements.inputLabel("Locke Code (starting with '/')"), lockeCode);

        JPanel again = new JPanel(new BorderLayout());
        again.add(Elements.header("Add Locke Access", SwingConstants.LEFT), BorderLayout.NORTH);
        again.add(form, BorderLayout.CENTER);
        again.add(toolbar(), BorderLayout.SOUTH);
        add(again, BorderLayout.NORTH);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review User");
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Grant Access", "execute", "Add Locke Code to user access");
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        create.addActionListener(_ -> {
            User user = Engine.getUser(userId.getText());
            user.addAccess(lockeCode.getText());
            user.save();
            dispose();
            JOptionPane.showMessageDialog(null, "Access granted!!!");
        });
        return tb;
    }
}