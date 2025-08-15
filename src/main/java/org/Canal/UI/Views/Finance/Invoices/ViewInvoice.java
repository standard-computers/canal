package org.Canal.UI.Views.Finance.Invoices;

import org.Canal.UI.Elements.LockeState;

/**
 * /INVS/$[INVOICE_ID]
 */
public class ViewInvoice extends LockeState {

    public ViewInvoice() {

        super("View Invoice", "/INVS/$", false, true, false, true);

    }
}
