package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Bins.CreateBin;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ViewArea extends LockeState {

    private Area area;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    public ViewArea(Area area, DesktopState desktop, RefreshListener refreshListener) {

        super("View Area", "/AREAS/" + area.getId());
        setFrameIcon(new ImageIcon(CreateArea.class.getResource("/icons/areas.png")));
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

        Form f = new Form();
        f.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(area.getId()));
        f.addInput(Elements.coloredLabel("Location", UIManager.getColor("Label.foreground")), new Copiable(area.getLocation()));
        f.addInput(Elements.coloredLabel("Name", UIManager.getColor("Label.foreground")), new Copiable(area.getName()));
        f.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(area.getStatus())));
        f.addInput(Elements.coloredLabel("Width", UIManager.getColor("Label.foreground")), widthField);
        f.addInput(Elements.coloredLabel("Length", UIManager.getColor("Label.foreground")), lengthField);
        f.addInput(Elements.coloredLabel("Height", UIManager.getColor("Label.foreground")), heightField);
        f.addInput(Elements.coloredLabel("Area", UIManager.getColor("Label.foreground")), areaField);
        f.addInput(Elements.coloredLabel("Volume", UIManager.getColor("Label.foreground")), volumeField);
        panel.add(f, BorderLayout.CENTER);

        return panel;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form f = new Form();
        f.addInput(Elements.coloredLabel("Allow Inventory", UIManager.getColor("Label.foreground")), new JCheckBox("Inventory Movements", area.allowsInventory()));
        f.addInput(Elements.coloredLabel("Allow Production", UIManager.getColor("Label.foreground")), new JCheckBox("Allows Production", area.allowsProduction()));
        f.addInput(Elements.coloredLabel("Allow Sales", UIManager.getColor("Label.foreground")), new JCheckBox("Sales Order Processing", area.allowsSales()));
        f.addInput(Elements.coloredLabel("Allow Purchasing", UIManager.getColor("Label.foreground")), new JCheckBox("Purchase Order Processing", area.allowsPurchasing()));
        controls.add(f);

        return controls;
    }

    private JPanel bins(){

        JPanel bins = new JPanel();

        return bins;
    }
}
