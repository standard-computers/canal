package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;

/**
 * /CATS/$[CATALOG_ID]
 */
public class CatalogView extends LockeState {

    public CatalogView(Catalog catalog) {
        super("Catalog", "/CATS/$", true, true, true, true);
        setFrameIcon(new ImageIcon(CatalogView.class.getResource("/icons/catalogs.png")));
    }
}