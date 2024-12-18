package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.UOMField;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /AREAS/AUTO_MK
 */
public class AutoMakeAreas extends LockeState {

    private JTextField areaIdField, areaNameField;
    private UOMField widthField, lengthField, heightField, areaField, volumeField;
    private JPanel checkboxPanel;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes;

    public AutoMakeAreas() {
        super("AutoMake Areas", "/AREAS/AUTO_MK", true, true, false, true);
        setFrameIcon(new ImageIcon(AutoMakeAreas.class.getResource("/icons/automake.png")));

        JTabbedPane tabs = new JTabbedPane();
        locations = Engine.getLocations();
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
        tabs.add(selector, "Locations");
        tabs.add(areaInformation(), "Area Info");
        setLayout(new BorderLayout());
        add(Elements.header("AutoMake Areas", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        JButton automake = Elements.button("AutoMake");
        add(automake, BorderLayout.SOUTH);
        automake.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.isSelected()) {
                        Area newArea = new Area();
                        newArea.setId(areaIdField.getText().trim().replace("@", checkbox.getActionCommand()));
                        newArea.setLocation(checkbox.getActionCommand());
                        newArea.setName(areaNameField.getText().replace("@", checkbox.getActionCommand()));
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
                    }
                }
                dispose();
                JOptionPane.showMessageDialog(AutoMakeAreas.this, "AutoMake Complete");
            }
        });
    }

    private JPanel areaInformation() {
        Form f = new Form();
        areaIdField = Elements.input("HBD1-@");
        areaNameField = Elements.input("Highbay");
        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();
        areaField = new UOMField();
        volumeField = new UOMField();
        f.addInput(new Label("*New Area ID (current: LOC_ID-001)", UIManager.getColor("Label.foreground")), areaIdField);
        f.addInput(new Label("Area Name (current: LOC_ID-001", UIManager.getColor("Label.foreground")), areaNameField);
        f.addInput(new Label("Width", Constants.colors[10]), widthField);
        f.addInput(new Label("Length", Constants.colors[9]), lengthField);
        f.addInput(new Label("Height", Constants.colors[8]), heightField);
        f.addInput(new Label("Area", Constants.colors[7]), areaField);
        f.addInput(new Label("Volume", Constants.colors[6]), volumeField);
        return f;
    }

    private void addCheckboxes() {
        for (Location location : locations) {
            String displayText = location.getId() + " - " + location.getName();
            JCheckBox checkbox = new JCheckBox(displayText);
            checkbox.setActionCommand(String.valueOf(location.getId())); // Set the value as ID
            checkboxes.add(checkbox);
            checkboxPanel.add(checkbox);
        }
    }
}