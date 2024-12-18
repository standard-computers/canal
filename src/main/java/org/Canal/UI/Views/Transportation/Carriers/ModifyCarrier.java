package org.Canal.UI.Views.Transportation.Carriers;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /TRANS/CRRS/MOD
 */
public class ModifyCarrier extends LockeState {

    public ModifyCarrier() {
        super("Modify Carrier", "/TRANS/CRRS/MOD", false, true, false, true);
        setFrameIcon(new ImageIcon(ModifyCarrier.class.getResource("/icons/modify.png")));
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.YELLOW));
    }
}