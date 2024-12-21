package org.Canal.UI.Views.Finance.SalesOrders;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.DatePicker;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * /ORDS/SO/NEW
 */
public class CreateSalesOrder extends LockeState {

    private ItemTableModel model;
    private double taxRate = 0.05;
    private JLabel netValue;
    private JLabel taxAmount;
    private JLabel totalAmount;
    private Selectable availablePurchaseRequisitions, selectSupplier, selectBillTo, selectShipTo, organizations, outboundCarriers, inboundCarriers, ledgers;
    private Copiable orderId;
    private DatePicker expectedDelivery;
    private JCheckBox commitToLedger, createOutboundDelivery, createPurchaseOrder, createInboundDelivery;
    private JTextField buyerObjexType, outboundTruckId, inboundTruckId;

    public CreateSalesOrder() {
        super("Create Sales Order", "/ORDS/SO/NEW", false, true, false, true);
        Constants.checkLocke(this, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Item Details", itemDetails());
        tabs.addTab("Delivery", deliveryDetails());
        tabs.addTab("Ledger", ledgerDetails());
        tabs.addTab("Purchase Order", purchaseOrderDetails());

        JPanel coreValues = orderInfoPanel();
        JPanel moreInfo = moreOrderInfoPanel();
        selectBillTo.setSelectedValue(Engine.getOrganization().getId());
        selectShipTo.setSelectedValue(Engine.getOrganization().getId());
        coreValues.setBorder(new EmptyBorder(10, 10, 10, 10));
        moreInfo.setBorder(new EmptyBorder(10, 10, 10, 10));

        setLayout(new BorderLayout());
        JPanel orderInfo = new JPanel(new BorderLayout());
        orderInfo.add(coreValues, BorderLayout.WEST);
        orderInfo.add(moreInfo, BorderLayout.EAST);
        add(orderInfo, BorderLayout.NORTH);

        add(tabs, BorderLayout.CENTER);
        JButton save = Elements.button("Submit Order");
        JPanel orderSummary = new JPanel(new BorderLayout());
        JPanel genSummary = orderSummary();
        orderSummary.add(genSummary, BorderLayout.CENTER);
        orderSummary.add(save, BorderLayout.SOUTH);
        add(orderSummary, BorderLayout.SOUTH);
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(expectedDelivery.getSelectedDate() == null){
                    JOptionPane.showMessageDialog(null, "Must select a delivery date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                PurchaseRequisition assignedPR = Engine.orderProcessing.getPurchaseRequisitions(availablePurchaseRequisitions.getSelectedValue());
                int ccc = JOptionPane.showConfirmDialog(null, "Confirm order?", "You have selected the Org ID as the charge account. Are you sure you want to charge the corp account?",JOptionPane.YES_NO_CANCEL_OPTION);
                if(ccc == JOptionPane.YES_OPTION){
                    SalesOrder newSalesOrder = new SalesOrder();
                    newSalesOrder.setOwner((Engine.getAssignedUser().getId()));
                    newSalesOrder.setOrderId(orderId.value());
                    newSalesOrder.setVendor(selectSupplier.getSelectedValue());
                    newSalesOrder.setBillTo(selectBillTo.getSelectedValue());
                    newSalesOrder.setShipTo(selectShipTo.getSelectedValue());
                    newSalesOrder.setExpectedDelivery(expectedDelivery.getSelectedDateString());
                    newSalesOrder.setStatus(LockeStatus.NEW);
                    ArrayList<OrderLineItem> lineitems = new ArrayList<>();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        String itemName = model.getValueAt(row, 0).toString();
                        String itemId = model.getValueAt(row, 1).toString();
                        double itemQty = Double.parseDouble(model.getValueAt(row, 2).toString());
                        double itemPrice = Double.parseDouble(model.getValueAt(row, 3).toString());
                        double itemTotal = Double.parseDouble(model.getValueAt(row, 4).toString());
                        lineitems.add(new OrderLineItem(itemName, itemId, itemQty, itemPrice, itemTotal));
                    }
                    newSalesOrder.setItems(lineitems);
                    newSalesOrder.setNetValue(Double.parseDouble(model.getTotalPrice()));
                    newSalesOrder.setTaxAmount(taxRate * Double.parseDouble(model.getTotalPrice()));
                    newSalesOrder.setTotal(taxRate * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice()));
                    newSalesOrder.setOrderedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")));
                    assignedPR.setStatus(LockeStatus.IN_USE);
                    assignedPR.save();
                    Pipe.save("/ORDS/SO", newSalesOrder);

                    if(createOutboundDelivery.isSelected()){
                        Truck t = new Truck();
                        t.setId(((String) Engine.codex("TRANS/TRCKS", "prefix") + 1000 + (Engine.getTrucks().size() + 1)));
                        t.setCarrier(outboundCarriers.getSelectedValue());
                        Pipe.save("/TRANS/TRCKS", t);
                        Delivery d = new Delivery();
                        d.setId(((String) Engine.codex("TRANS/ODO", "prefix") + 1000 + (Engine.getInboundDeliveries().size() + 1)));
                        d.setName("Sales Order from " + selectBillTo.getSelectedValue());
                        d.setSalesOrder(newSalesOrder.getOrderId());
                        d.setExpectedDelivery(newSalesOrder.getExpectedDelivery());
                        d.setDestination(selectSupplier.getSelectedValue());
                        d.setStatus(LockeStatus.PROCESSING);
                        Pipe.save("/TRANS/ODO", d);
                    }

                    if(createInboundDelivery.isSelected()){
                        Truck t = new Truck();
                        t.setId(((String) Engine.codex("TRANS/TRCKS", "prefix") + 1000 + (Engine.getTrucks().size() + 1)));
                        t.setCarrier(inboundCarriers.getSelectedValue());
                        Pipe.save("/TRANS/TRCKS", t);
                        Delivery d = new Delivery();
                        d.setId(((String) Engine.codex("TRANS/IDO", "prefix") + 1000 + (Engine.getInboundDeliveries().size() + 1)));
                        d.setSalesOrder(newSalesOrder.getOrderId());
                        d.setExpectedDelivery(newSalesOrder.getExpectedDelivery());
                        d.setDestination(selectShipTo.getSelectedValue());
                        d.setStatus(LockeStatus.PROCESSING);
                        Pipe.save("/TRANS/IDO", d);
                    }

                    if(createPurchaseOrder.isSelected()){
                        PurchaseOrder associatedPurchaseOrder = new PurchaseOrder();
                        associatedPurchaseOrder.setOwner((Engine.getAssignedUser().getId()));
                        associatedPurchaseOrder.setOrderId(orderId.value());
                        associatedPurchaseOrder.setVendor(selectSupplier.getSelectedValue());
                        associatedPurchaseOrder.setBillTo(selectBillTo.getSelectedValue());
                        associatedPurchaseOrder.setShipTo(selectShipTo.getSelectedValue());
                        associatedPurchaseOrder.setPurchaseRequisition(availablePurchaseRequisitions.getSelectedValue());
                        associatedPurchaseOrder.setExpectedDelivery(expectedDelivery.getSelectedDateString());
                        associatedPurchaseOrder.setStatus(LockeStatus.NEW);
                        ArrayList<OrderLineItem> li = new ArrayList<>();
                        for (int row = 0; row < model.getRowCount(); row++) {
                            String itemName = model.getValueAt(row, 0).toString();
                            String itemId = model.getValueAt(row, 1).toString();
                            double itemQty = Double.parseDouble(model.getValueAt(row, 2).toString());
                            double itemPrice = Double.parseDouble(model.getValueAt(row, 3).toString());
                            double itemTotal = Double.parseDouble(model.getValueAt(row, 4).toString());
                            li.add(new OrderLineItem(itemName, itemId, itemQty, itemPrice, itemTotal));
                        }
                        associatedPurchaseOrder.setItems(lineitems);
                        associatedPurchaseOrder.setNetValue(Double.parseDouble(model.getTotalPrice()));
                        associatedPurchaseOrder.setTaxAmount(taxRate * Double.parseDouble(model.getTotalPrice()));
                        associatedPurchaseOrder.setTotal(taxRate * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice()));
                        associatedPurchaseOrder.setOrderedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")));
                        assignedPR.setStatus(LockeStatus.IN_USE);
                        assignedPR.save();
                        Pipe.save("/ORDS/PO", associatedPurchaseOrder);
                        if(commitToLedger.isSelected()){
                            Ledger l = Engine.getLedger(ledgers.getSelectedValue());
                            if(l != null){
                                Transaction t = new Transaction();
                                t.setId(Constants.generateId(5));
                                t.setOwner(Engine.getAssignedUser().getId());
                                t.setLocke(getLocke());
                                t.setObjex(buyerObjexType.getText().trim());
                                t.setLocation(selectBillTo.getSelectedValue());
                                t.setReference(orderId.value());
                                t.setAmount(-1 * Double.parseDouble(model.getTotalPrice()));
                                t.setCommitted(Constants.now());
                                t.setStatus(LockeStatus.PROCESSING);
                                l.addTransaction(t);
                                l.save();
                            }else{
                                JOptionPane.showMessageDialog(null, "No such ledger.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                    if(commitToLedger.isSelected()){
                        Ledger l = Engine.getLedger(ledgers.getSelectedValue());
                        if(l != null){
                            Transaction t = new Transaction();
                            t.setId(Constants.generateId(5));
                            t.setOwner(Engine.getAssignedUser().getId());
                            t.setLocke(getLocke());
                            t.setObjex(buyerObjexType.getText().trim());
                            t.setLocation(selectSupplier.getSelectedValue());
                            t.setReference(orderId.value());
                            t.setAmount(Double.parseDouble(model.getTotalPrice()));
                            t.setCommitted(Constants.now());
                            t.setStatus(LockeStatus.PROCESSING);
                            l.addTransaction(t);
                            l.save();
                        }else{
                            JOptionPane.showMessageDialog(null, "No such ledger.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    dispose();
                    JOptionPane.showMessageDialog(null, "Order submitted.");
                }
            }
        });
        model.addTableModelListener(_ -> updateTotal());
    }

    private JPanel orderInfoPanel(){
        Form f = new Form();
        selectBillTo = Selectables.allLocations();
        selectBillTo.editable();
        selectShipTo = Selectables.allLocations();
        selectShipTo.editable();
        selectSupplier = Selectables.allLocations();
        selectSupplier.editable();
        orderId = new Copiable("SO" + (60000000 + (Engine.orderProcessing.getSalesOrders().size() + 1)));
        f.addInput(new Label("*Order ID", Constants.colors[0]), orderId);
        f.addInput(new Label("Supplier", Constants.colors[1]), selectSupplier);
        f.addInput(new Label("Bill To", Constants.colors[2]), selectBillTo);
        f.addInput(new Label("Ship To", Constants.colors[3]), selectShipTo);
        return f;
    }

    private JPanel moreOrderInfoPanel(){
        Form f = new Form();
        JTextField ordered = new Copiable(LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        expectedDelivery = new DatePicker();
        f.addInput(new Label("*Ordered", UIManager.getColor("Label.foreground")), ordered);
        f.addInput(new Label("Expected Delivery", UIManager.getColor("Label.foreground")), expectedDelivery);
        f.addInput(new Label("Status", UIManager.getColor("Label.foreground")), new Copiable("DRAFT"));
        return f;
    }

    private JPanel orderSummary(){
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

    private void updateTotal(){
        DecimalFormat df = new DecimalFormat("#0.00");
        netValue.setText("$" + model.getTotalPrice());
        taxAmount.setText("$" + df.format(taxRate * Double.parseDouble(model.getTotalPrice())));
        totalAmount.setText("$" + df.format(taxRate * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice())));
    }

    private JPanel itemDetails(){
        JPanel p = new JPanel(new BorderLayout());
        ArrayList<Item> items = Engine.getItems();
        if(items.isEmpty()){
            JOptionPane.showMessageDialog(this, "No items found", "Error", JOptionPane.ERROR_MESSAGE);
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

    private JPanel deliveryDetails(){
        Form p = new Form();
        createOutboundDelivery = new JCheckBox();
        if((boolean) Engine.codex("ORDS/SO", "use_deliveries")){
            createOutboundDelivery.setSelected(true);
            createOutboundDelivery.setEnabled(false);
        }
        outboundCarriers = Selectables.carriers();
        outboundTruckId = Elements.input();
        p.addInput(new Label("Create Outbound Delivery (ODO) for Supplier", Constants.colors[9]), createOutboundDelivery);
        p.addInput(new Label("Carrier", Constants.colors[8]), outboundCarriers);
        p.addInput(new Label("Truck ID/Number", Constants.colors[7]), outboundTruckId);
        return p;
    }

    private JPanel ledgerDetails(){
        Form f = new Form();
        commitToLedger = new JCheckBox();
        if((boolean) Engine.codex("ORDS/SO", "commit_to_ledger")){
            commitToLedger.setSelected(true);
            commitToLedger.setEnabled(false);
        }
        organizations = Selectables.organizations();
        buyerObjexType = Elements.input();
        ledgers = Selectables.ledgers();
        f.addInput(new Label("Commit to Ledger", Constants.colors[9]), commitToLedger);
        f.addInput(new Label("Trans. Type", Constants.colors[8]), buyerObjexType);
        f.addInput(new Label("Purchasing Org.", Constants.colors[7]), organizations);
        f.addInput(new Label("Ledger", Constants.colors[6]), ledgers);
        return f;
    }

    private JPanel purchaseOrderDetails(){
        Form f = new Form();
        createPurchaseOrder = new JCheckBox();
        if((boolean) Engine.codex("ORDS/SO", "auto_create_po")){
            createPurchaseOrder.setSelected(true);
            createPurchaseOrder.setEnabled(false);
        }
        HashMap<String, String> prs = new HashMap<>();
        for(PurchaseRequisition pr1 : Engine.orderProcessing.getPurchaseRequisitions()){
            prs.put(pr1.getId(), pr1.getId());
        }
        availablePurchaseRequisitions = new Selectable(prs);
        availablePurchaseRequisitions.editable();
        createInboundDelivery = new JCheckBox();
        if((boolean) Engine.codex("ORDS/SO", "create_buyer_inbound")){
            createInboundDelivery.setSelected(true);
            createInboundDelivery.setEnabled(false);
        }
        inboundCarriers = Selectables.carriers();
        inboundTruckId = Elements.input();
        f.addInput(new Label("Create Purchase Order?", Constants.colors[9]), createPurchaseOrder);
        f.addInput(new Label("Purchase Requisition", Constants.colors[8]), availablePurchaseRequisitions);
        f.addInput(new Label("Create IDO", Constants.colors[7]), createInboundDelivery);
        f.addInput(new Label("Transporation Carrier", Constants.colors[6]), inboundCarriers);
        f.addInput(new Label("Truck ID/Number", Constants.colors[5]), inboundTruckId);
        return f;
    }
}