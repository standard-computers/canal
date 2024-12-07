package org.Canal.UI.Views.Finance.CostCenters;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CCS/ARCHV
 */
public class ArchiveCostCenter extends LockeState {

    public ArchiveCostCenter() {
        super("Archive Cost Center", "/CCS/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}