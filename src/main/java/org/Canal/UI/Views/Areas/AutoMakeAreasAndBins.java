package org.Canal.UI.Views.Areas;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.Constants;

import javax.swing.*;

/**
 * /AREAS/AUTO_MK
 */
public class AutoMakeAreasAndBins extends LockeState {

    public AutoMakeAreasAndBins() {
        super("AutoMake Areas & Bins", "/AREAS/AUTO_MK", false, true, false, true);
        setFrameIcon(new ImageIcon(AutoMakeAreasAndBins.class.getResource("/icons/automake.png")));
        Selectable locations = Selectables.allLocations();
        Form f = new Form();
        f.addInput(new Label("Location", Constants.colors[0]), locations);
        f.addInput(new Label("Bins Per Area", Constants.colors[1]), Elements.input("100", 10));
        add(f);
    }
}