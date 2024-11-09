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
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyDistributionCenter.class.getResource("/icons/modify.png")));
    }
}