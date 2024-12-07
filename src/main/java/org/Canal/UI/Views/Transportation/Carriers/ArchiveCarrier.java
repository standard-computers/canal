package org.Canal.UI.Views.Transportation.Carriers;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /TRANS/CRRS/ARCHV
 */
public class ArchiveCarrier extends LockeState {

    public ArchiveCarrier() {
        super("Archive Carrier", "/TRANS/CRRS/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
    }
}