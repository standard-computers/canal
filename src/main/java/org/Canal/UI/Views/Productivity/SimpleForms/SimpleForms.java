package org.Canal.UI.Views.Productivity.SimpleForms;

import org.Canal.UI.Elements.Elements;

import javax.swing.*;
import java.awt.*;

/**
 * /CNL/SMPL_FRMS
 */
public class SimpleForms extends JInternalFrame {

    public SimpleForms() {
        super("SimpleForms", true, true, true, true);
        setFrameIcon(new ImageIcon(SimpleForms.class.getResource("/icons/create.png")));
        JPanel titleBar = Elements.header("SimpleForms");
        setLayout(new BorderLayout());
        add(titleBar, BorderLayout.NORTH);
    }
}
