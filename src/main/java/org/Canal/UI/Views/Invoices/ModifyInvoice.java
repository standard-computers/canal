package org.Canal.UI.Views.Invoices;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /INVS/MOD
 */
public class ModifyInvoice extends LockeState {

    public ModifyInvoice() {
        super("Modify Invoice", "/INVS/MOD/$", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyInvoice.class.getResource("/icons/modify.png")));
    }
}