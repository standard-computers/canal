package org.Canal.UI.Views.Transportation.Carriers;

import javax.swing.*;
import java.awt.*;

/**
 * /TRANS/CRRS/MOD
 */
public class ModifyCarrier extends JInternalFrame {

    public ModifyCarrier() {
        super("Modify Carrier", false, true, false, true);
        setFrameIcon(new ImageIcon(ModifyCarrier.class.getResource("/icons/modify.png")));
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
    }
}