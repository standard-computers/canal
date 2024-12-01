package org.Canal.UI.Views.Inventory;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * /STK/MOD/MV
 */
public class MoveStock extends JInternalFrame {

    public MoveStock() {
        super("Move Stock (Internal)", false, true, false, true);

        Form f = new Form();
        f.addInput(new Label("", Constants.colors[0]), Elements.input(""));
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton move = Elements.button("Move Stock");
        add(move, BorderLayout.SOUTH);
    }
}