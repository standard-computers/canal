package org.Canal.UI.Views.Invoices;

import javax.swing.*;
import java.awt.*;

/**
 * /INVS/MOD
 */
public class ModifyInvoice extends JInternalFrame {

    public ModifyInvoice() {
        super("Modify Invoice", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyInvoice.class.getResource("/icons/modify.png")));
    }
}