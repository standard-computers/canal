package org.Canal.UI.Views.Areas;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /AREAS/MOD
 */
public class ModifyArea extends LockeState {

    public ModifyArea() {
        super("Modify Area", "/AREAS/MOD", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.YELLOW));
    }
}