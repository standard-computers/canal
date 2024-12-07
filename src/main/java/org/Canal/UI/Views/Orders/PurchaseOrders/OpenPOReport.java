package org.Canal.UI.Views.Orders.PurchaseOrders;

import org.Canal.UI.Elements.Windows.LockeState;

/**
 * /RPTS/ORDS/PO/IS_OPEN
 */
public class OpenPOReport extends LockeState {

    public OpenPOReport() {
        super("Open Purchase Orders", "/RPTS/ORDS/PO/IS_OPEN", false, true, false, true);
    }
}