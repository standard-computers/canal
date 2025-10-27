package org.Canal.UI.Views.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.Order;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.System.CheckboxBarcodeFrame;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ORDS/PR
 */
public class PurchaseRequisitions extends LockeState implements RefreshListener {

    private CustomTable table;
    private DesktopState desktop;

    public PurchaseRequisitions(DesktopState desktop) {

        super("Purchase Requisitions", "/ORDS/PR");
        setFrameIcon(new ImageIcon(PurchaseRequisitions.class.getResource("/icons/purchasereqs.png")));
        this.desktop = desktop;

        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("Purchase Requisitions", SwingConstants.LEFT), BorderLayout.NORTH);
        holder.add(toolbar(), BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        if((boolean) Engine.codex.getValue("ORDS/PR", "start_maximized")){
            setMaximized(true);
        }
    }

    /**
     * List of all Purchase Requisitions not _
     * @return CustomTable
     */
    private CustomTable table() {

        String[] columns = new String[]{
                "ID",
                "Created",
                "Name",
                "Owner",
                "Number",
                "Supplier",
                "Supplier Name",
                "Buyer",
                "Max Spend",
                "Consumption",
                "Remaining",
                "Single Ord?",
                "Items",
                "Valid From",
                "Valid To",
                "Status",
        };
        ArrayList<Object[]> prs = new ArrayList<>();
        for (PurchaseRequisition pr : Engine.getPurchaseRequisitions()) {
            if(!pr.getStatus().equals(LockeStatus.ARCHIVED)
                    && !pr.getStatus().equals(LockeStatus.REMOVED)
                    && !pr.getStatus().equals(LockeStatus.DELETED)
                    && !pr.getStatus().equals(LockeStatus.COMPLETED)) {
                double consumption = 0;
                for(Order po : Engine.getPurchaseOrders()){
                    if(po.getPurchaseRequisition().equals(pr.getId())){
                        consumption -= po.getTotal();
                    }
                }
                Location vendor = Engine.getLocation(pr.getSupplier(), "VEND");
                double remaining = pr.getMaxSpend() + consumption;
                prs.add(new Object[]{
                        pr.getId(),
                        pr.getCreated(),
                        pr.getName(),
                        pr.getOwner(),
                        pr.getNumber(),
                        pr.getSupplier(),
                        vendor.getName(),
                        pr.getBuyer(),
                        pr.getMaxSpend(),
                        consumption,
                        remaining,
                        String.valueOf(pr.isSingleOrder()),
                        pr.getItems().size(),
                        pr.getStart(),
                        pr.getEnd(),
                        String.valueOf(pr.getStatus()),
                });
            }
        }
        CustomTable ct = new CustomTable(columns, prs);
        ct.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int row = t.getSelectedRow();
                    if (row != -1) {
                        String v = String.valueOf(t.getValueAt(row, 1));
                        PurchaseRequisition pr = Engine.getPurchaseRequisition(v);
                        desktop.put(new ViewPurchaseRequisition(pr, desktop, PurchaseRequisitions.this));
                    }
                }
            }
        });
        return ct;
    }

    /**
     * Action buttons for PR list
     * @return JPanel of buttons with a title element
     */
    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        if((boolean) Engine.codex.getValue("ORDS/PR", "import_enabled")) {
            IconButton importPR = new IconButton("Import", "import", "Import as CSV");
            tb.add(importPR);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("ORDS/PR", "export_enabled")) {
            IconButton export = new IconButton("Export", "export", "Export as CSV");
            tb.add(export);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton open = new IconButton("Open", "open", "Open selected", "/ORDS/PR/O");
        open.addActionListener(_ -> desktop.put(Engine.router("/ORDS/PR/O", desktop)));
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));

        IconButton createPurchaseReq = new IconButton("New PR", "create", "Create a Purchase Requisition", "/ORDS/PR/NEW");
        createPurchaseReq.addActionListener(_ -> desktop.put(new CreatePurchaseRequisition(desktop)));
        tb.add(createPurchaseReq);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakePRs = new IconButton("AutoMake", "automake", "AutoMake Purchase Requisitions", "/ORDS/PR/AUTO_MK");
        autoMakePRs.addActionListener(_ -> desktop.put(new AutoMakePurchaseRequisitions(desktop)));
        tb.add(autoMakePRs);
        tb.add(Box.createHorizontalStrut(5));

        IconButton convertPurchaseRequisitions = new IconButton("Convert Purchase Reqs", "start", "Convert Purchase Requisitions to Purchase Orders", "/ORDS/PR/PO");
        convertPurchaseRequisitions.addActionListener(_ -> desktop.put(new ConvertPurchaseRequisitions(desktop)));
        tb.add(convertPurchaseRequisitions);
        tb.add(Box.createHorizontalStrut(5));

        IconButton findPR = new IconButton("Find", "find", "Find a Purchase Requisition", "/ORDS/PR/F");
        tb.add(findPR);
        tb.add(Box.createHorizontalStrut(5));

        IconButton labels = new IconButton("Labels", "barcodes", "Print labels for selected");
        labels.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.getPurchaseOrders().size()];
                for (int i = 0; i < Engine.getPurchaseOrders().size(); i++) {
                    printables[i] = Engine.getPurchaseOrders().get(i).getOrderId();
                }
                new CheckboxBarcodeFrame(printables);
            }
        });
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print selectes");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        refresh.addActionListener(_ -> refresh());
        tb.add(refresh);

        return tb;
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