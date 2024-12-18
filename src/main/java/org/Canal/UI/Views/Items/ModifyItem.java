package org.Canal.UI.Views.Items;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /ITS/MOD
 */
public class ModifyItem extends LockeState {

    public ModifyItem() {
        super("Modify Item", "/", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyItem.class.getResource("/icons/modify.png")));
    }
}