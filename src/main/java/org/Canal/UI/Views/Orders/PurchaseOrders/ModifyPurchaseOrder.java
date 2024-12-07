package org.Canal.UI.Views.Orders.PurchaseOrders;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /ORDS/PO/MOD
 * Modify a Purchase Order by Purchase Order ID
 */
public class ModifyPurchaseOrder extends LockeState {

    public ModifyPurchaseOrder() {
        super("Modify Purchase Order", "/ORDS/PO/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.YELLOW));
    }
}