package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Views.ViewLocation;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * /ORDS/PR/$[PR_ID]
 */
public class ViewPurchaseRequisition extends LockeState {

    private PurchaseRequisition requisition;

    public ViewPurchaseRequisition(PurchaseRequisition requisition) {

        super("Purchase Requisitions", "/ORDS/PR/$", true, true, true, true);
        setFrameIcon(new ImageIcon(CreatePurchaseRequisition.class.getResource("/icons/purchasereqs.png")));
        this.requisition = requisition;

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Header Information", new ImageIcon(ViewLocation.class.getResource("/icons/info.png")), headerInfo());
        tabs.addTab("Purchase Orders", new ImageIcon(ViewLocation.class.getResource("/icons/purchaseorders.png")), purchaseOrders());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(headerPanel(), BorderLayout.NORTH);
    }

    private JPanel headerInfo() {
        Form f = new Form();
        f.addInput(new Label("ID", UIManager.getColor("Label.foreground")), new Copiable(requisition.getId()));
        f.addInput(new Label("Created", UIManager.getColor("Label.foreground")), new Copiable(requisition.getCreated()));
        f.addInput(new Label("Creator (Owner)", UIManager.getColor("Label.foreground")),  new Copiable(requisition.getOwner()));
        f.addInput(new Label("Purchase Req. #", UIManager.getColor("Label.foreground")),  new Copiable(requisition.getNumber()));
        f.addInput(new Label("Supplier ID", UIManager.getColor("Label.foreground")),  new Copiable(requisition.getSupplier()));
        f.addInput(new Label("Buyer", UIManager.getColor("Label.foreground")),  new Copiable(requisition.getBuyer()));
        f.addInput(new Label("Max Spend", UIManager.getColor("Label.foreground")),  new Copiable(String.valueOf(requisition.getMaxSpend())));
        f.addInput(new Label("[or] Single Order", UIManager.getColor("Label.foreground")),  new Copiable(String.valueOf(requisition.isSingleOrder())));
        f.addInput(new Label("Valid From", UIManager.getColor("Label.foreground")),  new Copiable(requisition.getStart()));
        f.addInput(new Label("To", UIManager.getColor("Label.foreground")),  new Copiable(requisition.getEnd()));
        f.addInput(new Label("Notes", UIManager.getColor("Label.foreground")),  new Copiable(requisition.getNotes()));
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
            if(po.getPurchaseRequisition().equals(requisition.getNumber())) {
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

    private JPanel headerPanel(){
        JPanel buttons = new JPanel(new BorderLayout());
        buttons.add(Elements.header("Viewing Pur Req " + requisition.getNumber(), SwingConstants.LEFT), BorderLayout.NORTH);
        return buttons;
    }
}