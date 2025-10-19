package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * /BNS/AUTO_MK
 */
public class AutoMakeBins extends LockeState {

    //Operating Objects
    private DesktopState desktop;

    //General Info
    private JTextField idField;
    private JTextField nameField;
    private JTextField binCount;

    //Dimensional Tab
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;

    //Areas Tab
    private JPanel checkboxPanel;
    private ArrayList<Area> areas;
    private ArrayList<JCheckBox> checkboxes;

    //Controls Tab
    private JCheckBox autoReplenish;
    private JCheckBox fixedBin;
    private JCheckBox doesGoodsIssue;
    private JCheckBox doesGoodsReceipt;
    private JCheckBox pickingEnabled;
    private JCheckBox putawayEnabled;
    private JCheckBox holdsStock;

    public AutoMakeBins(DesktopState desktop) {

        super("AutoMake Bins", "/BNS/AUTO_MK");
        setFrameIcon(new ImageIcon(AutoMakeBins.class.getResource("/icons/automake.png")));
        this.desktop = desktop;

        CustomTabbedPane tabs = new CustomTabbedPane();
        areas = Engine.getAreas();
        areas.addAll(Engine.getAreas());
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
            if (searchValue.endsWith("*")) { //Searching for ID starts with
                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().startsWith(searchValue.substring(0, searchValue.length() - 1))) {
                        checkbox.setSelected(!checkbox.isSelected());
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
        JButton sa = Elements.button("Select All");
        sa.addActionListener(_ -> {
            checkboxes.forEach(cb -> cb.setSelected(true));
            repaint();
        });
        opts.add(sa);

        JButton dsa = Elements.button("Deselect All");
        dsa.addActionListener(_ -> {
            checkboxes.forEach(cb -> cb.setSelected(false));
            repaint();
        });
        opts.add(dsa);

        tabs.addTab("Areas", selector);
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Controls", controls());

        setLayout(new BorderLayout());
        selector.add(opts, BorderLayout.SOUTH);
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from Bin");
        copyFrom.addActionListener(_ -> {
            String binId = JOptionPane.showInputDialog("Bin ID", "Bin ID");
            Bin bin = Engine.getBin(binId);
            if (bin == null) {
                return;
            } else {
                widthField.setValue(String.valueOf(bin.getWidth()));
                widthField.setUOM(bin.getWidthUOM());
                lengthField.setValue(String.valueOf(bin.getLength()));
                lengthField.setUOM(bin.getLengthUOM());
                heightField.setValue(String.valueOf(bin.getHeight()));
                heightField.setUOM(bin.getHeightUOM());
                weightField.setValue(String.valueOf(bin.getWeight()));
                weightField.setUOM(bin.getWeightUOM());
                autoReplenish.setSelected(bin.isAuto_replenish());
                fixedBin.setSelected(bin.isFixed());
                putawayEnabled.setSelected(bin.putawayEnabled());
                pickingEnabled.setSelected(bin.pickingEnabled());
                doesGoodsIssue.setSelected(bin.doesGI());
                doesGoodsReceipt.setSelected(bin.doesGR());
                holdsStock.setSelected(bin.holdsStock());
            }

        });
        tb.add(Box.createHorizontalStrut(5));
        tb.add(copyFrom);

        IconButton review = new IconButton("Review", "review", "Review Date");
        review.addActionListener(_ -> performReview());
        tb.add(Box.createHorizontalStrut(5));
        tb.add(review);

        IconButton start = new IconButton("Start AutoMake", "automake", "Start AutoMake");
        start.addActionListener(_ -> {
            dispose();
            for (JCheckBox checkbox : checkboxes) {

                if (checkbox.isSelected()) {

                    Area a = Engine.getArea(checkbox.getActionCommand());
                    if (a != null) {

                        for (int i = 1; i <= Integer.parseInt(binCount.getText()); i++) {

                            Bin b = new Bin();
                            b.setId(idField.getText().trim()
                                    .replace("@", checkbox.getActionCommand())
                                    .replace("+", String.valueOf(i)));
                            b.setName(nameField.getText().trim()
                                    .replace("@", checkbox.getActionCommand())
                                    .replace("+", String.valueOf(i)));
                            b.setArea(checkbox.getActionCommand());

                            b.setWidth(Double.parseDouble(widthField.getValue()));
                            b.setWidthUOM(widthField.getUOM());

                            b.setLength(Double.parseDouble(lengthField.getValue()));
                            b.setLengthUOM(lengthField.getUOM());

                            b.setHeight(Double.parseDouble(heightField.getValue()));
                            b.setHeightUOM(heightField.getUOM());

                            b.setWeight(Double.parseDouble(weightField.getValue()));
                            b.setWeightUOM(weightField.getUOM());

                            b.setAuto_replenish(autoReplenish.isSelected());
                            b.setFixed(fixedBin.isSelected());
                            b.putawayEnabled(putawayEnabled.isSelected());
                            b.pickingEnabled(pickingEnabled.isSelected());
                            b.doesGI(doesGoodsIssue.isSelected());
                            b.doesGR(doesGoodsReceipt.isSelected());
                            b.holdsStock(holdsStock.isSelected());

                            Pipe.save("/BNS", b);
                        }
                    }
                    a.save();
                }
            }
            JOptionPane.showMessageDialog(AutoMakeBins.this, "AutoMake Complete");
        });
        tb.add(Box.createHorizontalStrut(5));
        tb.add(start);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        toolbar.add(Elements.header("AutoMake Bins", SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);
        return toolbar;
    }

    private JPanel general() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));

