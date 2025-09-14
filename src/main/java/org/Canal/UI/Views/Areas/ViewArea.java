package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Bins.CreateBin;
import org.Canal.UI.Views.Bins.ViewBin;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ViewArea extends LockeState implements RefreshListener {

    private Area area;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    public ViewArea(Area area, DesktopState desktop, RefreshListener refreshListener) {

        super("View Area", "/AREAS/" + area.getId());
        setFrameIcon(new ImageIcon(CreateArea.class.getResource("/icons/windows/areas.png")));
        this.area = area;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());
        tabs.addTab("Activity", bins());
        tabs.addTab("Bins", bins());

        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    public JPanel toolbar() {

        JPanel panel = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();

        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton modify = new IconButton("Modify", "modify", "Modify Area", "/AREAS/MOD");
        modify.addActionListener(_ -> {
            desktop.put(new ModifyArea(area, refreshListener));
            dispose();
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

        IconButton makeBin = new IconButton("+ Bin", "bins", "Add a Bin", "/BNS/NEW");
        makeBin.addActionListener(_ -> desktop.put(new CreateBin(null, refreshListener)));
        tb.add(makeBin);
        tb.add(Box.createHorizontalStrut(5));

        IconButton archive;
        if(area.getStatus().equals(LockeStatus.ARCHIVED) || area.getStatus().equals(LockeStatus.NEW)){

            archive = new IconButton("Activate", "start", "Activate Area");
            archive.addActionListener(_ -> {
                area.setStatus(LockeStatus.ACTIVE);
                area.save();
                dispose();
                if(refreshListener != null){
                    refreshListener.refresh();
                }
            });
        } else {

            archive = new IconButton("Archive", "archive", "Archive Area", "/AREAS/ARCHV");
            archive.addActionListener(_ -> {
                area.setStatus(LockeStatus.ARCHIVED);
                area.save();
                dispose();
                if(refreshListener != null){
                    refreshListener.refresh();
                }
            });
        }
        tb.add(archive);
        tb.add(Box.createHorizontalStrut(5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Area", "/AREAS/DEL");
        delete.addActionListener(_ -> {
            if(area.getBins().isEmpty()) {

            } else {

            }
        });
        tb.add(delete);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));
        panel.add(Elements.header(area.getName() + " - " + area.getId(), SwingConstants.LEFT), BorderLayout.CENTER);
        panel.add(tb, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel general() {

        JPanel panel = new JPanel(new BorderLayout());

        UOMField widthField = new UOMField();
        widthField.setValue(String.valueOf(area.getWidth()));
        widthField.setUOM(area.getWidthUOM());
        widthField.disable();

        UOMField lengthField = new UOMField();
        lengthField.setValue(String.valueOf(area.getLength()));
        lengthField.setUOM(area.getLengthUOM());
        lengthField.disable();

        UOMField heightField = new UOMField();
        heightField.setValue(String.valueOf(area.getHeight()));
        heightField.setUOM(area.getHeightUOM());
        heightField.disable();

        UOMField areaField = new UOMField();
        areaField.setValue(String.valueOf(area.getArea()));
        areaField.setUOM(area.getAreaUOM());
        areaField.disable();

        UOMField volumeField = new UOMField();
        volumeField.setValue(String.valueOf(area.getVolume()));
        volumeField.setUOM(area.getVolumeUOM());
        volumeField.disable();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(area.getId()));
        form.addInput(Elements.coloredLabel("Location", UIManager.getColor("Label.foreground")), new Copiable(area.getLocation()));
        form.addInput(Elements.coloredLabel("Name", UIManager.getColor("Label.foreground")), new Copiable(area.getName()));
        form.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(area.getStatus())));
        form.addInput(Elements.coloredLabel("Width", UIManager.getColor("Label.foreground")), widthField);
        form.addInput(Elements.coloredLabel("Length", UIManager.getColor("Label.foreground")), lengthField);
        form.addInput(Elements.coloredLabel("Height", UIManager.getColor("Label.foreground")), heightField);
        form.addInput(Elements.coloredLabel("Area", UIManager.getColor("Label.foreground")), areaField);
        form.addInput(Elements.coloredLabel("Volume", UIManager.getColor("Label.foreground")), volumeField);
        panel.add(form, BorderLayout.CENTER);

        return panel;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));


        JCheckBox allowsInventory = new JCheckBox("Inventory Movements", area.allowsInventory());
        allowsInventory.setEnabled(false);
        JCheckBox allowsProduction = new JCheckBox("Production", area.allowsProduction());
        allowsProduction.setEnabled(false);
        JCheckBox allowsSales = new JCheckBox("Sales", area.allowsSales());
        allowsSales.setEnabled(false);
        JCheckBox allowsPurchasing = new JCheckBox("Purchasing", area.allowsPurchasing());
        allowsPurchasing.setEnabled(false);

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Allow Inventory", UIManager.getColor("Label.foreground")), allowsInventory);
        form.addInput(Elements.coloredLabel("Allow Production", UIManager.getColor("Label.foreground")), allowsProduction);
        form.addInput(Elements.coloredLabel("Allow Sales", UIManager.getColor("Label.foreground")), allowsSales);
        form.addInput(Elements.coloredLabel("Allow Purchasing", UIManager.getColor("Label.foreground")), allowsPurchasing);
        controls.add(form);

        return controls;
    }

    private JScrollPane bins(){

        JPanel bins = new JPanel();

        String[] columns = new String[]{
                "ID",
                "Area",
                "Name",
                "Width",
                "wUOM",
                "Length",
                "lUOM",
                "Height",
                "hUOM",
                "Area",
                "aUOM",
                "Volume",
                "vUOM",
                "Weight",
                "wtUOM",
                "Auto Repl",
                "Fixed",
                "Picking",
                "Putaway",
                "GI",
                "GR",
                "Holds Stock",
                "Status",
                "Created",
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for (Bin b : Engine.getArea(area.getId()).getBins()) {
            //TODO remove extra call to are some how
            data.add(new Object[]{
                    b.getId(),
                    b.getArea(),
                    b.getName(),
                    b.getWidth(),
                    b.getWidthUOM(),
                    b.getLength(),
                    b.getLengthUOM(),
                    b.getHeight(),
                    b.getHeightUOM(),
                    b.getAreaValue(),
                    b.getAreaUOM(),
                    b.getVolume(),
                    b.getVolumeUOM(),
                    b.getWeight(),
                    b.getWeightUOM(),
                    b.isAuto_replenish(),
                    b.isFixed(),
                    b.isPicking(),
                    b.isPutaway(),
                    b.doesGI(),
                    b.doesGR(),
                    b.isHoldsStock(),
                    b.getStatus(),
                    b.getCreated(),
            });
        }

        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));
                        for (Area area : Engine.getAreas()) {
                            for (Bin bin : area.getBins()) {
                                if (v.equals(bin.getId())) {
                                    bin.setArea(area.getId());
                                    desktop.put(new ViewBin(bin, desktop, null));
                                }
                            }
                        }
                    }
                }
            }
        });

        return new JScrollPane(ct);
    }

    @Override
    public void refresh(){

    }
}
