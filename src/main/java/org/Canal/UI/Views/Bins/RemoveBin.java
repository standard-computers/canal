package org.Canal.UI.Views.Bins;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /BNS/DEL
 */
public class RemoveBin extends LockeState {

    public RemoveBin() {
        super("Remove Bin", "/BNS/DEL", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
    }
}