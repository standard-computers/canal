package org.Canal.UI.Views.Finance.PurchaseOrders;

import org.Canal.UI.Elements.LockeState;

/**
 * /RPTS/ORDS/PO/IS_OPEN
 */
public class OpenPOReport extends LockeState {

    public OpenPOReport() {
        super("Open Purchase Orders", "/RPTS/ORDS/PO/IS_OPEN", false, true, false, true);
    }
}