package org.Canal.UI.Views.Transportation.AdvancedShippingNotifications;

import org.Canal.UI.Elements.LockeState;

/**
 * /TRANS/ASN/$[ASN_ID || #]
 * View an Advnanced Shipping Notice from a Vendor with Carrier and Truck
 */
public class ASNView extends LockeState {

    public ASNView() {
        super("View ASN", "/TRANS/ASN/$", false, true, false, true);
        
    }
}