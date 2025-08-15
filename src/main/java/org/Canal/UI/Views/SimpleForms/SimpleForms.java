package org.Canal.UI.Views.SimpleForms;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /SMPL_FRMS
 */
public class SimpleForms extends LockeState {

    public SimpleForms() {
        super("SimpleForms", "/SMPL_FRMS", true, true, true, true);
        setFrameIcon(new ImageIcon(SimpleForms.class.getResource("/icons/create.png")));
        JPanel titleBar = Elements.header("SimpleForms");
        setLayout(new BorderLayout());
        add(titleBar, BorderLayout.NORTH);
    }
}
