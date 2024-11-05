package org.Canal.UI.Views.Orders.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;

import javax.swing.*;

/**
 * /ORDS/PR/$[PR_ID]
 */
public class PurchaseReqView extends JInternalFrame {

    public PurchaseReqView(PurchaseRequisition requisition) {
        super("Purchase Requisitions", false, true, false, true);

    }
}