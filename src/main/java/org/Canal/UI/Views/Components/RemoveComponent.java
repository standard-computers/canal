package org.Canal.UI.Views.Components;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CMPS/DEL
 */
public class RemoveComponent extends LockeState {

    public RemoveComponent() {
        super("Remove Component", "/CMPS/DEL", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
    }
}