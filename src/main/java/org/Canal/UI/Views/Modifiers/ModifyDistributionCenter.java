package org.Canal.UI.Views.Modifiers;

import org.Canal.Models.SupplyChainUnits.Location;
import javax.swing.*;

/**
 * /DCSS/MOD
 */
public class ModifyDistributionCenter extends JInternalFrame {

    public ModifyDistributionCenter(Location distributionCenter) {
        setTitle(distributionCenter.getId() + " - " + distributionCenter.getName());

        setIconifiable(true);
        setClosable(true);
    }
}