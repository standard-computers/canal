package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * /BNS/NEW
 */
public class CreateBin extends LockeState {

    private String location;
    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JTextField idField;
    private JTextField nameField;
    private JTextField areas;

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
        this.location = location;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Items", restrictions());

        if ((boolean) Engine.codex.getValue("BNS", "allow_notes")) {
            tabs.addTab("Notes", notes());
        }

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
            if (!binId.isEmpty()) {

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
            String binArea = areas.getText();

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
            if (refreshListener != null) refreshListener.refresh();
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-modify");
        rp.getActionMap().put("do-modify", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        areas = Elements.input();
        generatedId = "BN" + (Engine.getAreas(location).size() + 1) + "-" + areas.getText();
        areas.setText(location);

        idField.setText(generatedId);
        nameField = Elements.input(generatedId, 15);

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Bin ID"), idField);
        form.addInput(Elements.inputLabel("*Area"), this.areas);
        form.addInput(Elements.inputLabel("Bin Name"), nameField);
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
        form.addInput(Elements.inputLabel("Goods Issue"), doesGoodsIssue);
        form.addInput(Elements.inputLabel("Goods Receipt"), doesGoodsReceipt);
        form.addInput(Elements.inputLabel("Picking Enabled"), pickingEnabled);
        form.addInput(Elements.inputLabel("Putaway Enabled"), putawayEnabled);
        form.addInput(Elements.inputLabel("Auto Replenish"), autoReplenish);
        form.addInput(Elements.inputLabel("Fixed Bin"), fixedBin);
        form.addInput(Elements.inputLabel("Holds Stock"), holdsStock);
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
        form.addInput(Elements.inputLabel("Width"), widthField);
        form.addInput(Elements.inputLabel("Length"), lengthField);
        form.addInput(Elements.inputLabel("Height"), heightField);
        form.addInput(Elements.inputLabel("Weight"), weightField);
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

    private void performReview() {

        if (idField.getText().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Bin ID is not set!!!"});
        }

        if (areas.getText().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Bin Area is not set!!!"});
        }

        if (nameField.getText().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "Bin Name is not set!!!"});
        }

        if (Double.parseDouble(widthField.getValue()) == 0) {
            addToQueue(new String[]{"WARNING", "Bin width will be set to 0. Are you sure?"});
        }

        if (Double.parseDouble(lengthField.getValue()) == 0) {
            addToQueue(new String[]{"WARNING", "Bin length will be set to 0. Are you sure?"});
        }

        if (Double.parseDouble(heightField.getValue()) == 0) {
            addToQueue(new String[]{"WARNING", "Bin height will be set to 0. Are you sure?"});
        }

        if (Double.parseDouble(weightField.getValue()) == 0) {
            addToQueue(new String[]{"WARNING", "Bin weight will be set to 0. Are you sure?"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}