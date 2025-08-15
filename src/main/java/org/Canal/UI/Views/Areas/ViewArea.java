package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.LockeStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ViewArea extends LockeState {

    private Area area;
    private Copiable areaIdField;
    private Selectable availableLocations;
    private Copiable areaNameField;
    private UOMField widthField;
    private UOMField lengthField;
    private UOMField heightField;
    private UOMField areaField;
    private UOMField volumeField;

    public ViewArea(Area area) {

        super("View Area", "/AREAS/" + area.getId(), false, true, false, true);
        setFrameIcon(new ImageIcon(CreateArea.class.getResource("/icons/areas.png")));
        this.area = area;

        add(Elements.header(area.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        add(content(), BorderLayout.CENTER);
    }

    public JPanel toolbar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton modify = new IconButton("Modify", "modify", "Modify Area");
        IconButton archive;
        if(area.getStatus().equals(LockeStatus.ARCHIVED) || area.getStatus().equals(LockeStatus.NEW)){
            archive = new IconButton("Activate", "start", "Activate Area");
            archive.addActionListener(e -> {
                area.setStatus(LockeStatus.ACTIVE);
                area.save();
                dispose();
            });
        } else {
            archive = new IconButton("Archive", "archive", "Archive Area");
            archive.addActionListener(e -> {
                area.setStatus(LockeStatus.ARCHIVED);
                area.save();
                dispose();
            });
        }

        IconButton delete = new IconButton("Remove", "delete", "Remove Area (Delete)");
        delete.addActionListener(e -> {
            if(area.getBins().isEmpty()) {

            } else {

            }
        });
        tb.add(modify);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archive);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(delete);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));
        panel.add(tb, BorderLayout.SOUTH);
        return panel;
    }

    public JPanel content() {
        JPanel panel = new JPanel(new BorderLayout());
        areaIdField = new Copiable(area.getId());
        availableLocations = Selectables.allLocations();
        areaNameField = new Copiable(area.getName());
        widthField = new UOMField();
        lengthField = new UOMField();
        heightField = new UOMField();
        areaField = new UOMField();
        volumeField = new UOMField();
        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), areaIdField);
        f.addInput(Elements.coloredLabel("*Location", UIManager.getColor("Label.foreground")), availableLocations);
        f.addInput(Elements.coloredLabel("Area Name", Constants.colors[10]), areaNameField);
        f.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[8]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[7]), heightField);
        f.addInput(Elements.coloredLabel("Area", Constants.colors[6]), areaField);
        f.addInput(Elements.coloredLabel("Volume", Constants.colors[6]), volumeField);
        panel.add(toolbar(), BorderLayout.NORTH);
        panel.add(f, BorderLayout.CENTER);
        return panel;
    }
}
