package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
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

    //Dimensions Tab
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;

    //Controls Tab
    private JCheckBox autoReplenish;
    private JCheckBox fixedBin;
    private JCheckBox doesGoodsIssue;
    private JCheckBox doesGoodsReceipt;
    private JCheckBox pickingEnabled;
    private JCheckBox putawayEnabled;
    private JCheckBox holdsStock;

    public CreateBin(String location, RefreshListener refreshListener) {

        super("New Bin", "/BNS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateBin.class.getResource("/icons/bins.png")));
        this.location = location;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Items", restrictions());
        tabs.addTab("Controls", restrictions());

        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from Bin");
        copyFrom.addActionListener(_ -> {
            String binId = JOptionPane.showInputDialog(CreateBin.this, "Enter Bin ID", "Copy Bin", JOptionPane.QUESTION_MESSAGE);
            if(!binId.isEmpty()){

            }
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Bin data");
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Refresh Data");
        create.addActionListener(_ -> {

            String binId = idField.getText();
            String binName = nameField.getText().trim();
            String binArea = areas.getSelectedValue();

            Bin newBin = new Bin();
            newBin.setId(binId);
            newBin.setName(binName);

            newBin.setWidth(Double.parseDouble(widthField.getValue()));
            newBin.setWidthUOM(widthField.getUOM());

            newBin.setLength(Double.parseDouble(lengthField.getValue()));
            newBin.setLengthUOM(lengthField.getUOM());

            newBin.setHeight(Double.parseDouble(heightField.getValue()));
            newBin.setHeightUOM(heightField.getUOM());

            newBin.setWeight(Double.parseDouble(weightField.getValue()));
            newBin.setWeightUOM(weightField.getUOM());

            newBin.setAuto_replenish(autoReplenish.isSelected());
            newBin.setFixed(fixedBin.isSelected());
            newBin.setPicking(pickingEnabled.isSelected());
            newBin.setPutaway(putawayEnabled.isSelected());
            newBin.setGoodsissue(doesGoodsIssue.isSelected());
            newBin.setGoodsreceipt(doesGoodsReceipt.isSelected());
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
        });
        tb.add(create);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));

        toolbar.add(Elements.header("Make a Bin", SwingConstants.LEFT), BorderLayout.NORTH);
        toolbar.add(tb, BorderLayout.SOUTH);
        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String generatedId;
        idField = Elements.input();
        locationField = Elements.input();
        HashMap<String, String> areas = new HashMap<>();
        for(Area a : Engine.getAreas(location)){
            areas.put(a.getName(), a.getId());
        }
        this.areas = new Selectable(areas);
        this.areas.editable();
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
        doesGoodsIssue = new JCheckBox("GI on stock removal from this bin");
        doesGoodsReceipt = new JCheckBox("GR on put away to this bin");
        pickingEnabled = new JCheckBox("Allow picks from this bin");
        putawayEnabled = new JCheckBox("Allow picks from this bin");
        autoReplenish = new JCheckBox("Auto Replenish");
        autoReplenish.setToolTipText("Bin will be automatically replenished based on set replenishments");
        fixedBin = new JCheckBox("Fixed Bin");
        fixedBin.setToolTipText("Bin can only contain one Item ID");
        holdsStock = new JCheckBox("Bin can hold inventory");

        Form form = new Form();
        form.addInput(Elements.coloredLabel("*New Bin ID", UIManager.getColor("Label.foreground")), idField);
        form.addInput(Elements.coloredLabel("Location ID", UIManager.getColor("Label.foreground")), locationField);
        form.addInput(Elements.coloredLabel("*Area", UIManager.getColor("Label.foreground")), this.areas);
        form.addInput(Elements.coloredLabel("Bin Name", Constants.colors[10]), nameField);
        form.addInput(Elements.coloredLabel("Goods Issue", Constants.colors[10]), doesGoodsIssue);
        form.addInput(Elements.coloredLabel("Goods Receipt", Constants.colors[10]), doesGoodsReceipt);
        form.addInput(Elements.coloredLabel("Picking Enabled", Constants.colors[9]), pickingEnabled);
        form.addInput(Elements.coloredLabel("Putaway Enabled", Constants.colors[9]), putawayEnabled);
        form.addInput(Elements.coloredLabel("Auto Replenish", Constants.colors[8]), autoReplenish);
        form.addInput(Elements.coloredLabel("Fixed Bin", Constants.colors[7]), fixedBin);
        form.addInput(Elements.coloredLabel("Holds Stock", Constants.colors[7]), holdsStock);
        general.add(form);

        return general;
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));

        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();
        weightField = new UOMField();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Width", Constants.colors[10]), widthField);
        form.addInput(Elements.coloredLabel("Length", Constants.colors[9]), lengthField);
        form.addInput(Elements.coloredLabel("Height", Constants.colors[8]), heightField);
        form.addInput(Elements.coloredLabel("Weight", Constants.colors[7]), weightField);
        dimensional.add(form);

        return dimensional;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        doesGoodsIssue = new JCheckBox("GI on stock removal");
        doesGoodsReceipt = new JCheckBox("GR on put away");
        pickingEnabled = new JCheckBox("Allow picks from this bin");
        putawayEnabled = new JCheckBox("Allow put away to this bin");
        autoReplenish = new JCheckBox("Auto Replenish");
        fixedBin = new JCheckBox("Fixed Bin");
        fixedBin.setToolTipText("Bin can only contain one Item ID");
        holdsStock = new JCheckBox("Bin can hold inventory");

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Goods Issue", UIManager.getColor("Label.foreground")), doesGoodsIssue);
        form.addInput(Elements.coloredLabel("Goods Receipt", UIManager.getColor("Label.foreground")), doesGoodsReceipt);
        form.addInput(Elements.coloredLabel("Picking Enabled", UIManager.getColor("Label.foreground")), pickingEnabled);
        form.addInput(Elements.coloredLabel("Putaway Enabled", UIManager.getColor("Label.foreground")), putawayEnabled);
        form.addInput(Elements.coloredLabel("Auto Replenish", UIManager.getColor("Label.foreground")), autoReplenish);
        form.addInput(Elements.coloredLabel("Fixed Bin", UIManager.getColor("Label.foreground")), fixedBin);
        form.addInput(Elements.coloredLabel("Holds Stock", UIManager.getColor("Label.foreground")), holdsStock);
        controls.add(form);

        return controls;
    }

    private JPanel restrictions() {

        JPanel restrictions = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return restrictions;
    }
}