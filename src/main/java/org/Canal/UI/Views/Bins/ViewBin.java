package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ViewBin extends LockeState {

    private Bin bin;
    private JTextField locationField;
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;
    private UOMField areaField;
    private UOMField volumeField;

    private JCheckBox autoReplenish;
    private JCheckBox fixedBin;
    private JCheckBox doesGoodsIssue;
    private JCheckBox doesGoodsReceipt;
    private JCheckBox pickingEnabled;
    private JCheckBox putawayEnabled;
    private JCheckBox holdsStock;

    public ViewBin(Bin bin) {

        super("View Bin", "/BNS/$", false, true, false, true);
        setFrameIcon(new ImageIcon(Bins.class.getResource("/icons/bins.png")));
        this.bin = bin;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
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

        IconButton create = new IconButton("Execute", "execute", "Refresh Data");
        tb.add(create);

        tb.setBorder(new EmptyBorder(0, 5, 0, 5));

        toolbar.add(Elements.header(bin.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();
        locationField = Elements.input();

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

        f.addInput(Elements.coloredLabel("Bin ID", UIManager.getColor("Label.foreground")), new Copiable(bin.getId()));
        f.addInput(Elements.coloredLabel("Location ID", UIManager.getColor("Label.foreground")), locationField);
        f.addInput(Elements.coloredLabel("Area", UIManager.getColor("Label.foreground")), new Copiable(""));
        f.addInput(Elements.coloredLabel("Bin Name", Constants.colors[10]), new Copiable(bin.getName()));
        f.addInput(Elements.coloredLabel("Goods Issue", Constants.colors[10]), doesGoodsIssue);
        f.addInput(Elements.coloredLabel("Goods Receipt", Constants.colors[10]), doesGoodsReceipt);
        f.addInput(Elements.coloredLabel("Picking Enabled", Constants.colors[9]), pickingEnabled);
        f.addInput(Elements.coloredLabel("Putaway Enabled", Constants.colors[9]), putawayEnabled);
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
        widthField.setValue(String.valueOf(bin.getWidth()));
        lengthField = new UOMField();
        lengthField.setValue(String.valueOf(bin.getLength()));
        heightField = new UOMField();
        heightField.setValue(String.valueOf(bin.getHeight()));
        weightField = new UOMField();
        weightField.setValue(String.valueOf(bin.getWeight()));
        areaField = new UOMField();
        areaField.setValue(String.valueOf(bin.getArea()));
        volumeField = new UOMField();
        volumeField.setValue(String.valueOf(bin.getVolume()));

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

    private JPanel stock() {

        JPanel stock = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return stock;
    }
}