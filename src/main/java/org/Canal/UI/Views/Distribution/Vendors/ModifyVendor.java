package org.Canal.UI.Views.Distribution.Vendors;

import org.Canal.Models.SupplyChainUnits.Warehouse;
import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /VEND/MOD
 */
public class ModifyVendor extends LockeState {

    public ModifyVendor(Warehouse warehouse) {
        super(warehouse.getId() + " - " + warehouse.getName(), "/VEND/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyVendor.class.getResource("/icons/modify.png")));
    }
}