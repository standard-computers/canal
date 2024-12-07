package org.Canal.UI.Views.Distribution.DistributionCenters;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /DCSS/ARCHV
 */
public class ArchiveDistributionCenter extends LockeState {

    public ArchiveDistributionCenter() {
        super("Archive Distribution Center", "/DCSS/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}