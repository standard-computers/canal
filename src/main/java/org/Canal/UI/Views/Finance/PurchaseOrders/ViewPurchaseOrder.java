package org.Canal.UI.Views.Finance.PurchaseOrders;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.ViewLocation;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ORDS/PO/$[PO_ID]
 */
public class ViewPurchaseOrder extends LockeState {

    private PurchaseOrder purchaseOrder;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    public ViewPurchaseOrder(PurchaseOrder purchaseOrder, DesktopState desktop, RefreshListener refreshListener) {

        super("Purchase Order", "/ORDS/PO/" + purchaseOrder.getOrderId(), true, true, true, true);
        setFrameIcon(new ImageIcon(ViewPurchaseOrder.class.getResource("/icons/purchasereqs.png")));
        this.purchaseOrder = purchaseOrder;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", new ImageIcon(ViewLocation.class.getResource("/icons/info.png")), headerInfo());
        tabs.addTab("Activity", activity());
        tabs.addTab("Items", purchaseOrders());
        tabs.addTab("Delivery", delivery());
        tabs.addTab("Ledger", ledger());
        tabs.addTab("Shipping", shipping());
        tabs.addTab("Packaging", packaging());
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(toolbar(), BorderLayout.NORTH);
    }

    private JPanel headerInfo() {

        Form f = new Form();
        f.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(purchaseOrder.getId()));
        f.addInput(Elements.coloredLabel("Created", UIManager.getColor("Label.foreground")), new Copiable(purchaseOrder.getCreated()));
        f.addInput(Elements.coloredLabel("Creator (Owner)", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getOwner()));
        f.addInput(Elements.coloredLabel("Purchase Req. #", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getOrderId()));
        f.addInput(Elements.coloredLabel("Vendor ID", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getVendor()));
        f.addInput(Elements.coloredLabel("Bill To", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getBillTo()));
        f.addInput(Elements.coloredLabel("Ship To", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getBillTo()));
        f.addInput(Elements.coloredLabel("Total", UIManager.getColor("Label.foreground")),  new Copiable(String.valueOf(purchaseOrder.getTotal())));
        f.addInput(Elements.coloredLabel("Created", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getCreated()));
        f.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")),  new Copiable(String.valueOf(purchaseOrder.getStatus())));
        f.addInput(Elements.coloredLabel("Created", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getCreated()));
        return f;
    }

    private JScrollPane purchaseOrders(){

        String[] columns = new String[]{
                "ID",
                "Description",
                "Ordered",
                "Exp Delivery",
                "Bill To",
                "Ship To",
                "Sold To",
                "Trans",
                "Customer",
                "Vendor",
                "Items",
                "Net Value",
                "Tax Rate",
                "Tax Amount",
                "Total",
                "Status",
                "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (PurchaseOrder po : Engine.getPurchaseOrders()) {
            if(po.getPurchaseRequisition().equals(purchaseOrder.getOrderId())) {
                data.add(new Object[]{
                        po.getId(),
                        po.getName(),
                        po.getOrderedOn(),
                        po.getExpectedDelivery(),
                        po.getBillTo(),
                        po.getShipTo(),
                        po.getSoldTo(),
                        po.getTransaction(),
                        po.getCustomer(),
                        po.getVendor(),
                        po.getItems().size(),
                        po.getNetValue(),
                        po.getTaxRate(),
                        po.getTaxAmount(),
                        po.getTotal(),
                        po.getStatus(),
                        po.getCreated()
                });
            }
        }
        CustomTable ibds = new CustomTable(columns, data);
        return new JScrollPane(ibds);
    }

    private JScrollPane activity(){

//        String[] columns = new String[]{
//                "User",
//                "Type",
//                "Created",
//                "Status",
//        };
//        ArrayList<Object[]> data = new ArrayList<>();
//        data.add(new Object[]{});
//        CustomTable ibds = new CustomTable(columns, data);
        return new JScrollPane();
    }

    private JScrollPane delivery(){

        return new JScrollPane();
    }

    private JScrollPane ledger(){

        return new JScrollPane();
    }

    private JScrollPane shipping(){

        return new JScrollPane();
    }

    private JScrollPane packaging(){

        return new JScrollPane();
    }

    private JScrollPane notes(){

        return new JScrollPane();
    }

    private JPanel toolbar(){

        JPanel buttons = new JPanel(new BorderLayout());
        buttons.add(Elements.header("Viewing Purchase Order " + purchaseOrder.getOrderId(), SwingConstants.LEFT), BorderLayout.NORTH);

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton block = new IconButton("Block", "block", "Block/Pause PO, can't be used");
        tb.add(block);
        tb.add(Box.createHorizontalStrut(5));

        IconButton suspend = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
        tb.add(suspend);
        tb.add(Box.createHorizontalStrut(5));

        IconButton activate = new IconButton("Start", "start", "Resume/Activate PO");
        tb.add(activate);
        tb.add(Box.createHorizontalStrut(5));

        IconButton archive = new IconButton("Archive", "archive", "Archive PO");
        tb.add(archive);
        tb.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("", "label", "Print labels");
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("", "print", "Print Pururchase Order");
        tb.add(print);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        block.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

                int cnf = JOptionPane.showConfirmDialog(null, "Set Purchase Requisision Status to BLOCKED?", "Confirm status change?", JOptionPane.YES_NO_OPTION);
                if(cnf == JOptionPane.YES_OPTION) {
                    purchaseOrder.setStatus(LockeStatus.BLOCKED);
                    purchaseOrder.save();
                    if(refreshListener != null){
                        refreshListener.refresh();
                    }
                }
            }
        });
        suspend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

                int cnf = JOptionPane.showConfirmDialog(null, "Set Purchase Requisision Status to SUSPENDED?", "Confirm status change?", JOptionPane.YES_NO_OPTION);
                if(cnf == JOptionPane.YES_OPTION) {
                    purchaseOrder.setStatus(LockeStatus.SUSPENDED);
                    purchaseOrder.save();
                    if(refreshListener != null){
                        refreshListener.refresh();
                    }
                }

            }
        });
        activate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

                int cnf = JOptionPane.showConfirmDialog(null, "Set Purchase Requisision Status to ACTIVE?", "Confirm status change?", JOptionPane.YES_NO_OPTION);
                if(cnf == JOptionPane.YES_OPTION) {
                    purchaseOrder.setStatus(LockeStatus.ACTIVE);
                    purchaseOrder.save();
                    if(refreshListener != null){
                        refreshListener.refresh();
                    }
                }
            }
        });
        archive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

                int cnf = JOptionPane.showConfirmDialog(null, "If you archive this purchase requisition, it will no longer be able to be used.", "Confirm archival?", JOptionPane.YES_NO_OPTION);
                if(cnf == JOptionPane.YES_OPTION) {
                    purchaseOrder.setStatus(LockeStatus.ARCHIVED);
                    purchaseOrder.save();
                    if(refreshListener != null){
                        refreshListener.refresh();
                    }
                }
            }
        });
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

            }
        });
        print.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

            }
        });
        buttons.add(tb, BorderLayout.SOUTH);
        return buttons;
    }
}