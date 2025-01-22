package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /AREAS
 */
public class Areas extends LockeState implements RefreshListener {

    private CustomTable table;

    public Areas() {
        super("Areas", "/AREAS", true, true, true, true);
        setFrameIcon(new ImageIcon(Areas.class.getResource("/icons/areas.png")));
        JPanel tb = toolbar();
        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Areas", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, details());
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton openSelected = new IconButton("Open", "open", "Open selected");
        IconButton createArea = new IconButton("Create", "create", "Create a Area", "/AREAS/NEW");
        IconButton autoMakeAreas = new IconButton("AutoMake", "automake", "Automate the creation of areas", "/AREAS/AUTO_MK");
        IconButton makeBins = new IconButton("Make Bins", "bins", "Make Bins", "/BNS/NEW");
        IconButton autoMakeBins = new IconButton("AutoMake Bins", "automake", "Automate Bins", "/BNS/AUTO_MK");
        IconButton modifyArea = new IconButton("Modify", "modify", "Modify an Area", "/AREAS/MOD");
        IconButton removeArea = new IconButton("Remove", "delete", "Delete an Area", "/AREAS/DEL");
        IconButton labels = new IconButton("Labels", "label", "Delete an Area");
        IconButton print = new IconButton("Print", "print", "Print selected");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(openSelected);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createArea);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMakeAreas);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(makeBins);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMakeBins);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyArea);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeArea);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        return tb;
    }

    private CustomTable table() {
        String[] columns = new String[]{
            "ID",
            "Location",
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
            "Σ Bins",
            "Status",
            "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Area area : Engine.getAreas()) {
            data.add(new Object[]{
                    area.getId(),
                    area.getLocation(),
                    area.getName(),
                    area.getWidth(),
                    area.getWidthUOM(),
                    area.getLength(),
                    area.getLengthUOM(),
                    area.getHeight(),
                    area.getHeightUOM(),
                    area.getArea(),
                    area.getAreaUOM(),
                    area.getVolume(),
                    area.getVolumeUOM(),
                    area.getBins().size(),
                    area.getStatus(),
                    area.getCreated(),
            });
        }
        return new CustomTable(columns, data);
    }

    private CustomTabbedPane details(){
        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Bins", new JPanel());
        return tabs;
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