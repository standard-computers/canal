package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * /BNS/AUTO_MK
 */
public class AutoMakeBins extends LockeState {

    private JTextField binIdField, binNameField, binCount;
    private UOMField widthField, lengthField, heightField, areaField, volumeField;
    private JPanel checkboxPanel;
    private ArrayList<Area> areas;
    private ArrayList<JCheckBox> checkboxes;

    public AutoMakeBins() {
        super("AutoMake Bins", "/BNS/AUTO_MK", false, true, false, true);
        setFrameIcon(new ImageIcon(AutoMakeBins.class.getResource("/icons/automake.png")));
        JTabbedPane tabs = new JTabbedPane();
        areas = Engine.getAreas();
        areas.addAll(Engine.getAreas());
        this.checkboxes = new ArrayList<>();
        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        addCheckboxes();
        JScrollPane js = new JScrollPane(checkboxPanel);
        js.setPreferredSize(new Dimension(200, 200));
        JPanel selector = new JPanel(new BorderLayout());
        JTextField search = Elements.input();
        selector.add(search, BorderLayout.NORTH);
        selector.add(js, BorderLayout.CENTER);
        JPanel opts = new JPanel(new GridLayout(1, 2));
        JButton sa = Elements.button("Select All");
        JButton dsa = Elements.button("Deselect All");
        sa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(true));
                repaint();
            }
        });
        dsa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(false));
                repaint();
            }
        });
        opts.add(sa);
        opts.add(dsa);
        selector.add(opts, BorderLayout.SOUTH);
        tabs.add(selector, "Select Areas");
        tabs.addTab("Bin Information", binInformation());
        setLayout(new BorderLayout());
        add(Elements.header("AutoMake Bins", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        JButton automake = Elements.button("AutoMake");
        add(automake, BorderLayout.SOUTH);
        automake.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
                JOptionPane.showMessageDialog(AutoMakeBins.this, "Working...");
                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.isSelected()) {
                        Area a = Engine.getArea(checkbox.getActionCommand());
                        if(a != null){
                            for(int i = 1; i <= Integer.parseInt(binCount.getText()); i++){
                                Bin b = new Bin();
                                b.setId(binIdField.getText().trim()
                                        .replace("@", checkbox.getActionCommand().split("-")[0])
                                        .replace("+", String.valueOf(i)));
                                b.setName(binNameField.getText().trim()
                                        .replace("@", checkbox.getActionCommand().split("-")[0])
                                        .replace("+", String.valueOf(i)));
                                b.setArea(checkbox.getActionCommand());
                                b.setWidth(Double.parseDouble(widthField.getValue()));
                                b.setLength(Double.parseDouble(lengthField.getValue()));
                                b.setHeight(Double.parseDouble(heightField.getValue()));
                                b.setVolume(Double.parseDouble(volumeField.getValue()));
                                b.setWidthUOM(widthField.getUOM());
                                b.setLengthUOM(lengthField.getUOM());
                                b.setHeightUOM(heightField.getUOM());
                                b.setVolumeUOM(volumeField.getUOM());
                                a.addBin(b);
                            }
                        }
                        a.save();
                    }
                }
                JOptionPane.showMessageDialog(AutoMakeBins.this, "AutoMake Complete");
            }
        });
    }


    private JPanel binInformation(){
        JPanel p = new JPanel(new BorderLayout());
        Form f = new Form();
        binIdField = Elements.input("BN+-@", 10);
        binNameField = Elements.input("BIN+-@", 10);

        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();
        areaField = new UOMField();
        volumeField = new UOMField();

        binCount = Elements.input("1");
        f.addInput(Elements.coloredLabel("Bin ID (current: BN1-IBD1)", Constants.colors[10]), binIdField);
        f.addInput(Elements.coloredLabel("Bin Name (current: BIN1-IBD1)", Constants.colors[9]), binNameField);

        f.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[9]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[9]), heightField);
        f.addInput(Elements.coloredLabel("Area", Constants.colors[9]), areaField);
        f.addInput(Elements.coloredLabel("Volume", Constants.colors[9]), volumeField);
        lengthField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                double area = Double.parseDouble(widthField.getValue()) * Double.parseDouble(lengthField.getValue());
                areaField.setValue(String.valueOf(area));
            }
        });
        heightField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                double volume = Double.parseDouble(widthField.getValue()) * Double.parseDouble(lengthField.getValue()) * Double.parseDouble(heightField.getValue());
                volumeField.setValue(String.valueOf(volume));
            }
        });
        f.addInput(Elements.coloredLabel("Bin Create Count", Constants.colors[8]), binCount);
        p.add(f, BorderLayout.CENTER);
        p.add(Elements.link("Need Help?", "@ is the Area ID, + is Bin #, "), BorderLayout.SOUTH);
        return p;
    }

    private void addCheckboxes() {
        Set<String> addedIds = new HashSet<>(); // To track unique IDs
        for (Area location : areas) {
            String id = location.getId();
            if (!addedIds.contains(id)) { // Check if the ID is already added
                String displayText = id + " - " + location.getName();
                JCheckBox checkbox = new JCheckBox(displayText);
                checkbox.setActionCommand(id); // Set the value as ID
                checkboxes.add(checkbox);
                checkboxPanel.add(checkbox);
                addedIds.add(id); // Mark this ID as added
            }
        }
    }
}