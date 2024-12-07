package org.Canal.UI.Views.Components;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CMPS/MOD
 */
public class ModifyComponent extends LockeState {

    public ModifyComponent() {
        super("Modify Component", "/CMPS/MOD", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.YELLOW));
    }
}
