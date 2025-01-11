package org.Canal.UI.Views.SimpleForms;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;

/**
 * /SMPL_FRMS/NEW
 */
public class CreateSimpleForm extends LockeState {

    public CreateSimpleForm() {

        super("New SimpleForm", "/CMPL_FRMS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateSimpleForm.class.getResource("/icons/create.png")));

    }
}