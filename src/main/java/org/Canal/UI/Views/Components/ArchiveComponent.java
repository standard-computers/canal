package org.Canal.UI.Views.Components;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CMPS/ARCHV
 */
public class ArchiveComponent extends LockeState {

    public ArchiveComponent() {
        super("Archive Component", "/CMPS/ARCHV", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}