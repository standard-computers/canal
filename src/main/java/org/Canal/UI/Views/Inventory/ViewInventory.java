package org.Canal.UI.Views.Inventory;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Areas.Areas;
import org.Canal.UI.Views.Areas.ViewArea;
import org.Canal.UI.Views.System.CheckboxBarcodeFrame;
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
public class ViewInventory extends LockeState implements RefreshListener {

    private String title, location;
    private CustomTable table;
    private DesktopState desktop;

    public ViewInventory(DesktopState desktop, String location) {

        super("Location Inventory", "/STK");
        setFrameIcon(new ImageIcon(ViewInventory.class.getResource("/icons/purchasereqs.png")));
        this.location = location;
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1000, 800));
        title = location + " Inventory";
        holder.add(Elements.header(title, SwingConstants.LEFT), BorderLayout.NORTH);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        setMaximized(true);
    }

    private CustomTable table() {

        String[] columns = new String[]{
                "Location",
                "HU",
                "ID",
                "Item",
                "Name",
                "Org. Qty.",
                "Qty.",
                "Price",
                "Value",
                "Total Weight",
                "wUOM",
                "Area",
                "Bin",
                "Receipt",
                "Status",
        };
        ArrayList<Object[]> stks = new ArrayList<>();
        for (StockLine sl : Engine.getInventory(location).getStockLines()) {
            Item i = Engine.getItem(sl.getItem());
            Bin b = Engine.getBin(sl.getBin());
            stks.add(new Object[]{
                    location,
                    sl.getHu(),
                    sl.getId(),
                    sl.getItem(),
                    i.getName(),
                    (sl.getQuantity()),
                    (sl.getQuantity()),
                    i.getPrice(),
                    sl.getValue(),
                    (sl.getQuantity() * i.getWeight()),
                    i.getWeightUOM(),
                    b.getArea(),
                    sl.getBin(),
                    sl.getReceipt(),
                    String.valueOf(sl.getStatus())
            });
        }
        CustomTable ct = new CustomTable(columns, stks);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable jt = (JTable) e.getSource();
                    int viewRow = jt.getSelectedRow();
                    if (viewRow != -1) {
                        int modelRow = jt.convertRowIndexToModel(viewRow);
                        String id = String.valueOf(jt.getModel().getValueAt(modelRow, 3));
                        for (StockLine sl : Engine.getInventory(location).getStockLines()) {
                            if (sl.getId().equals(id)) {
                                desktop.put(new ViewStockLine(sl, desktop, ViewInventory.this));
                            }
                        }
                    }
                }
            }
        });
        return ct;
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton export = new IconButton("Export", "export", "Export as CSV");
        export.addActionListener(_ -> table.exportToCSV());
        toolbar.add(export);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton move = new IconButton("Move", "start", "Move Inventory (Internally)");
        move.addActionListener(_ -> desktop.put(new MoveStock(location, ViewInventory.this)));
        toolbar.add(move);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton movements = new IconButton("Movements", "movements", "View stock movements");
        movements.addActionListener(_ -> desktop.put(new ProductMovements(desktop, location)));
        toolbar.add(movements);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton inventoryValuation = new IconButton("Valuation", "autoprice", "Valuate this inventory");
        inventoryValuation.addActionListener(_ -> {
            double totalValue = 0;
            for (StockLine sl : Engine.getInventory(location).getStockLines()) {
                Item i = Engine.getItem(sl.getItem());
                if (i != null) {
                    totalValue += sl.getQuantity() * i.getPrice();
                }
            }
            JOptionPane.showMessageDialog(null, "$" + totalValue, "Inventory Valuation", JOptionPane.INFORMATION_MESSAGE);
        });
        toolbar.add(inventoryValuation);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Barcodes", "barcodes", "Print labels for org properties");
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.getPurchaseOrders().size()];
                for (int i = 0; i < Engine.getPurchaseOrders().size(); i++) {
                    printables[i] = Engine.getPurchaseOrders().get(i).getOrderId();
                }
                new CheckboxBarcodeFrame(printables);
            }
        });
        toolbar.add(label);
        toolbar.add(Box.createHorizontalStrut(5));

        JTextField filterValue = Elements.input(location, 10);
        filterValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String searchValue = filterValue.getText().trim();
                    filterTableById(searchValue);
                }
            }
        });

        tb.add(toolbar);
        tb.add(filterValue);

        return tb;
    }

    private void filterTableById(String id) {

        String[] columns = new String[]{
                "Location",
                "HU",
                "ID",
                "Item",
                "Name",
                "Qty.",
                "Price",
                "Value",
                "Area",
                "Bin",
                "Receipt",
                "Status"
        };
        ArrayList<Object[]> stks = new ArrayList<>();
        for (StockLine sl : Engine.getInventory(id).getStockLines()) {
            Item i = Engine.getItem(sl.getItem());
            Bin bin = Engine.getBin(sl.getBin());
            stks.add(new String[]{
                    id,
                    sl.getHu(),
                    sl.getId(),
                    sl.getItem(),
                    i.getName(),
                    String.valueOf(sl.getQuantity()),
                    Constants.formatUSD(i.getPrice()),
                    Constants.formatUSD(i.getPrice() * sl.getQuantity()),
                    bin.getArea(),
                    sl.getBin(),
                    sl.getCreated(),
                    String.valueOf(sl.getStatus())
            });
        }
        title = location + " Inventory";
        CustomTable filteredTable = new CustomTable(columns, stks);
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(filteredTable);
        table = filteredTable;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable jt = (JTable) e.getSource();
                    int viewRow = jt.getSelectedRow();
                    if (viewRow != -1) {
                        int modelRow = jt.convertRowIndexToModel(viewRow);
                        String id = String.valueOf(jt.getModel().getValueAt(modelRow, 3));
                        for (StockLine sl : Engine.getInventory(location).getStockLines()) {
                            if (sl.getId().equals(id)) {
                                desktop.put(new ViewStockLine(sl, desktop, ViewInventory.this));
                            }
                        }
                    }
                }
            }
        });
        scrollPane.revalidate();
        scrollPane.repaint();
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