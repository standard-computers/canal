package org.Canal.UI.Views.Distribution;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /WHS/MOD
 */
public class ModifyWarehouse extends LockeState {

    public ModifyWarehouse(Location warehouse) {
        super(warehouse.getId() + " - " + warehouse.getName(), "/WHS/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.YELLOW));
        setFrameIcon(new ImageIcon(ModifyWarehouse.class.getResource("/icons/modify.png")));
    }
}