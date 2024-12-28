package org.Canal.UI.Views.SimpleForms;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;

/**
 * /CNL/SMPL_FRMS/NEW
 */
public class CreateSimpleForm extends LockeState {

    public CreateSimpleForm() {

        super("New SimpleForm", "/CNL/CMPL_FRMS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateSimpleForm.class.getResource("/icons/create.png")));

    }
}