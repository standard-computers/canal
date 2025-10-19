package org.Canal.UI.Views.Invoices;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Areas.AutoMakeAreas;
import org.Canal.UI.Views.Areas.CreateArea;
import org.Canal.UI.Views.Areas.ViewArea;
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
 * /INVS
 */
public class Invoices extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private JPanel holder;
    private JComponent headerComp;
    private JScrollPane tableScroll;
    private CustomTable table;
    private ArrayList<Area> areas;

    public Invoices(DesktopState desktop) {
        
        super("Invoices", "/INVS");
        setFrameIcon(new ImageIcon(Invoices.class.getResource("/icons/windows/locke.png")));
        this.desktop = desktop;

        areas = Engine.getAreas();

        holder = new JPanel(new BorderLayout());
        headerComp = Elements.header("Invoices", SwingConstants.LEFT);
        holder.add(headerComp, BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);

        table = buildTable();
        tableScroll = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);

        add(tableScroll, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("INVS", "start_maximized")) {
            setMaximized(true);
        }
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if ((boolean) Engine.codex.getValue("INVS", "import_enabled")) {
            IconButton importAreas = new IconButton("Import", "export", "Import from CSV", "");
            tb.add(importAreas);
            tb.add(Box.createHorizontalStrut(5));
        }

        if ((boolean) Engine.codex.getValue("INVS", "export_enabled")) {
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

        IconButton createArea = new IconButton("Create", "create", "Create a Area", "/INVS/NEW");
        createArea.addActionListener(_ -> desktop.put(new CreateArea(null, desktop, this)));
        tb.add(createArea);
        tb.add(Box.createHorizontalStrut(5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Area(s)", "/INVS/DEL");
        delete.addActionListener(_ -> {
            if (table.isEditing()) table.getCellEditor().stopCellEditing();

            java.util.List<Integer> checkedViewRows = new ArrayList<>();
            for (int viewRow = 0; viewRow < table.getRowCount(); viewRow++) {
                if (Boolean.TRUE.equals(table.getValueAt(viewRow, 0))) {
                    checkedViewRows.add(viewRow);
                }
            }

            if (checkedViewRows.isEmpty()) {
                desktop.put(new Deleter("/INVS", this));
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
                    Pipe.delete("/INVS", areaId);
                }
                refresh();
            }
        });
        tb.add(delete);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeAreas = new IconButton("AutoMake", "automake", "Automate the creation of areas", "/INVS/AUTO_MK");
        autoMakeAreas.addActionListener(_ -> desktop.put(new AutoMakeAreas(desktop, this)));
        tb.add(autoMakeAreas);
        tb.add(Box.createHorizontalStrut(5));

        IconButton makeBin = new IconButton("Make a Bin", "bins", "Make a single Bin", "/BNS/NEW");
        makeBin.addActionListener(_ -> desktop.put(new CreateBin(null, desktop, this)));
        tb.add(makeBin);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeBins = new IconButton("AutoMake Bins", "automake", "Automate Bins", "/BNS/AUTO_MK");
        autoMakeBins.addActionListener(_ -> desktop.put(new AutoMakeBins(desktop)));
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
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for (Area area : areas) {
            data.add(new Object[]{

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
                                desktop.put(new ViewArea(d, desktop, Invoices.this));
                                break;
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
        
        areas = Engine.getAreas();
        CustomTable newTable = buildTable();
        tableScroll.setViewportView(newTable);
        table = newTable;
        tableScroll.revalidate();
        tableScroll.repaint();
    }
}
