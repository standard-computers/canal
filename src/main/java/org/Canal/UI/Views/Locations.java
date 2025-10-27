package org.Canal.UI.Views;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Ledgers.Ledgers;
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
 * /$
 */
public class Locations extends LockeState implements RefreshListener {

    private String objexType;
    private DesktopState desktop;
    private CustomTable table;

    public Locations(String objexType, DesktopState desktop) {

        super("Locations", objexType);
        setTitle((String) Engine.codex.getValue(objexType, "icon"));
        setFrameIcon(new ImageIcon(Ledgers.class.getResource("/icons/windows/" + Engine.codex.getValue(objexType, "icon") + ".png")));
        this.objexType = objexType;
        this.desktop = desktop;
        String oo = objexType;
        if (oo.startsWith("/")) {
            oo = oo.substring(1);
        }
        JPanel tb = toolbar();
        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header(((String) Engine.codex.getValue(oo, "name")), SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        if ((boolean) Engine.codex.getValue(objexType, "start_maximized")) {
            setMaximized(true);
        }
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if ((boolean) Engine.codex.getValue(objexType, "import_enabled")) {
            IconButton importLocations = new IconButton("Import", "import", "Import as CSV");
            importLocations.addActionListener(_ -> {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try (Reader reader = new FileReader(file);
                         CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

                        for (CSVRecord record : csvParser) {

                            Location l = new Location();

                            String locationId = Engine.generateId(objexType);

                            l.setType(objexType);
                            l.setId(locationId);
                            l.setOrganization(record.get("Org"));
                            l.setName(record.get("Name"));
                            l.setLine1(record.get("Line 1"));
                            l.setLine2(record.get("Line 2"));
                            l.setCity(record.get("City"));
                            l.setState(record.get("State"));
                            l.setPostal(record.get("Postal"));
                            l.setCountry(record.get("Country"));
                            l.setEin(record.get("Tax ID"));
                            l.setTaxExempt(Boolean.parseBoolean(record.get("Tax Exempt")));
                            l.setEmail(record.get("Email"));
                            l.setPhone(record.get("Phone"));
                            l.setWidth(Double.parseDouble(record.get("Width")));
                            l.setWidthUOM(record.get("wUOM"));
                            l.setLength(Double.parseDouble(record.get("Length")));
                            l.setLengthUOM(record.get("lUOM"));
                            l.setHeight(Double.parseDouble(record.get("Height")));
                            l.setHeightUOM(record.get("hUOM"));
                            l.setStatus(LockeStatus.valueOf(record.get("Status")));
                            Pipe.save(objexType, l);
                        }
                        refresh();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to import CSV: " + ex.getMessage());
                    }
                }
            });
            tb.add(importLocations);
            tb.add(Box.createHorizontalStrut(5));
        }

        if ((boolean) Engine.codex.getValue(objexType, "export_enabled")) {
            IconButton export = new IconButton("Export", "export", "Export as CSV");
            export.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    table.exportToCSV();
                }
            });
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton open = new IconButton("Open", "open", "Open Location");
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("New", "create", "Create a Location", objexType + "/NEW");
        create.addActionListener(_ -> desktop.put(new CreateLocation(objexType, desktop, this)));
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Location(s)", objexType + "/DEL");
        delete.addActionListener(_ -> desktop.put(new CreateLocation(objexType, desktop, this)));
        tb.add(delete);
        tb.add(Box.createHorizontalStrut(5));

        IconButton find = new IconButton("Find", "find", "Find by Values", objexType + "/F");
        tb.add(find);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "barcodes", "Print labels for selected");
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selected");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));


        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));

        return tb;
    }

    private CustomTable table() {

        String[] columns = new String[]{
                "ID",
                "Org",
                "Name",
                "Line 1",
                "Line 2",
                "City",
                "State",
                "Postal",
                "Country",
                "Tax ID",
                "Tax Exempt",
                "Phone",
                "Email",
                "Departments",
                "Width",
                "wUOM",
                "Length",
                "lUOM",
                "Height",
                "hUOM",
                "Area",
                "aUOM",
                "Volume",
                "vUOM",
                "Inventory",
                "Production",
                "Sales",
                "Purchasing",
                "Status",
                "Created",
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Location location : Engine.getLocations(objexType)) {
            data.add(new Object[]{
                    location.getId(),
                    location.getOrganization(),
                    location.getName(),
                    location.getLine1(),
                    location.getLine2(),
                    location.getCity(),
                    location.getState(),
                    location.getPostal(),
                    location.getCountry(),
                    location.getEin(),
                    location.isTaxExempt(),
                    location.getPhone(),
                    location.getEmail(),
                    location.getDepartments().size(),
                    location.getWidth(),
                    location.getWidthUOM(),
                    location.getLength(),
                    location.getLengthUOM(),
                    location.getHeight(),
                    location.getHeightUOM(),
                    location.getArea(),
                    location.getAreaUOM(),
                    location.getVolume(),
                    location.getVolumeUOM(),
                    location.allowsInventory(),
                    location.allowsProduction(),
                    location.allowsSales(),
                    location.allowsPurchasing(),
                    location.getStatus(),
                    location.getCreated(),
            });
        }
        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(Engine.router(objexType + "/" + value, desktop));
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