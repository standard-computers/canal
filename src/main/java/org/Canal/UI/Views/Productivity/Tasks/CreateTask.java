package org.Canal.UI.Views.Productivity.Tasks;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /MVMT/TSKS/NEW
 */
public class CreateTask extends LockeState {

    public CreateTask() {
        super("Create Task", "/MVMT/TSKS/NEW", false, true, false, true);
        Form f = new Form();

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton create = Elements.button("Create Task");
        add(create, BorderLayout.SOUTH);
    }
}
