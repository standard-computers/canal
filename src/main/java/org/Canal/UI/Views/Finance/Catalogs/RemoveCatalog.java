package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CATS/DEL
 * Delete a Catalog with Catalog ID
 */
public class RemoveCatalog extends LockeState {

    public RemoveCatalog() {
        super("Remove Catalog", "/CATS/DEL", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
    }
}