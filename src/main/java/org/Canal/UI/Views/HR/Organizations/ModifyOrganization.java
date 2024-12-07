package org.Canal.UI.Views.HR.Organizations;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /ORGS/MOD/$[ORG_ID]
 */
public class ModifyOrganization extends LockeState {

    public ModifyOrganization() {
        super("Modify Organization", "/ORGS/MODE/$", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyOrganization.class.getResource("/icons/modify.png")));
    }
}