package org.Canal.UI.Views.Finance.Customers;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CSTS/ARCHV
 */
public class ArchiveCustomer extends LockeState {

    public ArchiveCustomer() {
        super("Archive Customer", "/CSTS/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}