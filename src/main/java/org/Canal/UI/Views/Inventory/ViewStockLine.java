package org.Canal.UI.Views.Inventory;

import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Bins.ViewBin;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /STL/$[STL_ID]
 */

public class ViewStockLine extends LockeState implements RefreshListener {

    private StockLine stockLine;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    public ViewStockLine(StockLine stockLine, DesktopState desktop, RefreshListener refreshListener) {

        super("View StockLine", "/STL/" + stockLine.getId(), false, true, false, false);
        setFrameIcon(new ImageIcon(ViewStockLine.class.getResource("/icons/windows/areas.png")));
        this.stockLine = stockLine;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Activity", activity());
        tabs.addTab("Notes", notes());

        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    public JPanel toolbar() {

        JPanel panel = new JPanel(new BorderLayout());
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton makeBin = new IconButton("Direct Move", "bins", "Direct move HU to Bin", "/STK/MV/DFULL");
        makeBin.addActionListener(_ -> {
            DirectedMoveToBinFull dmtbf = new DirectedMoveToBinFull(refreshListener);
            dmtbf.setHu(stockLine.getHu());
            desktop.put(dmtbf);
            dispose();
        });
        tb.add(makeBin);
        tb.add(Box.createHorizontalStrut(5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Area", "/AREAS/DEL");
        delete.addActionListener(_ -> {

        });
        tb.add(delete);
        tb.add(Box.createHorizontalStrut(5));

        panel.add(Elements.header(stockLine.getId() + " – " + stockLine.getHu(), SwingConstants.LEFT), BorderLayout.CENTER);
        panel.add(tb, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel general() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(stockLine.getId()));
        form.addInput(Elements.coloredLabel("HU", UIManager.getColor("Label.foreground")), new Copiable(stockLine.getHu()));
        form.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(stockLine.getStatus())));
        panel.add(form, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane activity() {

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
//        for (Bin b : Engine.getBinsForArea(area.getId())) {
//            //TODO remove extra call to are some how
//            data.add(new Object[]{
//                    b.getId(),
//                    b.getArea(),
//                    b.getName(),
//                    b.getWidth(),
//                    b.getWidthUOM(),
//                    b.getLength(),
//                    b.getLengthUOM(),
//                    b.getHeight(),
//                    b.getHeightUOM(),
//                    b.getAreaValue(),
//                    b.getAreaUOM(),
//                    b.getVolume(),
//                    b.getVolumeUOM(),
//                    b.getWeight(),
//                    b.getWeightUOM(),
//                    b.isAuto_replenish(),
//                    b.isFixed(),
//                    b.pickingEnabled(),
//                    b.putawayEnabled(),
//                    b.doesGI(),
//                    b.doesGR(),
//                    b.holdsStock(),
//                    b.getStatus(),
//                    b.getCreated(),
//            });
//        }

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
                        desktop.put(new ViewBin(bin, desktop, null));
                    }
                }
            }
        });
        return new JScrollPane(ct);
    }

    private RTextScrollPane notes() {

        RTextScrollPane notes = Elements.simpleEditor();
        notes.getTextArea().setText(stockLine.getNotes());
        notes.getTextArea().setEditable(false);
        return notes;
    }

    @Override
    public void refresh() {

    }
}
