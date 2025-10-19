package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * /BNS/NEW
 */
public class CreateBin extends LockeState {

    private String location;
    private DesktopState desktop;
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

    //Notes Tab
    private RTextScrollPane notes;

    public CreateBin(String location, DesktopState desktop, RefreshListener refreshListener) {

        super("New Bin", "/BNS/NEW");
        setFrameIcon(new ImageIcon(CreateBin.class.getResource("/icons/create.png")));
        this.location = location;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Items", restrictions());
        tabs.addTab("Notes", notes());

        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("BNS", "start_maximized")) {
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from Bin");
        copyFrom.addActionListener(_ -> {
            String binId = JOptionPane.showInputDialog(CreateBin.this, "Enter Bin ID", "Copy Bin", JOptionPane.QUESTION_MESSAGE);
            if(!binId.isEmpty()){

            }
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Bin data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Refresh Data");
        create.addActionListener(_ -> {

            String binId = idField.getText();
            String binName = nameField.getText().trim();
            String binArea = areas.getSelectedValue();

            Bin newBin = new Bin();
            newBin.setId(binId);
            newBin.setArea(binArea);
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
            newBin.pickingEnabled(pickingEnabled.isSelected());
            newBin.putawayEnabled(putawayEnabled.isSelected());
            newBin.doesGI(doesGoodsIssue.isSelected());
            newBin.doesGR(doesGoodsReceipt.isSelected());
            newBin.holdsStock(holdsStock.isSelected());
            newBin.setNotes(notes.getTextArea().getText());
            Pipe.save("/BNS", newBin);

            dispose();
            if(refreshListener != null) refreshListener.refresh();
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-modify");
        rp.getActionMap().put("do-modify", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                create.doClick();
            }
        });

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

        Form form = new Form();
        form.addInput(Elements.coloredLabel("*New Bin ID", UIManager.getColor("Label.foreground")), idField);
        form.addInput(Elements.coloredLabel("Location ID", UIManager.getColor("Label.foreground")), locationField);
        form.addInput(Elements.coloredLabel("*Area", UIManager.getColor("Label.foreground")), this.areas);
        form.addInput(Elements.coloredLabel("Bin Name", Constants.colors[10]), nameField);
        general.add(form);

        return general;
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
        form.addInput(Elements.coloredLabel("Goods Issue", Constants.colors[10]), doesGoodsIssue);
        form.addInput(Elements.coloredLabel("Goods Receipt", Constants.colors[9]), doesGoodsReceipt);
        form.addInput(Elements.coloredLabel("Picking Enabled", Constants.colors[8]), pickingEnabled);
        form.addInput(Elements.coloredLabel("Putaway Enabled", Constants.colors[7]), putawayEnabled);
        form.addInput(Elements.coloredLabel("Auto Replenish", Constants.colors[6]), autoReplenish);
        form.addInput(Elements.coloredLabel("Fixed Bin", Constants.colors[5]), fixedBin);
        form.addInput(Elements.coloredLabel("Holds Stock", Constants.colors[4]), holdsStock);
        controls.add(form);

        return controls;
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));

        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();
        weightField = new UOMField();
        weightField.setUOM("OZ");

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Width", Constants.colors[0]), widthField);
        form.addInput(Elements.coloredLabel("Length", Constants.colors[1]), lengthField);
        form.addInput(Elements.coloredLabel("Height", Constants.colors[2]), heightField);
        form.addInput(Elements.coloredLabel("Weight", Constants.colors[3]), weightField);
        dimensional.add(form);

        return dimensional;
    }

    private JPanel restrictions() {

        JPanel restrictions = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return restrictions;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview(){

        if(idField.getText().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin ID is not set!!!"});
        }

        if(locationField.getText().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin Location ID is not set!!!"});
        }

        if(areas.getSelectedValue().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin Area is not set!!!"});
        }

        if(nameField.getText().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin Name is not set!!!"});
        }

        if(Double.parseDouble(widthField.getValue()) == 0){
            addToQueue(new String[]{"WARNING", "Bin width will be set to 0. Are you sure?"});
        }

        if(Double.parseDouble(lengthField.getValue()) == 0){
            addToQueue(new String[]{"WARNING", "Bin length will be set to 0. Are you sure?"});
        }

        if(Double.parseDouble(heightField.getValue()) == 0){
            addToQueue(new String[]{"WARNING", "Bin height will be set to 0. Are you sure?"});
        }

        if(Double.parseDouble(weightField.getValue()) == 0){
            addToQueue(new String[]{"WARNING", "Bin weight will be set to 0. Are you sure?"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}