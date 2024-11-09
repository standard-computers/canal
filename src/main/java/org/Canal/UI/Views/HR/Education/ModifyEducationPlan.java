package org.Canal.UI.Views.HR.Education;

import javax.swing.*;
import java.awt.*;

public class ModifyEducationPlan extends JInternalFrame {

    public ModifyEducationPlan() {
        super("Modify Education Plan");
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyEducationPlan.class.getResource("/icons/modify.png")));
    }
}