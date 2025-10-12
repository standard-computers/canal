package org.Canal.UI.Views.Items;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Finder;
import org.Canal.Utils.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * /ITS
 */
public class Items extends LockeState implements RefreshListener {

    private ArrayList<Item> items;
    private DesktopState desktop;
    private CustomTable table;

    public Items(ArrayList<Item> items, DesktopState desktop) {

        super("Items", "/ITS");
        setFrameIcon(new ImageIcon(Items.class.getResource("/icons/items.png")));
        this.items = items;
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1300, 600));
        holder.add(Elements.header("Items", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        if((boolean) Engine.codex.getValue("ITS", "start_maximized")){
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue("ITS", "import_enabled")) {
            IconButton importItems = new IconButton("Import", "export", "Import from CSV", "");
            importItems.addActionListener(_ -> {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try (Reader reader = new FileReader(file);
                         CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

                        for (CSVRecord record : csvParser) {

                            Item i = new Item();

                            ArrayList<Item> ls = Engine.getItems();
                            String prefix = (String)  Engine.codex.getValue("ITS", "prefix");
                            int leadingZeros = (Integer) Engine.codex.getValue("ITS", "leading_zeros");
                            int nextId = ls.size() + 1;
                            int width = Math.max(0, leadingZeros);
                            String numberPart = String.format("%0" + width + "d", nextId); // zero-pad to width
                            String itemId = prefix + numberPart;

                            i.setId(itemId);
                            i.setName(record.get("Name"));
                            i.setOrg(record.get("Org"));
                            i.setVendor(record.get("Vendor"));
                            i.setColor(record.get("Color"));
                            i.setUpc(record.get("UPC"));
                            i.setVendorNumber(record.get("Vendor Number"));

                            i.setBaseQuantity(Double.parseDouble(record.get("Base Qty")));
                            i.setBatched(Boolean.parseBoolean(record.get("Batched")));
                            i.setRentable(Boolean.parseBoolean(record.get("Rentable")));
                            i.setSkud(Boolean.parseBoolean(record.get("Skud")));
                            i.setConsumable(Boolean.parseBoolean(record.get("Consumable")));
                            i.setPrice(Double.parseDouble(record.get("Price")));

                            i.setWidth(Double.parseDouble(record.get("Width")));
                            i.setWidthUOM(record.get("wUOM"));
                            i.setLength(Double.parseDouble(record.get("Length")));
                            i.setLengthUOM(record.get("lUOM"));
                            i.setHeight(Double.parseDouble(record.get("Height")));
                            i.setHeightUOM(record.get("hUOM"));
                            i.setWeight(Double.parseDouble(record.get("Weight")));
                            i.setWeightUOM(record.get("wtUOM"));
                            i.setStatus(LockeStatus.valueOf(record.get("Status")));
                            i.setTax(Double.parseDouble(record.get("Tax")));
                            i.setExciseTax(Double.parseDouble(record.get("Excise Tax")));
                            Pipe.save("ITS", i);
                        }
                        refresh();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to import CSV: " + ex.getMessage());
                    }
                }
            });
            tb.add(importItems);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("ITS", "export_enabled")) {
            IconButton export = new IconButton("Export", "export", "Export as CSV", "");
            export.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    table.exportToCSV();
                }
            });
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton open = new IconButton("Open", "open", "Open an Item", "/ITS/O");
        open.addActionListener(_ -> desktop.put(Engine.router("/ITS/O", desktop)));
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("New", "create", "Create an Item", "/ITS/NEW");
        create.addActionListener(_ -> desktop.put(new CreateItem(desktop, this)));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Values", "/ITS/F");
        find.addActionListener(_ -> desktop.put(new Finder("ITS", Item.class, desktop)));
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "label", "Print barcode for an Area");
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selected");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    private CustomTable table() {

        String[] columns = new String[]{
            "ID",
            "Name",
            "Org",
            "Vendor",
            "Vendor Name",
            "Color",
            "UPC",
            "Vendor Number",
            "Price",
            "Base Qty",
            "Pack. Unit",
            "Batched",
            "Rentable",
            "SKU'd",
            "Consumable",
            "Sales",
            "Purchasing",
            "Inventory",
            "Lead",
            "Transport",
            "Manufacture",
            "Components",
            "Width",
            "wUOM",
            "Length",
            "lUOM",
            "Height",
            "hUOM",
            "Volume",
            "vUOM",
            "Sur. Area",
            "saUOM",
            "Weight",
            "wtUOM",
            "Tax",
            "Excise Tax"
        };
        ArrayList<Object[]> d = new ArrayList<>();
        for (Item item : items) {
            item = Engine.getItem(item.getId());
            Location vendor = Engine.getLocation(item.getVendor(), "VEND");
            d.add(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getOrg(),
                    item.getVendor(),
                    vendor.getName(),
                    item.getColor(),
                    item.getUpc(),
                    item.getVendorNumber(),
                    item.getPrice(),
                    item.getBaseQuantity(),
                    item.getPackagingUnit(),
                    item.isBatched(),
                    item.isRentable(),
                    item.isSkud(),
                    item.isConsumable(),
                    item.allowSales(),
                    item.allowPurchasing(),
                    item.keepInventory(),
                    item.getLeadTime(),
                    item.getTransporationTime(),
                    item.getManufacturingTime(),
                    item.getComponents().size(),
                    item.getWidth(),
                    item.getWidthUOM(),
                    item.getLength(),
                    item.getLengthUOM(),
                    item.getHeight(),
                    item.getHeightUOM(),
                    item.getVolume(),
                    item.getVolumeUOM(),
                    item.getSurfaceArea(),
                    item.getSurfaceAreaUOM(),
                    item.getWeight(),
                    item.getWeightUOM(),
                    item.getTax(),
                    item.getExciseTax()
            });
        }
        CustomTable ct = new CustomTable(columns, d);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int row = t.getSelectedRow();
                    if (row != -1) {
                        String v = String.valueOf(t.getValueAt(row, 1));
                        desktop.put(new ViewItem(Engine.getItem(v), desktop, Items.this));
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