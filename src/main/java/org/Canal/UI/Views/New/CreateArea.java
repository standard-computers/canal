package org.Canal.UI.Views.New;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Warehouse;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * /AREAS/NEW
 */
public class CreateArea extends JInternalFrame {

    public CreateArea(DesktopState desktop, Location location) {
        setTitle("New Location Area");
        setFrameIcon(new ImageIcon(CreateArea.class.getResource("/icons/areas.png")));
        String generatedId;
        JTextField areaIdField = new JTextField();
        HashMap<String, String> locations = new HashMap<>();
        for(Location l : Engine.getCostCenters()){
            locations.put(l.getId() + " - " + l.getName(), l.getId());
        }
        for(Location l : Engine.getDistributionCenters()){
            locations.put(l.getId() + " - " + l.getName(), l.getId());
        }
        for(Warehouse l : Engine.getWarehouses()){
            locations.put(l.getId() + " - " + l.getName(), l.getId());
        }
        Selectable locationIdField = new Selectable(locations);
        if(location == null){
            generatedId = "A-" + Engine.getAreas().size();
        }else{
            generatedId = "A-" + (Engine.getAreas(location.getId()).size() + 1) + "-" + location.getId();
            locationIdField.setSelectedValue(location.getId());
        }
        areaIdField.setText(generatedId);
        JTextField areaNameField = new JTextField(20);
        JTextField widthField = new JTextField(20);
        JTextField lengthField = new JTextField(20);
        JTextField heightField = new JTextField(20);
        JTextField areaName = new JTextField(20);
        Button make = new Button("Make");
        Form f = new Form();
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), areaIdField);
        f.addInput(new Label("*Location", UIManager.getColor("Label.foreground")), locationIdField);
        f.addInput(new Label("Area Name", Constants.colors[10]), areaNameField);
        f.addInput(new Label("Width", Constants.colors[9]), widthField);
        f.addInput(new Label("Length", Constants.colors[8]), lengthField);
        f.addInput(new Label("Height", Constants.colors[7]), heightField);
        f.addInput(new Label("Area", Constants.colors[7]), areaName);
        add(make, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(make);
        JPanel main = new JPanel(new GridLayout(1, 2));
        main.add(f);
        JPanel binner = new JPanel();
        main.add(binner);
        add(main, BorderLayout.CENTER);
        setResizable(false);
        setIconifiable(true);
        setClosable(true);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
    }
}