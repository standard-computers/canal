package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /BNS
 */
public class Bins extends LockeState {

    private DesktopState desktop;
    private CustomTable table;

    public Bins(DesktopState desktop) {

        super("Bins", "/BNS", true, true, true, true);
        setFrameIcon(new ImageIcon(Bins.class.getResource("/icons/bins.png")));
        this.desktop = desktop;

        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("All Bins", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel createToolBar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton importBins = new IconButton("Import", "export", "Import as CSV", "");
        IconButton createBin = new IconButton("Create", "create", "Create a Bin", "/BNS/NEW");
        IconButton autoMakeBins = new IconButton("AutoMake", "automake", "Automate the creation of Bin(s)", "/BNS/AUTO_MK");
        IconButton modifyBin = new IconButton("Modify", "modify", "Modify an Bin", "/BNS/MOD");
        IconButton removeBin = new IconButton("Remove", "delete", "Delete an Bin");
        IconButton labels = new IconButton("Labels", "label", "Delete an Bin");
        IconButton print = new IconButton("Print", "print", "Delete an Bin");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importBins);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createBin);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMakeBins);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyBin);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeBin);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(print);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        removeBin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new RemoveBin());
            }
        });
        return tb;
    }

    private CustomTable createTable() {
        String[] columns = new String[]{
            "ID",
            "Area",
            "Name",
            "Width",
            "Width UOM",
            "Length",
            "Length UOM",
            "Height",
            "Height UOM",
            "Area",
            "Area UOM",
            "Volume",
            "Volume UOM",
            "Status"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Area area : Engine.getAreas()) {
            for(Bin b : area.getBins()){
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
                        b.getArea(),
                        b.getAreaUOM(),
                        b.getVolume(),
                        b.getVolumeUOM(),
                        b.getStatus(),
                });
            }
        }
        return new CustomTable(columns, data);
    }
}