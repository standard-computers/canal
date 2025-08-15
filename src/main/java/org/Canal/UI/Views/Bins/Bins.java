package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /BNS
 */
public class Bins extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;
    private String binCountString = "All Bins";

    public Bins(DesktopState desktop) {

        super("Bins", "/BNS", true, true, true, true);
        setFrameIcon(new ImageIcon(Bins.class.getResource("/icons/bins.png")));
        this.desktop = desktop;
        setMaximized(true);

        JPanel tb = toolbar();
        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header(binCountString, SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton importBins = new IconButton("Import", "export", "Import as CSV", "");
        tb.add(importBins);
        tb.add(Box.createHorizontalStrut(5));

        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));

        IconButton open = new IconButton("Open", "open", "Open Bin");
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create a Bin", "/BNS/NEW");
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeBins = new IconButton("AutoMake", "automake", "Automate the creation of Bin(s)", "/BNS/AUTO_MK");
        tb.add(autoMakeBins);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "label", "Delete an Bin");
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Delete an Bin");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));


        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(e -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    private CustomTable table() {
        String[] columns = new String[]{
            "ID",
            "Location",
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
        int bin_count = 0;
        ArrayList<Object[]> data = new ArrayList<>();
        for (Area area : Engine.getAreas()) {
            for(Bin b : area.getBins()){
                bin_count++;
                Area a = new Area();
                ArrayList<Area> areas = Engine.getAreas();
                for(int i = 0; i < areas.size(); i++){
                    for(int g = 0; g < areas.get(i).getBins().size(); g++){
                        if(areas.get(i).getBins().get(g).getId().equals(b.getId())) {
                            a = areas.get(i);
                        }
                    }
                }
                data.add(new Object[]{
                        b.getId(),
                        a.getLocation(),
                        a.getId(),
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
        }
        binCountString = "All Bins (" + bin_count + ")";
        CustomTable t = new CustomTable(columns, data);
        t.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));
                        for(Area d : Engine.getAreas()){
                            for(Bin b : d.getBins()){
                                if(v.equals(b.getId())){
                                    desktop.put(new ViewBin(b));
                                }
                            }
                        }
                    }
                }
            }
        });
        return t;
    }

    @Override
    public void refresh() {

        CustomTable newTable = table();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}