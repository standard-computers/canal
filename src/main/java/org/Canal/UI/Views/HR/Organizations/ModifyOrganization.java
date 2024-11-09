package org.Canal.UI.Views.HR.Organizations;

import javax.swing.*;
import java.awt.*;

/**
 * /ORGS/MOD/?$[ORG_ID]
 */
public class ModifyOrganization extends JInternalFrame {

    public ModifyOrganization() {
        super("Modify Organization", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyOrganization.class.getResource("/icons/modify.png")));
    }
}