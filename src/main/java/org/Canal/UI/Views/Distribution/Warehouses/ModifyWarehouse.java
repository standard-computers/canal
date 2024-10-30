package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.Models.SupplyChainUnits.Warehouse;
import javax.swing.*;

/**
 * /WHS/MOD
 */
public class ModifyWarehouse extends JInternalFrame {

    public ModifyWarehouse(Warehouse warehouse) {
        setTitle(warehouse.getId() + " - " + warehouse.getName());

        setIconifiable(true);
        setClosable(true);
    }
}