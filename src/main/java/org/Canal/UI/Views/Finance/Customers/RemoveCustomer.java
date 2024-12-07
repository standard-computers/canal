package org.Canal.UI.Views.Finance.Customers;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CSTS/DEL
 */
public class RemoveCustomer extends LockeState {

    public RemoveCustomer() {
        super("Remove Customer", "/CSTS/DEL", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));

    }
}