package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.System.CheckboxBarcodeFrame;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ORDS/PR
 */
public class PurchaseRequisitions extends LockeState {

    private CustomTable table;
    private DesktopState desktop;

    public PurchaseRequisitions(DesktopState desktop) {

        super("Purchase Requisitions", "/ORDS/PR", true, true, true, true);
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
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int row = t.getSelectedRow();
                    if (row != -1) {
                        String v = String.valueOf(t.getValueAt(row, 1));
                        PurchaseRequisition pr = Engine.orders.getPurchaseRequisition(v);
                        desktop.put(new ViewPurchaseRequisition(pr, desktop));
                    }
                }
            }
        });
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
                "Buyer",
                "Max Spend",
                "Consumption",
                "Remaining",
                "Single Ord?",
                "Valid From",
                "Valid To",
                "Status"
        };
        ArrayList<Object[]> prs = new ArrayList<>();
        for (PurchaseRequisition pr : Engine.orders.getPurchaseRequisitions()) {
            if(!pr.getStatus().equals("ARCHIVED") && !pr.getStatus().equals("REMOVED")) {
                double consumption = 0;
                for(PurchaseOrder po : Engine.getPurchaseOrders()){
                    if(po.getPurchaseRequisition().equals(pr.getId())){
                        consumption += po.getTotal();
                    }
                }
                double remaining = pr.getMaxSpend() - consumption;
                prs.add(new Object[]{
                        pr.getId(),
                        pr.getCreated(),
                        pr.getName(),
                        pr.getOwner(),
                        pr.getNumber(),
                        pr.getSupplier(),
                        pr.getBuyer(),
                        pr.getMaxSpend(),
                        consumption,
                        remaining,
                        String.valueOf(pr.isSingleOrder()),
                        pr.getStart(),
                        pr.getEnd(),
                        String.valueOf(pr.getStatus())
                });
            }
        }
        return new CustomTable(columns, prs);
    }

    /**
     * Action buttons for PR list
     * @return JPanel of buttons with a title element
     */
    private JScrollPane toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton open = new IconButton("Open", "open", "Open selected");
        IconButton createPurchaseReq = new IconButton("New PR", "create", "Build an item");
        IconButton blockPR = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        IconButton suspendPR = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
        IconButton activatePR = new IconButton("Start", "start", "Resume/Activate PO");
        IconButton archivePR = new IconButton("Archive", "archive", "Archive PO, removes");
        IconButton autoMakePRs = new IconButton("AutoMake", "automake", "AutoMake Purchase Requisitions");
        IconButton labels = new IconButton("Barcodes", "label", "Print labels for selected");
        IconButton print = new IconButton("Print", "print", "Print selectes");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(open);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createPurchaseReq);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blockPR);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(suspendPR);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(activatePR);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archivePR);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMakePRs);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(labels);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        export.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        open.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        createPurchaseReq.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseRequisition());
            }
        });
        autoMakePRs.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent e) {
               desktop.put(new AutoMakePurchaseRequisitions());
           }
        });
        labels.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.orders.getPurchaseOrder().size()];
                for (int i = 0; i < Engine.orders.getPurchaseOrder().size(); i++) {
                    printables[i] = Engine.orders.getPurchaseOrder().get(i).getOrderId();
                }
                new CheckboxBarcodeFrame(printables);
            }
        });
        return Elements.scrollPane(tb);
    }
}