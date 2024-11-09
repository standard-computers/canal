package org.Canal.UI.Views.Orders.SalesOrders;

import javax.swing.*;
import java.awt.*;

/**
 * /ORDS/SO/MOD
 */
public class ModifySalesOrder extends JInternalFrame {

    public ModifySalesOrder() {
        super("Modify Sales Order", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifySalesOrder.class.getResource("/icons/modify.png")));
    }
}