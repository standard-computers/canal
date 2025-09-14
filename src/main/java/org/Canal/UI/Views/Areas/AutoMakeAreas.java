package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /AREAS/AUTO_MK
 */
public class AutoMakeAreas extends LockeState {

    private RefreshListener refreshListener;

    //General Area Info
    private JTextField areaIdField;
    private JTextField areaNameField;
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;

    //Controls
    private JCheckBox allowsInventory;
    private JCheckBox allowsProduction;
    private JCheckBox allowsPurchasing;
    private JCheckBox allowsSales;

    //Locations to make Area for
    private JPanel checkboxPanel;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes;

    public AutoMakeAreas(RefreshListener refreshListener) {

        super("AutoMake Areas", "/AREAS/AUTO_MK");
        setFrameIcon(new ImageIcon(AutoMakeAreas.class.getResource("/icons/automake.png")));
        setLayout(new BorderLayout());
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());
        tabs.addTab("Locations", locationsSelection());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("AutoMake Areas", SwingConstants.LEFT), BorderLayout.CENTER);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar(){

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("AutoMake", "automake", "Start AutoMake");
        create.addActionListener(_ -> {
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
                    Pipe.save("/AREAS", newArea);
                }
            }
            dispose();
            JOptionPane.showMessageDialog(AutoMakeAreas.this, "AutoMake Complete");

            if(refreshListener != null){
                refreshListener.refresh();
            }
        });
        tb.add(create);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));

        panel.add(tb, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();

        areaIdField = Elements.input("HBD1-@");
        areaNameField = Elements.input("Highbay");
        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();

        f.addInput(Elements.coloredLabel("*New Area ID (current: LOC_ID-001)", UIManager.getColor("Label.foreground")), areaIdField);
        f.addInput(Elements.coloredLabel("Area Name (current: LOC_ID-001", UIManager.getColor("Label.foreground")), areaNameField);
        f.addInput(Elements.coloredLabel("Width", Constants.colors[10]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[9]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[8]), heightField);
        general.add(f);

        return general;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form f = new Form();

        allowsInventory = new JCheckBox("Inventory Movements");
        allowsProduction = new JCheckBox("Allows Production");
        allowsSales = new JCheckBox("Sales Order Processing");
        allowsPurchasing = new JCheckBox("Purchase Order Processing");

        f.addInput(Elements.coloredLabel("Allow Inventory", Constants.colors[0]), allowsInventory);
        f.addInput(Elements.coloredLabel("Allow Production", Constants.colors[1]), allowsProduction);
        f.addInput(Elements.coloredLabel("Allow Sales", Constants.colors[2]), allowsSales);
        f.addInput(Elements.coloredLabel("Allow Purchasing", Constants.colors[3]), allowsPurchasing);
        controls.add(f);

        return controls;
    }

    private JPanel locationsSelection(){
        JPanel locationsSelection = new JPanel(new BorderLayout());
        locations = Engine.getLocations();

        this.checkboxes = new ArrayList<>();
        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        addCheckboxes();

        JScrollPane js = new JScrollPane(checkboxPanel);
        js.setPreferredSize(new Dimension(200, 200));

        JPanel selector = new JPanel(new BorderLayout());

        JTextField search = Elements.input();
        search.addActionListener(_ -> {

           String searchValue = search.getText().trim();
           if(searchValue.endsWith("*")){ //Searching for ID starts with
               for (JCheckBox checkbox : checkboxes) {
                   if (checkbox.getText().startsWith(searchValue.substring(0, searchValue.length() - 1))) {
                       checkbox.setSelected(!checkbox.isSelected());
                   }
               }
           } else if (searchValue.startsWith("/")) { //Objex type selection

               for (int i = 0; i < checkboxes.size(); i++) {
                   if(locations.get(i).getType().equals(searchValue.toUpperCase())){
                       checkboxes.get(i).setSelected(!checkboxes.get(i).isSelected());
                   }
               }

           } else { //Select exact match
               for (JCheckBox checkbox : checkboxes) {
                   if (checkbox.getText().equals(searchValue)) {
                       checkbox.setSelected(!checkbox.isSelected());
                   }
               }
           }
        });

        selector.add(search, BorderLayout.NORTH);

        selector.add(js, BorderLayout.CENTER);

        JPanel opts = new JPanel(new GridLayout(1, 2));

        JButton selectAll = Elements.button("Select All");
        selectAll.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(true));
                repaint();
            }
        });
        opts.add(selectAll);

        JButton deselectAll = Elements.button("Deselect All");
        deselectAll.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(false));
                repaint();
            }
        });
        opts.add(deselectAll);

        selector.add(opts, BorderLayout.SOUTH);
        locationsSelection.add(selector, BorderLayout.CENTER);

        return locationsSelection;
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