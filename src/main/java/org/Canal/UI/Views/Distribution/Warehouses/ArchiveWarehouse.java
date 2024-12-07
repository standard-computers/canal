package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /WHS/ARCHV
 */
public class ArchiveWarehouse extends LockeState {

    public ArchiveWarehouse() {
        super("Archive Warehouse", "/WHS/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}