package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * /BNS/AUTO_MK
 */
public class AutoMakeBins extends LockeState {

    private JTextField idField;
    private JTextField nameField;
    private JTextField binCount;
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;
    private JPanel checkboxPanel;
    private ArrayList<Area> areas;
    private ArrayList<JCheckBox> checkboxes;

    public AutoMakeBins() {
        super("AutoMake Bins", "/BNS/AUTO_MK", false, true, false, true);
        setFrameIcon(new ImageIcon(AutoMakeBins.class.getResource("/icons/automake.png")));
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
        selector.add(search, BorderLayout.NORTH);
        selector.add(js, BorderLayout.CENTER);
        JPanel opts = new JPanel(new GridLayout(1, 2));
        JButton sa = Elements.button("Select All");
        JButton dsa = Elements.button("Deselect All");
        sa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(true));
                repaint();
            }
        });
        dsa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(false));
                repaint();
            }
        });
        opts.add(sa);
        opts.add(dsa);
        selector.add(opts, BorderLayout.SOUTH);
        tabs.addTab("Areas", selector);
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from Bin");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(copyFrom);

        IconButton review = new IconButton("Review", "review", "Review Date");
        tb.add(Box.createHorizontalStrut(5));
        tb.add(review);

        IconButton start = new IconButton("Start AutoMake", "automake", "Start AutoMake");
        start.addActionListener(_ -> {
            dispose();
            for (JCheckBox checkbox : checkboxes) {

                if (checkbox.isSelected()) {

                    Area a = Engine.getArea(checkbox.getActionCommand());
                    if(a != null){

                        for(int i = 1; i <= Integer.parseInt(binCount.getText()); i++){

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
                            a.addBin(b);
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

    private JPanel general(){

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));

        idField = Elements.input("@-BN+", 15);
        nameField = Elements.input("@-BIN+");
        binCount = Elements.input("1");

        Form f = new Form();
        f.addInput(Elements.coloredLabel("Bin ID (current: BN1-IBD1)", Constants.colors[10]), idField);
        f.addInput(Elements.coloredLabel("Bin Name (current: BIN1-IBD1)", Constants.colors[9]), nameField);
        f.addInput(Elements.coloredLabel("Bin Create Count", Constants.colors[8]), binCount);
        p.add(f);

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

    private JPanel dimensional(){

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
}