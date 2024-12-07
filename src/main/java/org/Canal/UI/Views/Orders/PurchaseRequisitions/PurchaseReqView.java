package org.Canal.UI.Views.Orders.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.UI.Elements.Windows.LockeState;

/**
 * /ORDS/PR/$[PR_ID]
 */
public class PurchaseReqView extends LockeState {

    public PurchaseReqView(PurchaseRequisition requisition) {
        super("Purchase Requisitions", "/ORDS/PR/$", false, true, false, true);

    }
}