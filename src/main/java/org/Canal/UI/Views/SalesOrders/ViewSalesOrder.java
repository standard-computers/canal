package org.Canal.UI.Views.SalesOrders;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * /ORDS/SO/$[SALES_ORDER_ID]
 * View a Sales Order Locke
 */
public class ViewSalesOrder extends LockeState {

    private Order salesOrder;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    private ItemTableModel model;
    private double taxRate = 0.05;
    private JLabel netValue;
    private JLabel taxAmount;
    private JLabel totalAmount;
    private Copiable orderId;
    private DatePicker expectedDelivery;
    private JCheckBox commitToLedger, createOutboundDelivery, createPurchaseOrder, createInboundDelivery;
    private JTextField outboundTruckId, inboundTruckId;

    public ViewSalesOrder(Order salesOrder, DesktopState desktop, RefreshListener refreshListener) {

        super("Create Sales Order", "/ORDS/SO/NEW", false, true, false, true);
        this.salesOrder = salesOrder;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Item Details", itemDetails());
        tabs.addTab("Delivery", deliveryDetails());
        tabs.addTab("Ledger", ledgerDetails());
        tabs.addTab("Purchase Order", purchaseOrderDetails());

        JPanel coreValues = orderInfoPanel();
        JPanel moreInfo = moreOrderInfoPanel();
        coreValues.setBorder(new EmptyBorder(5, 5, 5, 5));
        moreInfo.setBorder(new EmptyBorder(5, 5, 5, 5));

        setLayout(new BorderLayout());
        JPanel orderInfo = new JPanel(new BorderLayout());
        orderInfo.add(coreValues, BorderLayout.WEST);
        orderInfo.add(moreInfo, BorderLayout.EAST);
        add(orderInfo, BorderLayout.NORTH);

        add(tabs, BorderLayout.CENTER);
        JPanel orderSummary = new JPanel(new BorderLayout());
        JPanel genSummary = orderSummary();
        orderSummary.add(genSummary, BorderLayout.CENTER);
        add(orderSummary, BorderLayout.SOUTH);
        model.addTableModelListener(_ -> updateTotal());
    }

    private JPanel orderInfoPanel() {


        Form form = new Form();
        form.addInput(Elements.inputLabel("Order ID", "Sales Order ID"), new Copiable(salesOrder.getId()));
        form.addInput(Elements.inputLabel("Supplier", "Location ID of the supplier of these goods"), new Copiable(salesOrder.getVendor()));
        form.addInput(Elements.inputLabel("Bill To", "Location ID that pays for these goods"), new Copiable(salesOrder.getBillTo()));
        form.addInput(Elements.inputLabel("Ship To", "Location ID of where the goods are to be delivered to"), new Copiable(salesOrder.getShipTo()));

        return form;
    }

    private JPanel moreOrderInfoPanel() {

        Form f = new Form();
        JTextField ordered = new Copiable(LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        expectedDelivery = new DatePicker();
        f.addInput(Elements.inputLabel("*Ordered"), ordered);
        f.addInput(Elements.inputLabel("Expected Delivery"), expectedDelivery);
        f.addInput(Elements.inputLabel("Status"), new Copiable("DRAFT"));
        return f;
    }

    private JPanel orderSummary() {

        Form f = new Form();
        DecimalFormat df = new DecimalFormat("#0.00");
        netValue = new JLabel("$" + model.getTotalPrice());
        netValue.setFont(UIManager.getFont("h3.font"));
        f.addInput(new JLabel("Net Value"), netValue);
        taxAmount = new JLabel("$" + df.format(taxRate * Double.parseDouble(model.getTotalPrice())));
        taxAmount.setFont(UIManager.getFont("h3.font"));
        f.addInput(new JLabel("Tax Amount"), taxAmount);
        totalAmount = Elements.h2("$" + df.format(taxRate * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice())));
        totalAmount.setForeground(new Color(33, 124, 13));
        f.addInput(Elements.h2("Total"), totalAmount);
        return f;
    }

