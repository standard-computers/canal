package org.Canal.UI.Views.Finance.Customers;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CSTS/MOD
 */
public class ModifyCustomer extends LockeState {

    public ModifyCustomer() {
        super("Modify Customer", "/CSTS/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyCustomer.class.getResource("/icons/modify.png")));
    }
}