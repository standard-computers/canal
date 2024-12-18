package org.Canal.UI.Views.HR.Teams;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /TMS/MOD
 */
public class ModifyTeam extends LockeState {

    public ModifyTeam() {
        super("Modify Team", "/TMS/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyTeam.class.getResource("/icons/modify.png")));
    }
}