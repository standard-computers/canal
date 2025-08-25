package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
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

public class ModifyBin extends LockeState {

    //Operating Objects
    private Bin bin;
    private RefreshListener refreshListener;

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

    public ModifyBin(Bin bin, RefreshListener refreshListener) {

        super("View Bin", "/BNS/" + bin.getId());
        setFrameIcon(new ImageIcon(Bins.class.getResource("/icons/bins.png")));
        this.bin = bin;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Controls", controls());
        tabs.addTab("Item Restrictions", restrictions());

        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton save = new IconButton("Save", "save", "Save Changes");
        save.addActionListener(_ -> {

            Area area = Engine.getArea(bin.getArea());

           //Controls
           bin.setFixed(fixedBin.isSelected());
           bin.setPutaway(putawayEnabled.isSelected());
           bin.setPicking(pickingEnabled.isSelected());
           bin.setGoodsissue(doesGoodsIssue.isSelected());
           bin.setGoodsreceipt(doesGoodsReceipt.isSelected());
           bin.setHoldsStock(holdsStock.isSelected());

           area.setBin(bin);
           area.save();

           if(refreshListener != null){
               refreshListener.refresh();
           }
           dispose();

        });
        tb.add(save);
        tb.add(Box.createHorizontalStrut(5));

        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-save");
        rp.getActionMap().put("do-save", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                save.doClick();
            }
        });

        IconButton review = new IconButton("Review", "review", "Review Bin data");
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Bin", "/BNS/MOD");
        tb.add(delete);
        tb.add(Box.createHorizontalStrut(5));

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

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Bin ID", UIManager.getColor("Label.foreground")), new Copiable(bin.getId()));
        form.addInput(Elements.coloredLabel("Location ID", UIManager.getColor("Label.foreground")), new Copiable(""));
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

        doesGoodsReceipt = new JCheckBox("GR on put away");
        doesGoodsReceipt.setSelected(bin.doesGR());

        pickingEnabled = new JCheckBox("Allow picks from this bin");
        pickingEnabled.setSelected(bin.isPicking());

        putawayEnabled = new JCheckBox("Allow put away to this bin");
        putawayEnabled.setSelected(bin.isPutaway());

        autoReplenish = new JCheckBox("Auto Replenish");
        autoReplenish.setSelected(bin.isAuto_replenish());
        autoReplenish.setToolTipText("Bin will be automatically replenished based on set replenishments");

        fixedBin = new JCheckBox("Fixed Bin");
        fixedBin.setToolTipText("Bin can only contain one Item ID");

        holdsStock = new JCheckBox("Bin can hold inventory");
        holdsStock.setSelected(bin.isHoldsStock());

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