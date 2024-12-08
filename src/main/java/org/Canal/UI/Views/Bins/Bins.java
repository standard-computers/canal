package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
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

    private CustomTable table;

    public Bins() {
        super("Bins", "/BNS", true, true, true, true);
        setFrameIcon(new ImageIcon(Bins.class.getResource("/icons/bins.png")));
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
        IconButton createArea = new IconButton("New Bin", "order", "Create a Bin", "/BNS/NEW");
        IconButton autoMakeAreas = new IconButton("AutoMake Bins", "automake", "Automate the creation of Bin(s)", "/BNS/AUTO_MK");
        IconButton modifyArea = new IconButton("Modify", "modify", "Modify an Bin", "/BNS/MOD");
        IconButton removeArea = new IconButton("Remove", "delete", "Delete an Bin", "/BNS/DEL");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createArea);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMakeAreas);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyArea);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeArea);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterValue);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        return tb;
    }

    private CustomTable createTable() {
        String[] columns = new String[]{
            "ID", "Area", "Name", "Width",
            "Width UOM", "Length", "Length UOM",
            "Height", "Height UOM", "Area", "Area UOM",
            "Volume", "Volume UOM", "Status"
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