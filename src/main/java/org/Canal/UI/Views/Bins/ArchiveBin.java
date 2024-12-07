package org.Canal.UI.Views.Bins;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /BNS/ARCHV
 */
public class ArchiveBin extends LockeState {

    public ArchiveBin() {
        super("Archive Bin", "/BNS/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}