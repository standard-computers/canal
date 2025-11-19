package org.Canal.UI.Views.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.Order;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ORDS/PR/$[PR_ID]
 */
public class ViewPurchaseRequisition extends LockeState {

    private DesktopState desktop;
    private PurchaseRequisition purchaseRequisition;
    private RefreshListener refreshListener;

    public ViewPurchaseRequisition(PurchaseRequisition requisition, DesktopState desktop, RefreshListener refreshListener) {

        super("Purchase Requisitions", "/ORDS/PR/" + requisition.getId());
        this.purchaseRequisition = requisition;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Header Information", general());
        tabs.addTab("Purchase Orders", purchaseOrders());
        tabs.addTab("Activity", activity());
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(headerPanel(), BorderLayout.NORTH);
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.inputLabel("ID"), new Copiable(purchaseRequisition.getId()));
        form.addInput(Elements.inputLabel("Created"), new Copiable(purchaseRequisition.getCreated()));
        form.addInput(Elements.inputLabel("Creator (Owner)"),  new Copiable(purchaseRequisition.getOwner()));
        form.addInput(Elements.inputLabel("Purchase Req. #"),  new Copiable(purchaseRequisition.getNumber()));
        form.addInput(Elements.inputLabel("Supplier ID"),  new Copiable(purchaseRequisition.getSupplier()));
        form.addInput(Elements.inputLabel("Buyer"),  new Copiable(purchaseRequisition.getBuyer()));
        form.addInput(Elements.inputLabel("Max Spend"),  new Copiable(String.valueOf(purchaseRequisition.getMaxSpend())));
        form.addInput(Elements.inputLabel("Single Order?"),  new Copiable(String.valueOf(purchaseRequisition.isSingleOrder())));
        form.addInput(Elements.inputLabel("Valid From"),  new Copiable(purchaseRequisition.getStart()));
        form.addInput(Elements.inputLabel("To"),  new Copiable(purchaseRequisition.getEnd()));
        form.addInput(Elements.inputLabel("Status"),  new Copiable(String.valueOf(purchaseRequisition.getStatus())));
        form.addInput(Elements.inputLabel("Created"),  new Copiable(purchaseRequisition.getCreated()));
        general.add(form);

        return form;
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
                "Tax Amount",
                "Total",
                "Status",
                "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Order po : Engine.getPurchaseOrders()) {
            if(po.getPurchaseRequisition().equals(purchaseRequisition.getNumber())) {
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

    private JPanel headerPanel(){

        JPanel buttons = new JPanel(new BorderLayout());
        buttons.add(Elements.header("Viewing Pur Req " + purchaseRequisition.getNumber(), SwingConstants.LEFT), BorderLayout.NORTH);

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton block = new IconButton("Block", "block", "Block/Pause PR, can't be used");
        block.addActionListener(_ -> {
            int cnf = JOptionPane.showConfirmDialog(null, "Set Purchase Requisision Status to BLOCKED?", "Confirm status change?", JOptionPane.YES_NO_OPTION);
            if(cnf == JOptionPane.YES_OPTION) {
                purchaseRequisition.setStatus(LockeStatus.BLOCKED);
                purchaseRequisition.save();
                if(refreshListener != null) refreshListener.refresh();
            }
        });
        tb.add(block);
        tb.add(Box.createHorizontalStrut(5));

        if(purchaseRequisition.getStatus() != LockeStatus.ACTIVE){
            IconButton activate = new IconButton("Activate", "start", "Activate PR");
            activate.addActionListener(_ -> {
                int cnf = JOptionPane.showConfirmDialog(null, "Set Purchase Requisision Status to ACTIVE?", "Confirm status change?", JOptionPane.YES_NO_OPTION);
                if(cnf == JOptionPane.YES_OPTION) {
                    purchaseRequisition.setStatus(LockeStatus.ACTIVE);
                    purchaseRequisition.save();

                    if(refreshListener != null) refreshListener.refresh();
                    dispose();
                }
            });
            tb.add(activate);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton archive = new IconButton("Archive", "archive", "Archive PR");
        archive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

                int cnf = JOptionPane.showConfirmDialog(null, "If you archive this purchase requisition, it will no longer be able to be used.", "Confirm archival?", JOptionPane.YES_NO_OPTION);
                if(cnf == JOptionPane.YES_OPTION) {

                    purchaseRequisition.setStatus(LockeStatus.ARCHIVED);
                    purchaseRequisition.save();

                    if(refreshListener != null) refreshListener.refresh();
                }
            }
        });
        tb.add(archive);
        tb.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Barcodes", "barcodes", "Print labels");
        label.addActionListener(_ -> {});
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print Pur. Req.");
        print.addActionListener(_ -> {});
        tb.add(print);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        IconButton delete = new IconButton("Delete", "delete", "Delete Purchase Requisition", "/ORDS/PR/DEL");
        tb.add(delete);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        buttons.add(tb, BorderLayout.SOUTH);
        return buttons;
    }

    private RTextScrollPane notes() {

        RTextScrollPane notes = Elements.simpleEditor();
        notes.getTextArea().setText(purchaseRequisition.getNotes());
        notes.getTextArea().setEditable(false);
        return notes;
    }
}