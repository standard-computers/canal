package org.Canal.UI.Views.Distribution.Vendors;

import org.Canal.Models.SupplyChainUnits.Warehouse;

import javax.swing.*;

/**
 * /VEND/MOD
 */
public class ModifyVendor extends JInternalFrame {

    public ModifyVendor(Warehouse warehouse) {
        super(warehouse.getId() + " - " + warehouse.getName(), false, true, false, true);
    }
}