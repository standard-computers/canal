package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /AREAS
 */
public class Areas extends JInternalFrame {

    private CustomTable table;

    public Areas() {
        super("/AREAS", true, true, true, true);
        setFrameIcon(new ImageIcon(Areas.class.getResource("/icons/areas.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Areas", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("", "export", "Export as CSV", "");
        IconButton createArea = new IconButton("New Area", "order", "Create a Area", "/AREAS/NEW");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createArea);
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
            "ID", "Location", "Name", "Width",
            "Width UOM", "Length", "Length UOM",
            "Height", "Height UOM", "Area", "Area UOM",
            "Volume", "Volume UOM", "Status"
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
                    area.getStatus(),
            });
        }
        return new CustomTable(columns, data);
    }
}