package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
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
 * /AREAS
 */
public class Areas extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Areas(DesktopState desktop) {

        super("Areas", "/AREAS", true, true, true, true);
        setFrameIcon(new ImageIcon(Areas.class.getResource("/icons/areas.png")));
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Areas", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, details());
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        if((boolean) Engine.codex.getValue("AREAS", "start_maximized")){
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue("AREAS", "import_enabled")){
            IconButton importAreas = new IconButton("Import", "export", "Import from CSV", "");
            tb.add(importAreas);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("AREAS", "export_enabled")){
            IconButton export = new IconButton("", "export", "Export as CSV", "");
            export.addActionListener(e -> table.exportToCSV());
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton openSelected = new IconButton("Open", "open", "Open selected");
        tb.add(openSelected);
        tb.add(Box.createHorizontalStrut(5));

        IconButton createArea = new IconButton("Create", "create", "Create a Area");
        createArea.addActionListener(e -> {
            desktop.put(new CreateArea(null, this));
        });
        tb.add(createArea);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeAreas = new IconButton("AutoMake", "automake", "Automate the creation of areas", "/AREAS/AUTO_MK");
        tb.add(autoMakeAreas);
        tb.add(Box.createHorizontalStrut(5));

        IconButton makeBin = new IconButton("Make a Bin", "bins", "Make a single Bin", "/BNS/NEW");
        tb.add(makeBin);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeBins = new IconButton("AutoMake Bins", "automake", "Automate Bins", "/BNS/AUTO_MK");
        tb.add(autoMakeBins);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "label", "Print barcode for an Area");
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selected");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

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
                            if(d.getId().equals(v)){
                                desktop.put(new ViewArea(d));
                            }
                        }
                    }
                }
            }
        });
        return t;
    }

    private CustomTabbedPane details(){

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Bins", new JPanel());
        tabs.addTab("Inventory", new JPanel());
        tabs.addTab("Activity", new JPanel());
        tabs.addTab("Tasks", new JPanel());
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