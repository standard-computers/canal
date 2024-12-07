package org.Canal.UI.Views.Areas;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /AREAS/ARCHV
 */
public class ArchiveArea extends LockeState {

    public ArchiveArea() {
        super("Archive Area", "/AREAS/ARCHV", false, true, false, true);

        setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
    }
}