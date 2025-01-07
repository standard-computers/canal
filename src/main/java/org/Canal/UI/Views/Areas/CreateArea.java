package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.UOMField;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /AREAS/NEW
 * Create a new Area for a location
 */
public class CreateArea extends LockeState {

    public CreateArea(String location, RefreshListener refreshListener) {

        super("New Area", "/AREAS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateArea.class.getResource("/icons/areas.png")));

        JTextField areaIdField = Elements.input();
        Selectable availableLocations = Selectables.allLocations();
        if(location == null){
            areaIdField.setText("A-" + Engine.getAreas().size());
        }else{
            areaIdField.setText("A-" + (Engine.getAreas(location).size() + 1) + "-" + location);
            availableLocations.setSelectedValue(location);
        }
        JTextField areaNameField = Elements.input(areaIdField.getText(), 20);
        UOMField widthField = new UOMField();
        UOMField lengthField = new UOMField();
        UOMField heightField = new UOMField();
        UOMField areaField = new UOMField();
        UOMField volumeField = new UOMField();
        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), areaIdField);
        f.addInput(Elements.coloredLabel("*Location", UIManager.getColor("Label.foreground")), availableLocations);
        f.addInput(Elements.coloredLabel("Area Name", Constants.colors[10]), areaNameField);
        f.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[8]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[7]), heightField);
        f.addInput(Elements.coloredLabel("Area", Constants.colors[6]), areaField);
        f.addInput(Elements.coloredLabel("Volume", Constants.colors[6]), volumeField);

        JButton make = Elements.button("Make");
        add(make, BorderLayout.SOUTH);
        add(f, BorderLayout.CENTER);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Area newArea = new Area();
                newArea.setId(areaIdField.getText().trim());
                newArea.setLocation(availableLocations.getSelectedValue());
                newArea.setName(areaNameField.getText());
                newArea.setWidth(Double.parseDouble(widthField.getValue()));
                newArea.setWidthUOM(widthField.getUOM());
                newArea.setLength(Double.parseDouble(lengthField.getValue()));
                newArea.setLengthUOM(lengthField.getUOM());
                newArea.setHeight(Double.parseDouble(heightField.getValue()));
                newArea.setHeightUOM(heightField.getUOM());
                newArea.setArea(Double.parseDouble(areaField.getValue()));
                newArea.setAreaUOM(areaField.getUOM());
                newArea.setVolume(Double.parseDouble(volumeField.getValue()));
                newArea.setVolumeUOM(volumeField.getUOM());
                Pipe.save("/AREAS", newArea);
                dispose();
                JOptionPane.showMessageDialog(CreateArea.this, "Area Created");
                refreshListener.onRefresh();
            }
        });
    }
}