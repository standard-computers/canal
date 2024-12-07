package org.Canal.UI.Views.Distribution.Vendors;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /VEND/ARCHV
 */
public class ArchiveVendor extends LockeState {

    public ArchiveVendor() {
        super("Archive Vendor", "/VEND/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}