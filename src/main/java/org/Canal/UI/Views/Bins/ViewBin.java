package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ViewBin extends LockeState {

    //Operating Objects
    private Bin bin;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //Dimensional Tab
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField weightField;

    public ViewBin(Bin bin, DesktopState desktop, RefreshListener refreshListener) {

        super("View Bin", "/BNS/" + bin.getId());
        this.bin = bin;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());
        tabs.addTab("Controls", controls());
        tabs.addTab("Item Restrictions", restrictions());
        tabs.addTab("Stock", stock());
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

        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton modify = new IconButton("Modify", "modify", "Modify Bin", "/BNS/MOD");
        modify.addActionListener(_ -> {
            dispose();
            desktop.put(new ModifyBin(bin, desktop, refreshListener));
        });
        tb.add(modify);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_E, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-modify");
        rp.getActionMap().put("do-modify", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modify.doClick();
            }
        });

        IconButton block = new IconButton("Block", "block", "Block Bin", "/BNS/BLK");
        block.addActionListener(_ -> {

            bin.setStatus(LockeStatus.BLOCKED);
            bin.save();
            dispose();
            if (refreshListener != null) refreshListener.refresh();
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

        toolbar.add(Elements.header(bin.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String locationId = Engine.getArea(bin.getArea()).getLocation();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Bin ID"), new Copiable(bin.getId()));
        form.addInput(Elements.inputLabel("Location"), new Copiable(locationId));
        form.addInput(Elements.inputLabel("Area"), new Copiable(bin.getArea()));
        form.addInput(Elements.inputLabel("Bin Name"), new Copiable(bin.getName()));
        form.addInput(Elements.inputLabel("Status"), new Copiable(String.valueOf(bin.getStatus())));
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
        form.addInput(Elements.inputLabel("Width"), widthField);
        form.addInput(Elements.inputLabel("Length"), lengthField);
        form.addInput(Elements.inputLabel("Height"), heightField);
        form.addInput(Elements.inputLabel("Weight"), weightField);
        dimensional.add(form);

        return dimensional;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.inputLabel("Goods Issue"), new Copiable(String.valueOf(bin.doesGI())));
        form.addInput(Elements.inputLabel("Goods Receipt"), new Copiable(String.valueOf(bin.doesGR())));
        form.addInput(Elements.inputLabel("Picking Enabled"), new Copiable(String.valueOf(bin.pickingEnabled())));
        form.addInput(Elements.inputLabel("Putaway Enabled"), new Copiable(String.valueOf(bin.putawayEnabled())));
        form.addInput(Elements.inputLabel("Auto Replenish"), new Copiable(String.valueOf(bin.isAuto_replenish())));
        form.addInput(Elements.inputLabel("Fixed Bin"), new Copiable(String.valueOf(bin.isFixed())));
        form.addInput(Elements.inputLabel("Holds Stock"), new Copiable(String.valueOf(bin.holdsStock())));
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

    private RTextScrollPane notes() {

        RTextScrollPane notes = Elements.simpleEditor();
        notes.getTextArea().setText(bin.getNotes());
        notes.getTextArea().setEditable(false);
        return notes;
    }
}