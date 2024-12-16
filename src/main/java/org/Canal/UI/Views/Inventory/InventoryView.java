package org.Canal.UI.Views.Inventory;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.UI.Views.Controllers.CheckboxBarcodeFrame;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * /STK
 */
public class InventoryView extends LockeState implements RefreshListener {

    private String title, location;
    private CustomTable table;
    private DesktopState desktop;

    public InventoryView(DesktopState desktop, String location) {
        super("Location Inventory", "/STK", true, true, true, true);
        this.location = location;
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(InventoryView.class.getResource("/icons/purchasereqs.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1000, 800));
        title = location + " Inventory";
        holder.add(Elements.header(title, SwingConstants.LEFT), BorderLayout.NORTH);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        isMaximized();
        repaint();
        revalidate();
    }

    private CustomTable createTable() {
        String[] columns = new String[]{"Location", "Type", "HU", "ID", "Name", "Org. Qty.", "Qty.", "Price", "Value", "Area", "Bin", "Receipt", "Status"};
        ArrayList<Object[]> stks = new ArrayList<>();
        for (StockLine sl : Engine.getInventory(location).getStockLines()) {
            Item i = Engine.getItem(sl.getId());
            stks.add(new String[]{
                    location,
                    sl.getObjex(),
                    sl.getHu(),
                    sl.getId(),
                    i.getName(),
                    String.valueOf(sl.getQuantity()),
                    String.valueOf(sl.getQuantity()),
                    Constants.formatUSD(i.getPrice()),
                    Constants.formatUSD(i.getPrice() * sl.getQuantity()),
                    sl.getArea(),
                    sl.getBin(),
                    sl.getReceipt(),
                    String.valueOf(sl.getStatus())
            });
        }
        return new CustomTable(columns, stks);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton blockPo = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        IconButton move = new IconButton("Move", "start", "Move Inventory (Internally)");
        IconButton movements = new IconButton("Movements", "movements", "View stock movements");
        IconButton archivePo = new IconButton("Archive", "archive", "Archive PO, removes");
        IconButton label = new IconButton("Barcodes", "label", "Print labels for org properties");
        JTextField filterValue = Elements.input(location, 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blockPo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(move);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(movements);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archivePo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterValue);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        move.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new MoveStock(location, InventoryView.this));
            }
        });
        movements.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new ProductMovements(desktop, location));
            }
        });
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.orderProcessing.getPurchaseOrder().size()];
                for (int i = 0; i < Engine.orderProcessing.getPurchaseOrder().size(); i++) {
                    printables[i] = Engine.orderProcessing.getPurchaseOrder().get(i).getOrderId();
                }
                new CheckboxBarcodeFrame(printables);
            }
        });
        filterValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String searchValue = filterValue.getText().trim();
                    filterTableById(searchValue);
                }
            }
        });
        return tb;
    }

    private void filterTableById(String id) {
        String[] columns = new String[]{"Location", "Type", "HU", "ID", "Name", "Org. Qty.", "Qty.", "Price", "Value", "Area", "Bin", "Receipt", "Status"};
        ArrayList<Object[]> stks = new ArrayList<>();
        for (StockLine sl : Engine.getInventory(id).getStockLines()) {
            Item i = Engine.getItem(sl.getId());
            stks.add(new String[]{
                    id,
                    sl.getObjex(),
                    sl.getHu(),
                    sl.getId(),
                    i.getName(),
                    String.valueOf(sl.getQuantity()),
                    String.valueOf(sl.getQuantity()),
                    Constants.formatUSD(i.getPrice()),
                    Constants.formatUSD(i.getPrice() * sl.getQuantity()),
                    sl.getArea(),
                    sl.getBin(),
                    sl.getReceipt(),
                    String.valueOf(sl.getStatus())
            });
        }
        title = location + " Inventory";
        CustomTable filteredTable = new CustomTable(columns, stks);
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(filteredTable);
        table = filteredTable;
        scrollPane.revalidate();
        scrollPane.repaint();
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