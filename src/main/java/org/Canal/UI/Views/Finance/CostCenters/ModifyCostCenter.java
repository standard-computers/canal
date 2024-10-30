package org.Canal.UI.Views.Finance.CostCenters;

import org.Canal.Models.SupplyChainUnits.Location;
import javax.swing.*;

/**
 * /CCS/MOD
 */
public class ModifyCostCenter extends JInternalFrame {

    public ModifyCostCenter(Location costCenter) {
        setTitle(costCenter.getId() + " - " + costCenter.getName());

        setIconifiable(true);
        setClosable(true);
    }
}