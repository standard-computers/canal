package org.Canal.UI.Views.Productivity.Tasks;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Windows.Form;

import javax.swing.*;
import java.awt.*;

/**
 * /MVMT/TSKS/NEW
 */
public class CreateTask extends JInternalFrame {

    public CreateTask() {
        super("Create Task", false, true, false, true);
        Form f = new Form();

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton create = Elements.button("Create Task");
        add(create, BorderLayout.SOUTH);
    }
}
