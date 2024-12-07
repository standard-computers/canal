package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.Models.SupplyChainUnits.Warehouse;
import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /WHS/MOD
 */
public class ModifyWarehouse extends LockeState {

    public ModifyWarehouse(Warehouse warehouse) {
        super(warehouse.getId() + " - " + warehouse.getName(), "/WHS/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.YELLOW));
        setFrameIcon(new ImageIcon(ModifyWarehouse.class.getResource("/icons/modify.png")));
    }
}