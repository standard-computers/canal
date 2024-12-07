package org.Canal.UI.Views.Transportation.AdvancedShippingNotifications;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /TRANS/ASN/ARCHV
 */
public class ArchiveASN extends LockeState {

    public ArchiveASN() {
        super("Archive ASN", "/TRANS/ASN/ARCHV", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
    }
}