package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JInternalFrame {

    public Login() {
        super("Login", false, false, false, false);
        setFrameIcon(new ImageIcon(Login.class.getResource("/icons/login.png")));
        Form f = new Form();
        JTextField userIdField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        f.addInput(new Label("User ID", UIManager.getColor("Label.foreground")), userIdField);
        f.addInput(new Label("Password", UIManager.getColor("Label.foreground")), passwordField);
        Button login = new Button("Login");
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        add(login, BorderLayout.SOUTH);
        login.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
    }
}