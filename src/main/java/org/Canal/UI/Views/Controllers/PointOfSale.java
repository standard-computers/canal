package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.LockeState;

/**
 * /POS
 */
public class PointOfSale extends LockeState {

    public PointOfSale() {

        super("Canal POS", "/POS", true, true, true, true);

        setMaximized(true);
    }
}