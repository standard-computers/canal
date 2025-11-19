package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
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

        super("Bins", "/BNS");
        this.desktop = desktop;

        JPanel tb = toolbar();
        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header(binCountString, SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue("BNS", "start_maximized")) {
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        if ((boolean) Engine.codex.getValue("BNS", "import_enabled")) {
            IconButton importBins = new IconButton("Import", "import", "Import as CSV", "");
            tb.add(importBins);
            tb.add(Box.createHorizontalStrut(5));
        }

        if ((boolean) Engine.codex.getValue("BNS", "export_enabled")) {
            IconButton export = new IconButton("Export", "export", "Export as CSV", "");
            export.addActionListener(_ -> table.exportToCSV());
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton open = new IconButton("Open", "open", "Open Bin");
        open.addActionListener(_ -> {
            String binId = JOptionPane.showInputDialog("Bin ID");
            Bin bin = Engine.getBin(binId);
            desktop.put(new ViewBin(bin, desktop, this));
        });
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create a Bin", "/BNS/NEW");
        create.addActionListener(_ -> desktop.put(new CreateBin(null, desktop, this)));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeBins = new IconButton("AutoMake", "automake", "Automate the creation of Bin(s)", "/BNS/AUTO_MK");
        autoMakeBins.addActionListener(_ -> desktop.put(new AutoMakeBins(desktop)));
        tb.add(autoMakeBins);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print Bin Labels");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }

    private CustomTable table() {

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

        int bin_count = 0;
        ArrayList<Object[]> data = new ArrayList<>();
        for (Bin b : Engine.getBins()) {
            bin_count++;
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
                    b.pickingEnabled(),
                    b.putawayEnabled(),
                    b.doesGI(),
                    b.doesGR(),
                    b.holdsStock(),
                    b.getStatus(),
                    b.getCreated(),
            });
        }

        binCountString = "All Bins (" + bin_count + ")";
        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));
                        Bin bin = Engine.getBin(v);
                        desktop.put(new ViewBin(bin, desktop, Bins.this));
                    }
                }
            }
        });
        return ct;
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