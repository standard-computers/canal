package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.UI.Elements.LockeState;

/**
 * /ORDS/PR/$[PR_ID]
 */
public class ViewPurchaseRequisition extends LockeState {

    public ViewPurchaseRequisition(PurchaseRequisition requisition) {
        super("Purchase Requisitions", "/ORDS/PR/$", false, true, false, true);

    }
}