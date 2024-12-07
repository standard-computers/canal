package org.Canal.UI.Views.Orders.PurchaseRequisitions;

import org.Canal.UI.Elements.Windows.LockeState;

/**
 * /RPTS/ORDS/PR/IS_OPEN
 */
public class OpenPRReport extends LockeState {

    public OpenPRReport() {
        super("Open Purchase Reqs", "/RPTS/ORDS/PR/IS_OPEN", false, true, false, true);
    }
}