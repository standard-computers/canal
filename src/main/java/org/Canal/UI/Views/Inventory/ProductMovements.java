package org.Canal.UI.Views.Inventory;

import org.Canal.Models.SupplyChainUnits.MaterialMovement;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
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
 *
 */
public class ProductMovements extends LockeState implements RefreshListener {

    private String location;
    private CustomTable table;

    public ProductMovements(DesktopState desktop, String location) {
        super("Product Movements in Stock", "/STK", true, true, true, true);
        this.location = location;
        setFrameIcon(new ImageIcon(ProductMovements.class.getResource("/icons/purchasereqs.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header(location + " Product Movements", SwingConstants.LEFT), BorderLayout.NORTH);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        isMaximized();
    }

    private CustomTable createTable() {
        String[] columns = new String[]{"Objex", "user", "Src. Bin", "Dest. Bin", "Src. HU", "Dest. HU", "Timestamp", "Type", "Status"};
        ArrayList<Object[]> stks = new ArrayList<>();
        for (MaterialMovement mm : Engine.getInventory(location).getMaterialMovements()) {
            stks.add(new String[]{
                    mm.getObjex(),
                    mm.getUser(),
                    mm.getSourceBin(),
                    mm.getDestinationBin(),
                    mm.getSourceHu(),
                    mm.getDestinationHu(),
                    mm.getTimestamp(),
                    mm.getType(),
                    String.valueOf(mm.getStatus())
            });
        }
        return new CustomTable(columns, stks);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        JTextField filterValue = Elements.input(location, 10);
        tb.add(export);
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

    @Override
    public void onRefresh() {
        CustomTable newTable = createTable();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}