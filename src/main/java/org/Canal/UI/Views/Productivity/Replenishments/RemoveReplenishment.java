package org.Canal.UI.Views.Productivity.Replenishments;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /RPL
 */
public class RemoveReplenishment extends LockeState {

    public RemoveReplenishment() {
        super("Replenishments", "/RPL", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
    }
}