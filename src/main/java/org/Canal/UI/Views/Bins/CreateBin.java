package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * /BNS/NEW
 */
public class CreateBin extends LockeState {

    public CreateBin(String location, RefreshListener refreshListener) {

        super("New Bin", "/BNS/NEW", false, true, false, true);
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
        JButton make = Elements.button("Make");

        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New Bin ID", UIManager.getColor("Label.foreground")), binIdField);
        f.addInput(Elements.coloredLabel("*Area", UIManager.getColor("Label.foreground")), binAreaId);
        f.addInput(Elements.coloredLabel("Bin Name", Constants.colors[10]), binNameField);
        f.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[8]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[7]), heightField);
        f.addInput(Elements.coloredLabel("Volume", Constants.colors[7]), areaName);
        f.addInput(Elements.coloredLabel("Min Qty", UIManager.getColor("Label.foreground")), Elements.input(5));
        f.addInput(Elements.coloredLabel("Max Qty", UIManager.getColor("Label.foreground")), Elements.input(5));
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
                Area foundArea = Engine.getArea(binArea);
                if(foundArea != null){
                    foundArea.addBin(newBin);
                    foundArea.save();
                    dispose();
                    JOptionPane.showMessageDialog(null, "Bin '" + binName + "' created in '" + binArea + "'");
                    refreshListener.onRefresh();
                }else{
                    JOptionPane.showMessageDialog(null, "Bin '" + binName + "' could not be created in '" + binArea + "'");
                }
            }
        });
    }
}