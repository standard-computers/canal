package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
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
        JTextField binIdField = Elements.input();
        HashMap<String, String> areas = new HashMap<>();
        for(Area a : Engine.getAreas(location)){
            areas.put(a.getName(), a.getId());
        }
        Selectable binAreaId = new Selectable(areas);
        generatedId = "BN" + (Engine.getAreas(location).size() + 1) + "-" + binAreaId.getSelectedValue() + "-" + location;
        binAreaId.setSelectedValue(location);
        binIdField.setText(generatedId);
        JTextField binNameField = Elements.input(generatedId, 20);
        UOMField widthField = new UOMField();
        UOMField lengthField = new UOMField();
        UOMField heightField = new UOMField();
        UOMField areaName = new UOMField();
        Button make = new Button("Make");

        Form f = new Form();
        f.addInput(new Label("*New Bin ID", UIManager.getColor("Label.foreground")), binIdField);
        f.addInput(new Label("*Area", UIManager.getColor("Label.foreground")), binAreaId);
        f.addInput(new Label("Bin Name", Constants.colors[10]), binNameField);
        f.addInput(new Label("Width", Constants.colors[9]), widthField);
        f.addInput(new Label("Length", Constants.colors[8]), lengthField);
        f.addInput(new Label("Height", Constants.colors[7]), heightField);
        f.addInput(new Label("Volume", Constants.colors[7]), areaName);
        f.addInput(new Label("Min Qty", UIManager.getColor("Label.foreground")), Elements.input(5));
        f.addInput(new Label("Max Qty", UIManager.getColor("Label.foreground")), Elements.input(5));
        JCheckBox autoReplenish = new JCheckBox("Auto Replenish");
        autoReplenish.setToolTipText("Bin will be automatically replenished based on set replenishments");
        JCheckBox fixedBin = new JCheckBox("Fixed Bin");
        fixedBin.setToolTipText("Bin can only contain one Item ID");
        f.addInput(autoReplenish, fixedBin);
        add(make, BorderLayout.SOUTH);
        add(f, BorderLayout.CENTER);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String binId = binIdField.getText();
                String binName = binNameField.getText();
                String binArea = binAreaId.getSelectedValue();
                double binWidth = Double.parseDouble(widthField.getValue());
                String binWidthUom = widthField.getUOM();
                double binLength = Double.parseDouble(lengthField.getValue());
                String binLengthUom = lengthField.getUOM();
                double binHeight = Double.parseDouble(heightField.getValue());
                String binHeightUom = heightField.getUOM();
                double binVolume = Double.parseDouble(areaName.getValue());
                String binVolumeUom = areaName.getUOM();
                Bin newBin = new Bin();
                newBin.setId(binId);
                newBin.setName(binName);
                newBin.setArea(binArea);
                newBin.setAuto_replenish(autoReplenish.isSelected());
                newBin.setFixed(fixedBin.isSelected());
                newBin.setWidth(binWidth);
                newBin.setWidthUOM(binWidthUom);
                newBin.setLength(binLength);
                newBin.setLengthUOM(binLengthUom);
                newBin.setHeight(binHeight);
                newBin.setHeightUOM(binHeightUom);
                newBin.setVolume(binVolume);
                newBin.setVolumeUOM(binVolumeUom);
                Area foundArea = Engine.realtime.getArea(binArea);
                if(foundArea != null){
                    foundArea.addBin(newBin);
                    foundArea.save();
                    dispose();
                    JOptionPane.showMessageDialog(null, "Bin '" + binName + "' created in '" + binArea + "'");
                }else{
                    JOptionPane.showMessageDialog(null, "Bin '" + binName + "' could not be created in '" + binArea + "'");
                }
            }
        });
    }
}