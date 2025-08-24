package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Bins.AutoMakeBins;
import org.Canal.UI.Views.Bins.CreateBin;
import org.Canal.UI.Views.Deleter;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
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
    private JPanel holder;
    private JComponent headerComp;
    private JScrollPane tableScroll;
    private CustomTable table;
    private ArrayList<Area> areas;

    public Areas(DesktopState desktop) {
        super("Areas", "/AREAS", true, true, true, true);
        setFrameIcon(new ImageIcon(Areas.class.getResource("/icons/areas.png")));
        this.desktop = desktop;

        // initial data
        areas = Engine.getAreas();

        // ----- NORTH: header + toolbar
        holder = new JPanel(new BorderLayout());
        headerComp = Elements.header(headerText(areas.size()), SwingConstants.LEFT);
        holder.add(headerComp, BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);

        // ----- CENTER: table + details
        table = buildTable();
        tableScroll = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);

        JSplitPane splitPane =
                new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, details());
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("AREAS", "start_maximized")) {
            setMaximized(true);
        }
    }

    private static String headerText(int count) {
        return "Areas (" + String.format("%,d", count) + ")";
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if ((boolean) Engine.codex.getValue("AREAS", "import_enabled")) {
            IconButton importAreas = new IconButton("Import", "export", "Import from CSV", "");
            tb.add(importAreas);
            tb.add(Box.createHorizontalStrut(5));
        }

        if ((boolean) Engine.codex.getValue("AREAS", "export_enabled")) {
            IconButton export = new IconButton("Export", "export", "Export as CSV", "");
            export.addActionListener(_ -> table.exportToCSV());
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton openSelected = new IconButton("Open", "open", "Open selected");
        openSelected.addActionListener(_ -> {
            // Prefer the currently selected row; fall back to prompt
            int viewRow = table.getSelectedRow();
            String id = null;
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                id = String.valueOf(table.getModel().getValueAt(modelRow, 1)); // col 0 = checkbox, 1 = ID
            } else {
                id = JOptionPane.showInputDialog("Area ID", "Area ID");
            }
            if (id != null && !id.isBlank()) {
                for (Area a : areas) {
                    if (id.equals(String.valueOf(a.getId()))) {
                        desktop.put(new ViewArea(a, desktop, this));
                        break;
                    }
                }
            }
        });
        tb.add(openSelected);
        tb.add(Box.createHorizontalStrut(5));

        IconButton createArea = new IconButton("Create", "create", "Create a Area", "/AREAS/NEW");
        createArea.addActionListener(_ -> desktop.put(new CreateArea(null, desktop, this)));
        tb.add(createArea);
        tb.add(Box.createHorizontalStrut(5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Area(s)", "/AREAS/DEL");
        delete.addActionListener(_ -> {
            if (table.isEditing()) table.getCellEditor().stopCellEditing();

            java.util.List<Integer> checkedViewRows = new java.util.ArrayList<>();
            for (int viewRow = 0; viewRow < table.getRowCount(); viewRow++) {
                if (Boolean.TRUE.equals(table.getValueAt(viewRow, 0))) {
                    checkedViewRows.add(viewRow);
                }
            }

            if (checkedViewRows.isEmpty()) {
                desktop.put(new Deleter("/AREAS", this));
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    checkedViewRows.size() + " Areas selected for deletion",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                for (int viewRow : checkedViewRows) {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    String areaId = String.valueOf(table.getModel().getValueAt(modelRow, 1)); // 1 = ID
                    Pipe.delete("/AREAS", areaId);
                }
                refresh();
            }
        });
        tb.add(delete);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeAreas = new IconButton("AutoMake", "automake", "Automate the creation of areas", "/AREAS/AUTO_MK");
        autoMakeAreas.addActionListener(_ -> desktop.put(new AutoMakeAreas(this)));
        tb.add(autoMakeAreas);
        tb.add(Box.createHorizontalStrut(5));

        IconButton makeBin = new IconButton("Make a Bin", "bins", "Make a single Bin", "/BNS/NEW");
        makeBin.addActionListener(_ -> desktop.put(new CreateBin(null, this)));
        tb.add(makeBin);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeBins = new IconButton("AutoMake Bins", "automake", "Automate Bins", "/BNS/AUTO_MK");
        autoMakeBins.addActionListener(_ -> desktop.put(new AutoMakeBins()));
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

    private CustomTable buildTable() {
        String[] columns = new String[]{
                "ID", "Location", "Name",
                "Width", "Width UOM",
                "Length", "Length UOM",
                "Height", "Height UOM",
                "Area", "Area UOM",
                "Volume", "Volume UOM",
                "Σ Bins", "Status", "Created"
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for (Area area : areas) {
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
                    JTable jt = (JTable) e.getSource();
                    int viewRow = jt.getSelectedRow();
                    if (viewRow != -1) {
                        int modelRow = jt.convertRowIndexToModel(viewRow);
                        String id = String.valueOf(jt.getModel().getValueAt(modelRow, 1)); // 1 = ID
                        for (Area d : areas) {
                            if (String.valueOf(d.getId()).equals(id)) {
                                desktop.put(new ViewArea(d, desktop, Areas.this));
                                break;
                            }
                        }
                    }
                }
            }
        });
        return t;
    }

    private CustomTabbedPane details() {
        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Bins", new JPanel());
        tabs.addTab("Inventory", new JPanel());
        tabs.addTab("Activity", new JPanel());
        tabs.addTab("Tasks", new JPanel());
        return tabs;
    }

    @Override
    public void refresh() {
        // 1) data
        areas = Engine.getAreas();

        // 2) update header (rebuild the component to avoid type issues)
        holder.remove(headerComp);
        headerComp = Elements.header(headerText(areas.size()), SwingConstants.LEFT);
        holder.add(headerComp, BorderLayout.CENTER);

        // 3) rebuild table and swap into the existing scroll pane
        CustomTable newTable = buildTable();
        tableScroll.setViewportView(newTable);
        table = newTable;

        // 4) layout refresh
        holder.revalidate();
        holder.repaint();
        tableScroll.revalidate();
        tableScroll.repaint();
    }
}
