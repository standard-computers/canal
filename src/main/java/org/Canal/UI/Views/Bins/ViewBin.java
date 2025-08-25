package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ViewBin extends LockeState {

    //Operating Objects
    private Bin bin;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //Controls Tab
    private JCheckBox autoReplenish;
    private JCheckBox fixedBin;
    private JCheckBox doesGoodsIssue;
    private JCheckBox doesGoodsReceipt;
    private JCheckBox pickingEnabled;
    private JCheckBox putawayEnabled;
    private JCheckBox holdsStock;

    //Dimensional Tab
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;

    public ViewBin(Bin bin, DesktopState desktop, RefreshListener refreshListener) {

        super("View Bin", "/BNS/" + bin.getId());
        setFrameIcon(new ImageIcon(Bins.class.getResource("/icons/bins.png")));
        this.bin = bin;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Controls", controls());
        tabs.addTab("Item Restrictions", restrictions());
        tabs.addTab("Stock", stock());

        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from Bin");
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Bin data");
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton modify = new IconButton("Modify", "modify", "Modify Bin", "/BNS/MOD");
        modify.addActionListener(_ -> {
            dispose();
            desktop.put(new ModifyBin(bin, refreshListener));
        });
        tb.add(modify);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_E, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-modify");
        rp.getActionMap().put("do-modify", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                modify.doClick();
            }
        });

        IconButton block = new IconButton("Block", "block", "Block Bin", "/BNS/BLK");
        block.addActionListener(_ -> {
            bin.setStatus(LockeStatus.BLOCKED);

        });
        tb.add(block);
        tb.add(Box.createHorizontalStrut(5));

        tb.setBorder(new EmptyBorder(0, 5, 0, 5));

        toolbar.add(Elements.header(bin.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String locationId = Engine.getArea(bin.getArea()).getLocation();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Bin ID", UIManager.getColor("Label.foreground")), new Copiable(bin.getId()));
        form.addInput(Elements.coloredLabel("Location ID", UIManager.getColor("Label.foreground")), new Copiable(locationId));
        form.addInput(Elements.coloredLabel("Area", UIManager.getColor("Label.foreground")), new Copiable(bin.getArea()));
        form.addInput(Elements.coloredLabel("Bin Name", UIManager.getColor("Label.foreground")), new Copiable(bin.getName()));
        general.add(form);

        return general;
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));

        widthField = new UOMField();
        widthField.setValue(String.valueOf(bin.getWidth()));
        widthField.setUOM(bin.getWidthUOM());
        widthField.disable();

        lengthField = new UOMField();
        lengthField.setValue(String.valueOf(bin.getLength()));
        lengthField.setUOM(bin.getLengthUOM());
        lengthField.disable();

        heightField = new UOMField();
        heightField.setValue(String.valueOf(bin.getHeight()));
        heightField.setUOM(bin.getHeightUOM());
        heightField.disable();

        weightField = new UOMField();
        weightField.setValue(String.valueOf(bin.getWeight()));
        weightField.setUOM(bin.getWeightUOM());
        weightField.disable();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Width", UIManager.getColor("Label.foreground")), widthField);
        form.addInput(Elements.coloredLabel("Length", UIManager.getColor("Label.foreground")), lengthField);
        form.addInput(Elements.coloredLabel("Height", UIManager.getColor("Label.foreground")), heightField);
        form.addInput(Elements.coloredLabel("Weight", UIManager.getColor("Label.foreground")), weightField);
        dimensional.add(form);

        return dimensional;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        doesGoodsIssue = new JCheckBox("GI on stock removal");
        doesGoodsIssue.setSelected(bin.doesGI());
        doesGoodsIssue.setEnabled(false);

        doesGoodsReceipt = new JCheckBox("GR on put away");
        doesGoodsReceipt.setSelected(bin.doesGR());
        doesGoodsReceipt.setEnabled(false);

        pickingEnabled = new JCheckBox("Allow picks from this bin");
        pickingEnabled.setSelected(bin.isPicking());
        pickingEnabled.setEnabled(false);

        putawayEnabled = new JCheckBox("Allow put away to this bin");
        putawayEnabled.setSelected(bin.isPutaway());
        putawayEnabled.setEnabled(false);

        autoReplenish = new JCheckBox("Auto Replenish");
        autoReplenish.setSelected(bin.isAuto_replenish());
        autoReplenish.setToolTipText("Bin will be automatically replenished based on set replenishments");
        autoReplenish.setEnabled(false);

        fixedBin = new JCheckBox("Fixed Bin");
        fixedBin.setToolTipText("Bin can only contain one Item ID");
        fixedBin.setEnabled(false);

        holdsStock = new JCheckBox("Bin can hold inventory");
        holdsStock.setSelected(bin.isHoldsStock());
        holdsStock.setEnabled(false);

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

    private JPanel stock() {

        JPanel stock = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return stock;
    }
}