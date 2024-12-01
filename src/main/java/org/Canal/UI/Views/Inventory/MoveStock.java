package org.Canal.UI.Views.Inventory;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
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
        super("/STK/MOD/MV", false, true, false, true);
        Form f = new Form();
        Selectable locations = Selectables.allLocations();
        Selectable areas = Selectables.areas();
        Selectable sourceBins = Selectables.areas();
        Selectable destinationBins = Selectables.areas();
        f.addInput(new Label("Location", UIManager.getColor("Label.foreground")), locations);
        f.addInput(new Label("Area", UIManager.getColor("Label.foreground")), areas);
        f.addInput(new Label("Source Bin", Constants.colors[6]), sourceBins);
        f.addInput(new Label("Destination Bin", Constants.colors[0]), destinationBins);
        setLayout(new BorderLayout());
        add(Elements.header("Move Stock Internally", SwingConstants.LEFT), BorderLayout.NORTH);
        add(f, BorderLayout.CENTER);
        JButton move = Elements.button("Move Stock");
        add(move, BorderLayout.SOUTH);
    }
}