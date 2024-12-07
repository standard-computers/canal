package org.Canal.UI.Views.Distribution.DistributionCenters;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /DCSS/DEL
 * Delete a Distribution Center with Dis. Center ID
 */
public class RemoveDistributionCenter extends LockeState {

    public RemoveDistributionCenter() {
        super("Remove Distribution Center", "/DCSS/DEL", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
    }
}