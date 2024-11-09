package org.Canal.UI.Views.Finance.Customers;

import javax.swing.*;
import java.awt.*;

/**
 * /CSTS/MOD
 */
public class ModifyCustomer extends JInternalFrame {

    public ModifyCustomer() {
        super("Modify Customer", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyCustomer.class.getResource("/icons/modify.png")));
    }
}