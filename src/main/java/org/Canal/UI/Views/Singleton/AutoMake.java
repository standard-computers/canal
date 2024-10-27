package org.Canal.UI.Views.Singleton;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.FormFrame;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Selectable;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AutoMake extends FormFrame {

    public AutoMake() {
        setTitle("Area/Bin AutoMake");

        HashMap<String, String> als = new HashMap<>();
        for(Location l : Engine.getCostCenters()){
            als.put(l.getId(), l.getId());
        }
        for(Location l : Engine.getDistributionCenters()){
            als.put(l.getId(), l.getId());
        }
        for(Location l : Engine.getWarehouses()){
            als.put(l.getId(), l.getId());
        }
        Selectable locations = new Selectable(als);

        addInput(new Label("Location", Constants.colors[0]), locations);
        addInput(new Label("Bins Per Area", Constants.colors[1]), new JTextField("100", 10));
        add(prepareAreas());
        setVisible(true);
        pack();

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