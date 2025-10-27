package org.Canal.UI.Views.Controllers;

import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /LOGIN
 */
public class Login extends LockeState {

    public Login(boolean closable) {
        super("Login", "/LOGIN", false, closable, false, false);
        setFrameIcon(new ImageIcon(Login.class.getResource("/icons/login.png")));

        JTextField userIdField = Elements.input(10);
        JPasswordField passwordField = new JPasswordField(10);

        Form form = new Form();
        form.addInput(Elements.inputLabel("User ID"), userIdField);
        form.addInput(Elements.inputLabel("Password"), passwordField);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);

        JButton login = Elements.button("Login");
        login.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String providedUserId = userIdField.getText().trim();
                String providedPassword = passwordField.getText().trim();
                User u = Engine.getUser(providedUserId);
                if (u.hasPassword(providedPassword)) {
                    Engine.assignUser(u);
                    JOptionPane.showMessageDialog(null, "This Canal has been assigned 😊");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Wrong Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(login, BorderLayout.SOUTH);
    }
}