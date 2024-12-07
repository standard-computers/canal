package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CATS/ARCHV
 */
public class ArchiveCatalog extends LockeState {

    public ArchiveCatalog() {
        super("Archive Catalog", "/CATS/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}