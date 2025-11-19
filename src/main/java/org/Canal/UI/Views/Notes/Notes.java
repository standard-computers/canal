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
        super("Notes", "/NTS"); //TODO Notes?
        JPanel titleBar = Elements.header("SimpleForms");
        setLayout(new BorderLayout());
        add(titleBar, BorderLayout.NORTH);
    }
}
