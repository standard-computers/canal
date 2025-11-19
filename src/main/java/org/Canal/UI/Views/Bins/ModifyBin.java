package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ModifyBin extends LockeState {

    //Operating Objects
    private Bin bin;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //General Info
    private Selectable statuses;

    //Controls Tab
    private JCheckBox autoReplenish;
    private JCheckBox fixedBin;
    private JCheckBox putawayEnabled;
    private JCheckBox pickingEnabled;
    private JCheckBox doesGoodsIssue;
    private JCheckBox doesGoodsReceipt;
    private JCheckBox holdsStock;

    //Dimensional Tab
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;

    //Notes Tab
    private RTextScrollPane notes;

    public ModifyBin(Bin bin, DesktopState desktop, RefreshListener refreshListener) {

        super("Modify Bin", "/BNS/MOD/" + bin.getId());
        this.bin = bin;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Item Restrictions", restrictions());

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

        IconButton save = new IconButton("Save", "save", "Save Changes");
        save.addActionListener(_ -> {

            //Controls
            bin.setFixed(fixedBin.isSelected());
            bin.putawayEnabled(putawayEnabled.isSelected());
            bin.pickingEnabled(pickingEnabled.isSelected());
            bin.doesGI(doesGoodsIssue.isSelected());
            bin.doesGR(doesGoodsReceipt.isSelected());
            bin.holdsStock(holdsStock.isSelected());
            bin.setNotes(notes.getTextArea().getText());
            bin.setStatus(LockeStatus.valueOf(statuses.getSelectedValue()));
            bin.save();

            dispose();
            if (refreshListener != null) refreshListener.refresh();
        });
        tb.add(save);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-save");
        rp.getActionMap().put("do-save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save.doClick();
            }
        });

        IconButton review = new IconButton("Review", "review", "Review Bin data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton block = new IconButton("Block", "block", "Block Bin", "/BNS/BLK");
        block.addActionListener(_ -> {
            bin.setStatus(LockeStatus.BLOCKED);
            bin.save();
        });
        tb.add(block);
        tb.add(Box.createHorizontalStrut(5));

        IconButton archive = new IconButton("Archive", "archive", "Archive Bin", "/BNS/BLK");
        archive.addActionListener(_ -> {
            bin.setStatus(LockeStatus.ARCHIVED);
            bin.save();
        });
        tb.add(archive);
        tb.add(Box.createHorizontalStrut(5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Bin", "/BNS/MOD");
        delete.addActionListener(_ -> {

            //TODO Checks
            bin.setStatus(LockeStatus.DELETED);
            bin.save();

            dispose();
            if (refreshListener != null) refreshListener.refresh();
        });
        tb.add(delete);
        tb.add(Box.createHorizontalStrut(5));

        toolbar.add(Elements.header(bin.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        statuses = Selectables.statusTypes();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Bin ID"), new Copiable(bin.getId()));
        form.addInput(Elements.inputLabel("Location ID"), new Copiable(""));
        form.addInput(Elements.inputLabel("Area"), new Copiable(bin.getArea()));
        form.addInput(Elements.inputLabel("Bin Name"), new Copiable(bin.getName()));
        form.addInput(Elements.inputLabel("Status"), statuses);
        general.add(form);

        return general;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        doesGoodsIssue = new JCheckBox("GI on stock removal");
        doesGoodsIssue.setSelected(bin.doesGI());

        doesGoodsReceipt = new JCheckBox("GR on put away");
        doesGoodsReceipt.setSelected(bin.doesGR());

        pickingEnabled = new JCheckBox("Allow picks from this bin");
        pickingEnabled.setSelected(bin.pickingEnabled());

        putawayEnabled = new JCheckBox("Allow put away to this bin");
        putawayEnabled.setSelected(bin.putawayEnabled());

        autoReplenish = new JCheckBox("Auto Replenish");
        autoReplenish.setSelected(bin.isAuto_replenish());
        autoReplenish.setToolTipText("Bin will be automatically replenished based on set replenishments");

        fixedBin = new JCheckBox("Fixed Bin");
        fixedBin.setToolTipText("Bin can only contain one Item ID");

        holdsStock = new JCheckBox("Bin can hold inventory");
        holdsStock.setSelected(bin.holdsStock());

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
        widthField.setValue(String.valueOf(bin.getWidth()));
        widthField.setUOM(bin.getWidthUOM());

        lengthField = new UOMField();
        lengthField.setValue(String.valueOf(bin.getLength()));
        lengthField.setUOM(bin.getLengthUOM());

        heightField = new UOMField();
        heightField.setValue(String.valueOf(bin.getHeight()));
        heightField.setUOM(bin.getHeightUOM());

        weightField = new UOMField();
        weightField.setValue(String.valueOf(bin.getWeight()));
        weightField.setUOM(bin.getWeightUOM());

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
        notes.getTextArea().setText(bin.getNotes());
        return notes;
    }

    private void performReview() {

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