package org.Canal.UI.Views.Productivity.Notes;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;

/**
 * /NTS/MOD
 */
public class ModifyNote extends LockeState {

    public ModifyNote() {
        super("Modify Note", "/NTS/MOD", false, true, false, true);
        setFrameIcon(new ImageIcon(ModifyNote.class.getResource("/icons/modify.png")));
    }
}