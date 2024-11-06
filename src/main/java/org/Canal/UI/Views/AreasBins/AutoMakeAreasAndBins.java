package org.Canal.UI.Views.AreasBins;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Warehouse;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * /AREAS/AUTO_MK
 */
public class AutoMakeAreasAndBins extends JInternalFrame {

    public AutoMakeAreasAndBins() {
        super("Area/Bin AutoMake", false, true, false, true);

        HashMap<String, String> als = new HashMap<>();
        for(Location l : Engine.getCostCenters()){
            als.put(l.getId(), l.getId());
        }
        for(Location l : Engine.getDistributionCenters()){
            als.put(l.getId(), l.getId());
        }
        for(Warehouse l : Engine.getWarehouses()){
            als.put(l.getId(), l.getId());
        }
        Selectable locations = new Selectable(als);
        Form f = new Form();
        f.addInput(new Label("Location", Constants.colors[0]), locations);
        f.addInput(new Label("Bins Per Area", Constants.colors[1]), new JTextField("100", 10));
        add(f);
    }

    private JPanel prepareAreas() {
        ArrayList<JCheckBox> checkboxes = new ArrayList<>();
        ArrayList<String> allTransactions = new ArrayList<>();
        allTransactions.add("");
        for (String transaction : allTransactions) {
            JCheckBox checkbox = new JCheckBox(transaction);
            checkboxes.add(checkbox);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (JCheckBox checkbox : checkboxes) {
            panel.add(checkbox);
        }
        return panel;
    }
}