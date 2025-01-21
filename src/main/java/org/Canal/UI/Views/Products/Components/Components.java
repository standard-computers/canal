package org.Canal.UI.Views.Products.Components;

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
 * /CMPS
 */
public class Components extends LockeState implements RefreshListener {

    private CustomTable table;

    public Components(DesktopState desktop) {

        super("Components", "/CMPS", true, true, true, true);
        setFrameIcon(new ImageIcon(Components.class.getResource("/icons/components.png")));

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1300, 600));
        holder.add(Elements.header("Components", SwingConstants.LEFT), BorderLayout.CENTER);
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
                        desktop.put(new ViewComponent(Engine.products.getComponent(v)));
                    }
                }
            }
        });
    }

    private JScrollPane toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton importComponents = new IconButton("Import", "export", "Import as CSV", "");
        IconButton createComponent = new IconButton("Create", "create", "Create a Component", "/CMPS/NEW");
        IconButton modifyComponent = new IconButton("Modify", "modify", "Modify a Component", "/CMPS/MOD");
        IconButton archiveComponent = new IconButton("Archive", "archive", "Archive a Component", "/CMPS/ARCHV");
        IconButton removeComponent = new IconButton("Remove", "delete", "Delete a Component", "/CMPS/DEL");
        IconButton findComponent = new IconButton("Find", "find", "Find by Values", "/CMPS/F");
        IconButton labels = new IconButton("Labels", "label", "Print labels for selected..");
        IconButton print = new IconButton("Print", "print", "Print Selected...");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importComponents);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createComponent);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyComponent);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archiveComponent);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removeComponent);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(findComponent);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));
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
            "Pack. Unit",
            "Batched",
            "Rentable",
            "SKU'd",
            "Consumable",
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
            "Excise Tax"
        };
        ArrayList<Object[]> d = new ArrayList<>();
        for (OrderLineItem component : Engine.products.getComponents()) {
            Location vendor = Engine.getLocation(component.getVendor(), "VEND");
            d.add(new Object[]{
                    component.getId(),
                    component.getOrg(),
                    component.getVendor(),
                    vendor.getName(),
                    component.getName(),
                    component.getColor(),
                    component.getUpc(),
                    component.getBaseQuantity(),
                    component.getPackagingUnit(),
                    component.isBatched(),
                    component.isRentable(),
                    component.isSkud(),
                    component.isConsumable(),
                    component.getPrice(),
                    component.getWidth(),
                    component.getWidthUOM(),
                    component.getLength(),
                    component.getLengthUOM(),
                    component.getHeight(),
                    component.getHeightUOM(),
                    component.getWeight(),
                    component.getWeightUOM(),
                    component.getTax(),
                    component.getExciseTax()
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