        idField = Elements.input("@-BN+", 15);
        nameField = Elements.input("@-BIN+");
        binCount = Elements.input("1");

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Bin ID (current: BN1-IBD1)", Constants.colors[10]), idField);
        form.addInput(Elements.coloredLabel("Bin Name (current: BIN1-IBD1)", Constants.colors[9]), nameField);
        form.addInput(Elements.coloredLabel("Bin Create Count", Constants.colors[8]), binCount);
        p.add(form);

        return p;
    }

    private void addCheckboxes() {

        Set<String> addedIds = new HashSet<>();
        for (Area location : areas) {
            String id = location.getId();
            if (!addedIds.contains(id)) {
                String displayText = id;
                JCheckBox checkbox = new JCheckBox(displayText);
                checkbox.setActionCommand(id);
                checkboxes.add(checkbox);
                checkboxPanel.add(checkbox);
                addedIds.add(id);
            }
        }
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));

        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();
        weightField = new UOMField();

        Form f = new Form();
        f.addInput(Elements.coloredLabel("Width", Constants.colors[10]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[9]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[8]), heightField);
        f.addInput(Elements.coloredLabel("Weight", Constants.colors[7]), weightField);
        dimensional.add(f);

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

    private void performReview(){

        if(idField.getText().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin ID field empty!"});
        }

        if(nameField.getText().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin Name field empty!"});
        }

        if(Double.parseDouble(binCount.getText()) <= 0){
            addToQueue(new String[]{"CRITICAL", "Bin count is less than or equal to 0! No bins will be created!"});
        }

        if(widthField.getValue().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin WIDTH dimension is set to 0, are you sure?"});
        }

        if(lengthField.getValue().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin LENGTH dimension is set to 0, are you sure?"});
        }

        if(heightField.getValue().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin HEIGHT dimension is set to 0, are you sure?"});
        }

        if(weightField.getValue().isEmpty()){
            addToQueue(new String[]{"CRITICAL", "Bin WEIGHT dimension is set to 0, are you sure?"});
        }

        int areaCount = 0;
        for (JCheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                areaCount++;
            }
        }
        if (areaCount == 0) {
            addToQueue(new String[]{"CRITICAL", "NO AREAS SELECTED!!!"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}