package org.Canal.UI.Views.HR.Users;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Views.Controllers.Login;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * /USRS/CHG_PSSWD
 */
public class ChangeUserPassword extends JInternalFrame {

    public ChangeUserPassword(){
        super("Change Password", false, true, false, true);
        setFrameIcon(new ImageIcon(ChangeUserPassword.class.getResource("/icons/login.png")));
        if(Engine.getEmployees().isEmpty()){
            JOptionPane.showMessageDialog(null, "No employees to attach to!");
            try {
                setClosed(true);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }
        JPasswordField currentPasswordField = new JPasswordField(15);
        JPasswordField newPasswordField = new JPasswordField(15);
        JPasswordField repeatNewPasswordField = new JPasswordField(15);
        Form f = new Form();
        f.addInput(new Label("User ID", UIManager.getColor("Label.foreground")), new Copiable(Engine.getAssignedUser().getId()));
        f.addInput(new Label("Current Password", UIManager.getColor("Label.foreground")), currentPasswordField);
        f.addInput(new Label("New Password", UIManager.getColor("Label.foreground")), newPasswordField);
        f.addInput(new Label("Repeat New Password", UIManager.getColor("Label.foreground")), repeatNewPasswordField);
        Button updateUserPassword = new Button("Update Password");
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        add(updateUserPassword, BorderLayout.SOUTH);
    }
}