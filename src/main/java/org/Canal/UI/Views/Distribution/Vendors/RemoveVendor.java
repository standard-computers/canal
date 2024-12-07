package org.Canal.UI.Views.Distribution.Vendors;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /VEND/DEL
 */
public class RemoveVendor extends LockeState {

    public RemoveVendor() {
        super("Remove Vendor", "/VEND/DEL", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
    }
}