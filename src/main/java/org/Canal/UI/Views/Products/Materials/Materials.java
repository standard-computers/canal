package org.Canal.UI.Views.Products.Materials;

import org.Canal.Models.SupplyChainUnits.OrderLineItem;
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

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1300, 600));
        holder.add(Elements.header("Materials", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
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
                        desktop.put(new ViewMaterial(Engine.products.getMaterial(v)));
                    }
                }
            }
        });
    }

    private JScrollPane toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV", "");
        IconButton importMaterials = new IconButton("Import", "export", "Import as CSV", "");
        IconButton createMaterial = new IconButton("Create", "create", "Create a Material", "/MTS/NEW");
        IconButton modifyMaterial = new IconButton("Modify", "modify", "Modify a Material", "/MTS/MOD");
        IconButton archiveMaterial = new IconButton("Archive", "archive", "Archive a Material", "/MTS/ARCHV");
        IconButton removeMaterial = new IconButton("Remove", "delete", "Delete a Material", "/MTS/DEL");
        IconButton findMaterial = new IconButton("Find", "find", "Find by Values", "/MTS/F");
        IconButton labels = new IconButton("Labels", "label", "Print labels for selected..");
        IconButton print = new IconButton("Print", "print", "Print Selected...");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importMaterials);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createMaterial);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyMaterial);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveMaterial);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeMaterial);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(findMaterial);
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
        return Elements.scrollPane(tb);
    }

    private CustomTable table() {

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
        for (OrderLineItem material : Engine.products.getMaterials()) {
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
    public void refresh() {
        CustomTable newTable = table();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}