package org.Canal.UI.Views.HR.Users;

import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Views.HR.Employees.Employees;

import javax.swing.*;
import java.awt.*;

public class UserView extends JInternalFrame {

    public UserView(User user) {
        super("User View", true, true, true, true);
        setFrameIcon(new ImageIcon(Employees.class.getResource("/icons/employees.png")));
        JPanel userInfo = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        setLayout(new BorderLayout());


    }
}