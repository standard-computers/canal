package org.Canal.UI.Views.Products.Materials;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
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
 * /MTS
 */
public class Materials extends LockeState implements RefreshListener {

    private CustomTable table;

    public Materials(DesktopState desktop) {

        super("Materials", "/MTS", true, true, true, true);
        setFrameIcon(new ImageIcon(Materials.class.getResource("/icons/materials.png")));

        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1300, 600));
        holder.add(Elements.header("Materials", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int row = t.getSelectedRow();
                    if (row != -1) {
                        String v = String.valueOf(t.getValueAt(row, 1));
                        desktop.put(new ViewMaterial(Engine.getMaterial(v)));
                    }
                }
            }
        });
    }

    private JPanel createToolBar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton createMaterial = new IconButton("New", "order", "Create a Material", "/MTS/NEW");
        IconButton modifyMaterial = new IconButton("Modify", "modify", "Modify a Material", "/MTS/MOD");
        IconButton archiveMaterial = new IconButton("Archive", "archive", "Archive a Material", "/MTS/ARCHV");
        IconButton removeMaterial = new IconButton("Remove", "delete", "Delete a Material", "/MTS/DEL");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createMaterial);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyMaterial);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveMaterial);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeMaterial);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
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
            "ID",
            "Org",
            "Vendor",
            "Vendor Name",
            "Name",
            "Color",
            "UPC",
            "Base Qty",
            "Packaging Unit",
            "Batched",
            "Rentable",
            "SKU'd",
            "Price",
            "Width",
            "wUOM",
            "Length",
            "lUOM",
            "Height",
            "hUOM",
            "Weight",
            "wtUOM",
            "Tax",
            "Excise Tax",
            "Status",
            "Created",
        };
        ArrayList<Object[]> d = new ArrayList<>();
        for (Item material : Engine.getMaterials()) {
            Location vendor = Engine.getLocation(material.getVendor(), "VEND");
            d.add(new Object[]{
                    material.getId(),
                    material.getOrg(),
                    material.getVendor(),
                    vendor.getName(),
                    material.getName(),
                    material.getColor(),
                    material.getUpc(),
                    material.getBaseQuantity(),
                    material.getPackagingUnit(),
                    material.isBatched(),
                    material.isRentable(),
                    material.isSkud(),
                    material.getPrice(),
                    material.getWidth(),
                    material.getWidthUOM(),
                    material.getLength(),
                    material.getLengthUOM(),
                    material.getHeight(),
                    material.getHeightUOM(),
                    material.getWeight(),
                    material.getWeightUOM(),
                    material.getTax(),
                    material.getExciseTax(),
                    material.getStatus(),
                    material.getCreated(),
            });
        }
        return new CustomTable(columns, d);
    }

    @Override
    public void onRefresh() {

    }
}