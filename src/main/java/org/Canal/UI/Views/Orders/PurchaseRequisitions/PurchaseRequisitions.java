package org.Canal.UI.Views.Orders.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.UI.Elements.CustomJTable;
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
 * /ORDS/PR
 */
public class PurchaseRequisitions extends JInternalFrame {

    private CustomJTable table;
    private DesktopState desktop;

    public PurchaseRequisitions(DesktopState desktop) {
        super("Purchase Requisitions", true, true, true, true);
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(PurchaseRequisitions.class.getResource("/icons/purchasereqs.png")));
        JPanel tb = createToolBar();
        JPanel holder = new JPanel(new BorderLayout());
        table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        holder.add(Elements.header("All Purchase Requisitions", SwingConstants.LEFT), BorderLayout.NORTH);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private CustomJTable createTable() {
        String[] columns = new String[]{"ID", "Created", "Name", "Owner", "Number", "Supplier", "Buyer", "Max Spend", "Consumption", "Remaining", "Single Ord?", "Valid From", "Valid To", "Status"};
        ArrayList<String[]> prs = new ArrayList<>();
        for (PurchaseRequisition pr : Engine.realtime.getPurchaseRequisitions()) {
            double consumption = 0;
            for(PurchaseOrder po : Engine.getOrders()){
                if(po.getPurchaseRequisition().equals(pr.getId())){
                    consumption += po.getTotal();
                }
            }
            double remaining = pr.getMaxSpend() - consumption;
            prs.add(new String[]{
                    pr.getId(),
                    pr.getCreated(),
                    pr.getName(),
                    pr.getOwner(),
                    pr.getNumber(),
                    pr.getSupplier(),
                    pr.getBuyer(),
                    String.valueOf(pr.getMaxSpend()),
                    String.valueOf(consumption),
                    String.valueOf(remaining),
                    String.valueOf(pr.isSingleOrder()),
                    pr.getStart(),
                    pr.getEnd(),
                    String.valueOf(pr.getStatus())
            });
        }
        String[][] data = new String[prs.size()][columns.length];
        for (int i = 0; i < prs.size(); i++) {
            data[i] = prs.get(i);
        }
        CustomJTable table = new CustomJTable(data, columns); // Use CustomJTable
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Engine.adjustColumnWidths(table);
        return table;
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton createPurchaseReq = new IconButton("New PR", "create", "Build an item");
        IconButton blockPo = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        IconButton suspendPo = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
        IconButton activatePO = new IconButton("Start", "start", "Resume/Activate PO");
        IconButton archivePo = new IconButton("Archive", "archive", "Archive PO, removes");
        IconButton autoMake = new IconButton("AutoMake", "automake", "AutoMake Purchase Requisitions");
        IconButton label = new IconButton("Barcodes", "label", "Print labels for org properties");
        JTextField filterValue = Elements.input("Search", 10);
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createPurchaseReq);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(blockPo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(suspendPo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(activatePO);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archivePo);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMake);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(filterValue);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        createPurchaseReq.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseRequisition());
            }
        });
        autoMake.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent e) {
               desktop.put(new AutoMakePurchaseRequisitions());
           }
        });
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String[] printables = new String[Engine.realtime.getPurchaseOrders().size()];
                for (int i = 0; i < Engine.realtime.getPurchaseOrders().size(); i++) {
                    printables[i] = Engine.realtime.getPurchaseOrders().get(i).getOrderId();
                }
                new CheckboxBarcodeFrame(printables);
            }
        });
        return tb;
    }
}