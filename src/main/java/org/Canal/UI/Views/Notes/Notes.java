package org.Canal.UI.Views.Notes;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /NOTES
 */
public class Notes extends LockeState {

    public Notes() {
        super("Notes", "/", true, true, true, true);
        setFrameIcon(new ImageIcon(Notes.class.getResource("/icons/create.png")));
        JPanel titleBar = Elements.header("SimpleForms");
        setLayout(new BorderLayout());
        add(titleBar, BorderLayout.NORTH);
    }
}
