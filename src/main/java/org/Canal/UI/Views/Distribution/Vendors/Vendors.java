package org.Canal.UI.Views.Distribution.Vendors;

import org.Canal.Models.SupplyChainUnits.Vendor;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Views.Controllers.CheckboxBarcodeFrame;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /VEND
 */
public class Vendors extends JInternalFrame {

    private JTable table;
    private DesktopState desktop;

    public Vendors(DesktopState desktop) {
        super("Vendors", true, true, true, true);
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(Vendors.class.getResource("/icons/vendors.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Vendors", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        add(holder);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton createVendor = new IconButton("New Vendor", "order", "Create a Vendor");
        IconButton blockPo = new IconButton("Block", "block", "Block/Pause Vendor, can't be used");
        IconButton suspendPo = new IconButton("Suspend", "suspend", "Suspend Vendor, can't be used");
        IconButton activatePO = new IconButton("Start", "start", "Resume/Activate Vendor");
        IconButton archivePo = new IconButton("Archive", "archive", "Archive Vendor, removes");
        IconButton label = new IconButton("Barcodes", "label", "Print labels for vendors");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createVendor);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blockPo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(suspendPo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(activatePO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archivePo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterValue);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        createVendor.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateVendor(desktop));
            }
        });
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.orderProcessing.getPurchaseOrder().size()];
                for (int i = 0; i < Engine.orderProcessing.getPurchaseOrder().size(); i++) {
                    printables[i] = Engine.orderProcessing.getPurchaseOrder().get(i).getOrderId();
                }
                new CheckboxBarcodeFrame(printables);
            }
        });
        return tb;
    }

    private JTable createTable() {
        String[] columns = new String[]{"ID", "Org", "Name", "Street", "City", "State", "Postal", "Country", "Status", "Tax Exempt"};
        ArrayList<String[]> pos = new ArrayList<>();
        for (Vendor v : Engine.getVendors()) {
            pos.add(new String[]{
                    v.getId(),
                    v.getOrganization(),
                    v.getName(),
                    v.getLine1(),
                    v.getCity(),
                    v.getState(),
                    v.getPostal(),
                    v.getCountry(),
                    String.valueOf(v.getStatus()),
                    String.valueOf(v.isTaxExempt())
            });
        }
        String[][] data = new String[pos.size()][columns.length];
        for (int i = 0; i < pos.size(); i++) {
            data[i] = pos.get(i);
        }
        JTable table = new JTable(data, columns);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Engine.adjustColumnWidths(table);
        return table;
    }
}