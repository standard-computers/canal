package org.Canal.UI.Views.Materials;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /MTS/MOD
 */
public class ModifyMaterial extends LockeState {

    public ModifyMaterial() {
        super("Modify Material", "/MTS/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyMaterial.class.getResource("/icons/modify.png")));
    }
}