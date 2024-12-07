package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /WHS/DEL
 */
public class RemoveWarehouse extends LockeState {

    public RemoveWarehouse() {
        super("Remove Warehouse", "/WHS/DEL", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
    }
}