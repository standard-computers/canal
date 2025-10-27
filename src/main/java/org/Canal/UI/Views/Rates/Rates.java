package org.Canal.UI.Views.Rates;

import org.Canal.Models.BusinessUnits.Rate;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Items.CreateItem;
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
 * /RTS
 */
public class Rates extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Rates(DesktopState desktop) {

        super("Rates", "/RTS");
        this.desktop = desktop;

        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1300, 600));
        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue("ITS", "import_enabled")) {
            IconButton importItems = new IconButton("Import", "import", "Import from CSV", "");
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

        IconButton create = new IconButton("New", "create", "Create an Item", "/ITS/NEW");
        create.addActionListener(_ -> {
            desktop.put(new CreateItem(desktop, this));
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton open = new IconButton("Open", "open", "Open an Item", "/ITS/O");
        open.addActionListener(_ -> desktop.put(Engine.router("/ITS/O", desktop)));
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Values", "/ITS/F");
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        toolbar.add(Elements.header("Rates", SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private CustomTable table() {

        String[] columns = new String[]{
                "ID",
                "Name",
                "Description",
                "Value",
                "Percent",
                "Tax",
                "Objex",
                "Reference",
                "Status",
                "Created"
        };
        ArrayList<Object[]> d = new ArrayList<>();
        for (Rate rate : Engine.getRates()) {
            d.add(new Object[]{
                    rate.getId(),
                    rate.getName(),
                    rate.getDescription(),
                    rate.getValue(),
                    rate.isPercent(),
                    rate.isTax(),
                    rate.getObjex(),
                    rate.getReference(),
                    rate.getStatus(),
                    rate.getCreated(),
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
                        desktop.put(new ViewRate(Engine.getRate(v), desktop, Rates.this));
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