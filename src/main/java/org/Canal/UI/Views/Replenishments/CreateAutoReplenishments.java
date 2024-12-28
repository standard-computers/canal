package org.Canal.UI.Views.Replenishments;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;

/**
 * /RPL/AUTO/NEW
 */
public class CreateAutoReplenishments extends LockeState {

    public CreateAutoReplenishments() {

        super("Replenishments", "/RPL/AUTO/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateAutoReplenishments.class.getResource("/icons/create.png")));

    }
}