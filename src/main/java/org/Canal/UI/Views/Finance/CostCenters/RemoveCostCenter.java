package org.Canal.UI.Views.Finance.CostCenters;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CCS/DEL
 */
public class RemoveCostCenter extends LockeState {

    public RemoveCostCenter() {
        super("Remove Cost Center", "/CCS/DEL", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
    }
}