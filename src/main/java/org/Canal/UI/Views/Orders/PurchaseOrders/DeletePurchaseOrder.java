package org.Canal.UI.Views.Orders.PurchaseOrders;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /ORDS/PO/DEL
 */
public class DeletePurchaseOrder extends LockeState {

    public DeletePurchaseOrder() {
        super("Delete Purchase Order", "/ORDS/PO/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(DeletePurchaseOrder.class.getResource("/icons/modify.png")));
    }
}