package org.Canal.UI.Views.Controllers;

import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Constants;
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
        Constants.checkLocke(this, false, true);
        setFrameIcon(new ImageIcon(Login.class.getResource("/icons/login.png")));
        Form f = new Form();
        JTextField userIdField = Elements.input(10);
        JPasswordField passwordField = new JPasswordField(10);
        f.addInput(new Label("User ID", UIManager.getColor("Label.foreground")), userIdField);
        f.addInput(new Label("Password", UIManager.getColor("Label.foreground")), passwordField);
        JButton login = Elements.button("Login");
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        add(login, BorderLayout.SOUTH);
        login.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String providedUserId = userIdField.getText().trim();
                String providedPassword = passwordField.getText().trim();
                User u = Engine.getUser(providedUserId);
                if(u.hasPassword(providedPassword)){
                    Engine.assignUser(u);
                    JOptionPane.showMessageDialog(null, "This Canal has been assigned 😊");
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(Login.this, "Wrong Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}