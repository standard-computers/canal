package org.Canal.UI.Views.Productivity.SimpleForms;

import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /CNL/SMPL_FRMS/DEL
 */
public class RemoveSimpleForm extends LockeState {

    public RemoveSimpleForm() {
        super("Delete SimpleForm", "/CNL/SMPL_FRMS/DEL", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
    }
}