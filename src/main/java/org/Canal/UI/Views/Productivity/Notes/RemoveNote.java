package org.Canal.UI.Views.Productivity.Notes;

import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /NTS/DEL
 */
public class RemoveNote extends LockeState {

    public RemoveNote() {
        super("Delete Note", "/NTS/DEL", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
    }
}