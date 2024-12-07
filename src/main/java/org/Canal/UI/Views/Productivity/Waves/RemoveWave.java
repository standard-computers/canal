package org.Canal.UI.Views.Productivity.Waves;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /MVMT/WVS/DEL
 */
public class RemoveWave extends LockeState {

    public RemoveWave() {
        super("Remove Wave", "/MVMT/WVS/DEL", true, true, true, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
    }
}
