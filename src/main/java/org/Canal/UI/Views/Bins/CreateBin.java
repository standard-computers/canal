package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.UOMField;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * /BNS/NEW
 */
public class CreateBin extends JInternalFrame {

    public CreateBin(String location) {
        super("New Area Bin", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateBin.class.getResource("/icons/bins.png")));
        String generatedId;
        JTextField areaIdField = Elements.input();
        HashMap<String, String> areas = new HashMap<>();
        for(Area a : Engine.getAreas(location)){
            areas.put(a.getName(), a.getId());
        }
        Selectable binAreaId = new Selectable(areas);
        generatedId = "BN-" + (Engine.getAreas(location).size() + 1) + "-" + location;
        binAreaId.setSelectedValue(location);
        areaIdField.setText(generatedId);
        JTextField areaNameField = Elements.input(generatedId, 20);
        JPanel widthField = new UOMField();
        JPanel lengthField = new UOMField();
        JPanel heightField = new UOMField();
        JPanel areaName = new UOMField();
        Button make = new Button("Make");

        Form f = new Form();
        f.addInput(new Label("*New Bin ID", UIManager.getColor("Label.foreground")), areaIdField);
        f.addInput(new Label("*Location", UIManager.getColor("Label.foreground")), binAreaId);
        f.addInput(new Label("Bin Name", Constants.colors[10]), areaNameField);
        f.addInput(new Label("Width", Constants.colors[9]), widthField);
        f.addInput(new Label("Length", Constants.colors[8]), lengthField);
        f.addInput(new Label("Height", Constants.colors[7]), heightField);
        f.addInput(new Label("Volume", Constants.colors[7]), areaName);

        add(make, BorderLayout.SOUTH);
        add(f, BorderLayout.CENTER);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }
}