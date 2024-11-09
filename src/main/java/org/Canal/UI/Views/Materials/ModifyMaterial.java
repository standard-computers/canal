package org.Canal.UI.Views.Materials;

import javax.swing.*;
import java.awt.*;

/**
 * /MTS/MOD
 */
public class ModifyMaterial extends JInternalFrame {

    public ModifyMaterial() {
        super("Modify Material", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyMaterial.class.getResource("/icons/modify.png")));
    }
}