    private void updateTotal() {

        DecimalFormat df = new DecimalFormat("#0.00");
        netValue.setText("$" + model.getTotalPrice());
        taxAmount.setText("$" + df.format(taxRate * Double.parseDouble(model.getTotalPrice())));
        totalAmount.setText("$" + df.format(taxRate * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice())));
    }

    private JPanel itemDetails() {

        JPanel p = new JPanel(new BorderLayout());
        ArrayList<Item> items = Engine.getItems();
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products found", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
        model = new ItemTableModel(Collections.singletonList(items.getFirst()));
        JTable table = new JTable(model);
        TableColumn col1 = table.getColumnModel().getColumn(1);
        TableColumn col2 = table.getColumnModel().getColumn(2);
        TableColumn col3 = table.getColumnModel().getColumn(3);
        TableColumn col4 = table.getColumnModel().getColumn(4);

        TableCellRenderer centerRenderer = new CenteredRenderer();
        col1.setCellRenderer(centerRenderer);
        col2.setCellRenderer(centerRenderer);
        col3.setCellRenderer(centerRenderer);
        col4.setCellRenderer(centerRenderer);
        JComboBox<Item> itemComboBox = new JComboBox<>(items.toArray(new Item[0]));
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(itemComboBox));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UIManager.getColor("Panel.background"));
        IconButton addButton = new IconButton("", "add_rows", "Add products");
        addButton.addActionListener((ActionEvent _) -> {
            if (!items.isEmpty()) {
                model.addRow(items.getFirst());
            }
        });
        IconButton removeButton = new IconButton("", "delete_rows", "Remove selected product");
        removeButton.addActionListener((ActionEvent _) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
            }
        });
        buttonPanel.add(removeButton);
        buttonPanel.add(addButton);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.NORTH);
        return p;
    }

    private JPanel deliveryDetails() {

        createOutboundDelivery = new JCheckBox();
        if ((boolean) Engine.codex("ORDS/SO", "use_deliveries")) {
            createOutboundDelivery.setSelected(true);
            createOutboundDelivery.setEnabled(false);
        }
        outboundTruckId = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Create Outbound Delivery (ODO) for Supplier"), createOutboundDelivery);
//        form.addInput(Elements.inputLabel("Carrier"), new Copiable(salesOrder.get));
        form.addInput(Elements.inputLabel("Truck ID/Number"), outboundTruckId);

        return form;
    }

    private JPanel ledgerDetails() {

        commitToLedger = new JCheckBox();
        if ((boolean) Engine.codex("ORDS/SO", "commit_to_ledger")) {
            commitToLedger.setSelected(true);
            commitToLedger.setEnabled(false);
        }

        Form form = new Form();
        form.addInput(Elements.inputLabel("Commit to Ledger"), commitToLedger);
//        form.addInput(Elements.inputLabel("Trans. Type (Receiving location type)"), buyerObjexType);
//        form.addInput(Elements.inputLabel("Purchasing Org."), organizations);
//        form.addInput(Elements.inputLabel("Ledger"), ledgers);
//
        return form;
    }

    private JPanel purchaseOrderDetails() {

        createPurchaseOrder = new JCheckBox();
        if ((boolean) Engine.codex("ORDS/SO", "auto_create_po")) {
            createPurchaseOrder.setSelected(true);
            createPurchaseOrder.setEnabled(false);
        }
        HashMap<String, String> prs = new HashMap<>();
        for (PurchaseRequisition pr1 : Engine.getPurchaseRequisitions()) {
            prs.put(pr1.getId(), pr1.getId());
        }
        createInboundDelivery = new JCheckBox();
        if ((boolean) Engine.codex("ORDS/SO", "create_buyer_inbound")) {
            createInboundDelivery.setSelected(true);
            createInboundDelivery.setEnabled(false);
        }
        inboundTruckId = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Create Purchase Order?"), createPurchaseOrder);
//        form.addInput(Elements.inputLabel("Purchase Requisition"), availablePurchaseRequisitions);
        form.addInput(Elements.inputLabel("Create IDO"), createInboundDelivery);
//        form.addInput(Elements.inputLabel("Transporation Carrier"), inboundCarriers);
        form.addInput(Elements.inputLabel("Truck ID/Number"), inboundTruckId);
        return form;

    }
}