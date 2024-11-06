package org.Canal.UI.Views.Distribution.DistributionCenters;

import org.Canal.Models.SupplyChainUnits.Location;

import javax.swing.*;
import java.awt.*;

/**
 * /DCSS/MOD
 */
public class ModifyDistributionCenter extends JInternalFrame {

    public ModifyDistributionCenter(Location distributionCenter) {
        super(distributionCenter.getId() + " - " + distributionCenter.getName(), false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.YELLOW));
    }
}