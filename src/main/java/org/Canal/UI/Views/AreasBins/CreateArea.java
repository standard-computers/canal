package org.Canal.UI.Views.AreasBins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.UOMField;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /AREAS/NEW
 */
public class CreateArea extends JInternalFrame {

    public CreateArea(String location) {
        super("New Location Area", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateArea.class.getResource("/icons/areas.png")));
        String generatedId;
        JTextField areaIdField = new JTextField();
        Selectable availableLocations = Selectables.allLocations();
        if(location == null){
            generatedId = "A-" + Engine.getAreas().size();
        }else{
            generatedId = "A-" + (Engine.getAreas(location).size() + 1) + "-" + location;
            availableLocations.setSelectedValue(location);
        }
        areaIdField.setText(generatedId);
        JTextField areaNameField = new JTextField(generatedId, 20);
        UOMField widthField = new UOMField();
        UOMField lengthField = new UOMField();
        UOMField heightField = new UOMField();
        UOMField areaField = new UOMField();
        UOMField volumeField = new UOMField();
        Form f = new Form();
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), areaIdField);
        f.addInput(new Label("*Location", UIManager.getColor("Label.foreground")), availableLocations);
        f.addInput(new Label("Area Name", Constants.colors[10]), areaNameField);
        f.addInput(new Label("Width", Constants.colors[9]), widthField);
        f.addInput(new Label("Length", Constants.colors[8]), lengthField);
        f.addInput(new Label("Height", Constants.colors[7]), heightField);
        f.addInput(new Label("Area", Constants.colors[6]), areaField);
        f.addInput(new Label("Volume", Constants.colors[6]), volumeField);

        Button addBins = new Button("Add Bins");
        Button make = new Button("Make");
        JPanel areaOptions = new JPanel();
        areaOptions.add(addBins);
        areaOptions.add(make);

        add(areaOptions, BorderLayout.SOUTH);
        JPanel main = new JPanel(new GridLayout(1, 2));
        main.add(f);
        JPanel binner = new JPanel();

        main.add(binner);
        add(main, BorderLayout.CENTER);
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
            }
        });
    }
}