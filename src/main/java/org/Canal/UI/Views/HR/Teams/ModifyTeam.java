package org.Canal.UI.Views.HR.Teams;

import javax.swing.*;
import java.awt.*;

/**
 * /TMS/MOD
 */
public class ModifyTeam extends JInternalFrame {

    public ModifyTeam() {
        super("Modify Team", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyTeam.class.getResource("/icons/modify.png")));
    }
}