package org.Canal.UI.Views.Distribution.DistributionCenters;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /DCSS/MOD
 */
public class ModifyDistributionCenter extends LockeState {

    public ModifyDistributionCenter(Location distributionCenter) {
        super(distributionCenter.getId() + " - " + distributionCenter.getName(), "/DCSS/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyDistributionCenter.class.getResource("/icons/modify.png")));
    }
}