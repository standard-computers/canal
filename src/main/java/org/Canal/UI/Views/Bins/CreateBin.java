package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.ViewLocation;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * /BNS/NEW
 */
public class CreateBin extends LockeState {

    private String location;
    private RefreshListener refreshListener;
    private JTextField idField;
    private JTextField locationField;
    private JTextField nameField;
    private Selectable areas;
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;
    private UOMField areaField;
    private UOMField volumeField;
    private JCheckBox autoReplenish;
    private JCheckBox fixedBin;
    private JCheckBox doesGoodsIssue;
    private JCheckBox pickingEnabled;
    private JCheckBox holdsStock;

    public CreateBin(String location, RefreshListener refreshListener) {

        super("New Bin", "/BNS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateBin.class.getResource("/icons/bins.png")));
        this.location = location;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Item Restrictions", restrictions());

        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from Bin");
        IconButton review = new IconButton("Review", "review", "Review Bin data");
        IconButton create = new IconButton("Create", "execute", "Refresh Data");
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(create);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));
        copyFrom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String binId = JOptionPane.showInputDialog(CreateBin.this, "Enter Bin ID", "Copy Bin", JOptionPane.QUESTION_MESSAGE);
                if(!binId.isEmpty()){

                }
            }
        });
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String binId = idField.getText();
                String binName = nameField.getText();
                String binArea = areas.getSelectedValue();
                double binWidth = Double.parseDouble(widthField.getValue());
                String binWidthUom = widthField.getUOM();
                double binLength = Double.parseDouble(lengthField.getValue());
                String binLengthUom = lengthField.getUOM();
                double binHeight = Double.parseDouble(heightField.getValue());
                String binHeightUom = heightField.getUOM();
                double binVolume = Double.parseDouble(areaField.getValue());
                String binVolumeUom = areaField.getUOM();
                Bin newBin = new Bin();
                newBin.setId(binId);
                newBin.setName(binName);
                newBin.setWidth(binWidth);
                newBin.setWidthUOM(binWidthUom);
                newBin.setLength(binLength);
                newBin.setLengthUOM(binLengthUom);
                newBin.setHeight(binHeight);
                newBin.setHeightUOM(binHeightUom);
                newBin.setAreaValue(areaField.getValue());
                newBin.setAreaUOM(areaField.getUOM());
                newBin.setVolume(binVolume);
                newBin.setVolumeUOM(binVolumeUom);
                newBin.setAuto_replenish(autoReplenish.isSelected());
                newBin.setFixed(fixedBin.isSelected());
                newBin.setPicking(pickingEnabled.isSelected());
                newBin.setGoodsissue(doesGoodsIssue.isSelected());
                newBin.setHoldsStock(holdsStock.isSelected());
                Area foundArea = Engine.getArea(binArea);
                if(foundArea != null){
                    foundArea.addBin(newBin);
                    foundArea.save();
                    dispose();
                    JOptionPane.showMessageDialog(null, "Bin '" + binName + "' created in '" + binArea + "'");
                    refreshListener.refresh();
                }else{
                    JOptionPane.showMessageDialog(null, "Bin '" + binName + "' could not be created in '" + binArea + "'");
                }
            }
        });
        toolbar.add(Elements.header("Make a Bin", SwingConstants.LEFT), BorderLayout.NORTH);
        toolbar.add(tb, BorderLayout.SOUTH);
        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();
        String generatedId;
        idField = Elements.input();
        locationField = Elements.input();
        HashMap<String, String> areas = new HashMap<>();
        for(Area a : Engine.getAreas(location)){
            areas.put(a.getName(), a.getId());
        }
        this.areas = new Selectable(areas);
        generatedId = "BN" + (Engine.getAreas(location).size() + 1) + "-" + this.areas.getSelectedValue();
        this.areas.setSelectedValue(location);
        locationField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChange();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                onChange();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                onChange();
            }
            private void onChange() {
                String locationIdAttempt = locationField.getText();
                for(Location l : Engine.getLocations()){
                    if(l.getId().equals(locationIdAttempt)){

                    }
                }
            }
        });
        idField.setText(generatedId);
        nameField = Elements.input(generatedId, 20);
        doesGoodsIssue = new JCheckBox("GI on put away to this bin");
        pickingEnabled = new JCheckBox("Allow picks from this bin");
        autoReplenish = new JCheckBox("Auto Replenish");
        autoReplenish.setToolTipText("Bin will be automatically replenished based on set replenishments");
        fixedBin = new JCheckBox("Fixed Bin");
        fixedBin.setToolTipText("Bin can only contain one Item ID");
        holdsStock = new JCheckBox("Bin can hold inventory");
        f.addInput(Elements.coloredLabel("*New Bin ID", UIManager.getColor("Label.foreground")), idField);
        f.addInput(Elements.coloredLabel("Location ID", UIManager.getColor("Label.foreground")), locationField);
        f.addInput(Elements.coloredLabel("*Area", UIManager.getColor("Label.foreground")), this.areas);
        f.addInput(Elements.coloredLabel("Bin Name", Constants.colors[10]), nameField);
        f.addInput(Elements.coloredLabel("Goods Issue Enabled", Constants.colors[10]), doesGoodsIssue);
        f.addInput(Elements.coloredLabel("Picking Enabled", Constants.colors[9]), pickingEnabled);
        f.addInput(Elements.coloredLabel("Auto Replenish", Constants.colors[8]), autoReplenish);
        f.addInput(Elements.coloredLabel("Fixed Bin", Constants.colors[7]), fixedBin);
        f.addInput(Elements.coloredLabel("Holds Stock", Constants.colors[7]), holdsStock);
        general.add(f);
        return general;
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();
        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();
        weightField = new UOMField();
        areaField = new UOMField();
        volumeField = new UOMField();
        f.addInput(Elements.coloredLabel("Width", Constants.colors[10]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[9]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[8]), heightField);
        f.addInput(Elements.coloredLabel("Weight", Constants.colors[7]), weightField);
        f.addInput(Elements.coloredLabel("Area", Constants.colors[6]), areaField);
        f.addInput(Elements.coloredLabel("Volume", Constants.colors[5]), volumeField);
        dimensional.add(f);
        return dimensional;
    }

    private JPanel restrictions() {

        JPanel restrictions = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return restrictions;
    }
}