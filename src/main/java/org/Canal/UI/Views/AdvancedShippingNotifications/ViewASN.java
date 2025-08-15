package org.Canal.UI.Views.AdvancedShippingNotifications;

import org.Canal.UI.Elements.LockeState;

/**
 * /TRANS/ASN/$[ASN_ID || #]
 * View an Advnanced Shipping Notice from a Vendor with Carrier and Truck
 */
public class ViewASN extends LockeState {

    public ViewASN() {
        super("View ASN", "/TRANS/ASN/$", false, true, false, true);
        
    }
}