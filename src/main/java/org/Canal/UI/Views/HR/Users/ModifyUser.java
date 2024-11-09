package org.Canal.UI.Views.HR.Users;

import org.Canal.Models.HumanResources.User;

import javax.swing.*;
import java.awt.*;

/**
 * /USRS/MOD
 */
public class ModifyUser extends JInternalFrame {

    public ModifyUser(User user) {
        super("Modify User", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyUser.class.getResource("/icons/modify.png")));
    }
}