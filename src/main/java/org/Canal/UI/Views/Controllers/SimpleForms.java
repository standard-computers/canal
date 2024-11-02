package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Labels;
import javax.swing.*;

/**
 * /CNL/SMPL_FRMS
 */
public class SimpleForms extends JInternalFrame {

    public SimpleForms() {
        setTitle("SimpleForms");
        setFrameIcon(new ImageIcon(SimpleForms.class.getResource("/icons/create.png")));

        JPanel titleBar = new JPanel();
        titleBar.setLayout(new BoxLayout(titleBar, BoxLayout.Y_AXIS));
        titleBar.add(Box.createVerticalGlue());
        titleBar.add(Labels.h3("SimpleForms"));


        setIconifiable(true);
        setClosable(true);
        setResizable(true);
        setMaximizable(true);
    }
}
