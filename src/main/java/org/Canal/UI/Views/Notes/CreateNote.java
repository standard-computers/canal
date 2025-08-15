package org.Canal.UI.Views.Notes;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;

/**
 * /NOTES/NEW
 */
public class CreateNote extends LockeState {

    public CreateNote() {

        super("New Note", "/NOTES/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateNote.class.getResource("/icons/create.png")));

    }
}