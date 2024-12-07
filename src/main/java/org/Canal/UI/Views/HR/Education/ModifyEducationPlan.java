package org.Canal.UI.Views.HR.Education;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /HR/EDU/PLNS/MOD
 */
public class ModifyEducationPlan extends LockeState {

    public ModifyEducationPlan() {
        super("Modify Education Plan", "/HR/EDU/PLNS/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyEducationPlan.class.getResource("/icons/modify.png")));
    }
}