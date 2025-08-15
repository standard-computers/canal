package org.Canal.UI.Views.Finance.SalesOrders;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.DatePicker;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.Pipe;
import org.fife.ui.rtextarea.RTextScrollPane;

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
    private JTextField availablePurchaseRequisitions;
    private JTextField selectSupplier;
    private JTextField selectBillTo;
    private JTextField selectShipTo;
    private Selectable organizations;
    private Selectable outboundCarriers;
    private Selectable inboundCarriers;
    private Selectable buyerObjexType;
    private Selectable ledgers;
    private Copiable orderId;
    private DatePicker expectedDelivery;
    private JCheckBox commitToLedger;
    private JCheckBox createOutboundDelivery;
    private JCheckBox createPurchaseOrder;
    private JCheckBox createInboundDelivery;
    private JTextField descriptionField;
    private JTextField outboundTruckId;
    private JTextField inboundTruckId;
    private RTextScrollPane notes;

    public CreateSalesOrder() {

        super("Create Sales Order", "/ORDS/SO/NEW", true, true, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Item Details", items());
        tabs.addTab("Delivery", delivery());
        tabs.addTab("Ledger", ledger());
        tabs.addTab("Purchase Order", purchaseOrder());
        tabs.addTab("Shipping", shipping());
        tabs.addTab("Notes", notes());

        JPanel coreValues = orderInfoPanel();
        JPanel moreInfo = moreOrderInfoPanel();
        coreValues.setBorder(new EmptyBorder(10, 10, 10, 10));
        moreInfo.setBorder(new EmptyBorder(10, 10, 10, 10));

        setLayout(new BorderLayout());
        JPanel orderInfo = new JPanel(new BorderLayout());
        orderInfo.add(coreValues, BorderLayout.WEST);
        orderInfo.add(moreInfo, BorderLayout.EAST);
        orderInfo.add(Elements.header("Create Sales Order", SwingConstants.LEFT), BorderLayout.NORTH);
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
                PurchaseRequisition assignedPR = Engine.orders.getPurchaseRequisitions(availablePurchaseRequisitions.getText());
                int ccc = JOptionPane.showConfirmDialog(null, "Confirm order?", "You have selected the Org ID as the charge account. Are you sure you want to charge the corp account?",JOptionPane.YES_NO_CANCEL_OPTION);
                if(ccc == JOptionPane.YES_OPTION){
                    SalesOrder newSalesOrder = new SalesOrder();
                    newSalesOrder.setOwner((Engine.getAssignedUser().getId()));
                    newSalesOrder.setOrderId(orderId.value());
                    newSalesOrder.setVendor(selectSupplier.getText());
                    newSalesOrder.setBillTo(selectBillTo.getText());
                    newSalesOrder.setShipTo(selectShipTo.getText());
                    newSalesOrder.setExpectedDelivery(expectedDelivery.getSelectedDateString());
                    newSalesOrder.setStatus(LockeStatus.NEW);
                    ArrayList<org.Canal.Models.BusinessUnits.OrderLineItem> lineitems = new ArrayList<>();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        String itemName = model.getValueAt(row, 0).toString();
                        String itemId = model.getValueAt(row, 1).toString();
                        double itemQty = Double.parseDouble(model.getValueAt(row, 2).toString());
                        double itemPrice = Double.parseDouble(model.getValueAt(row, 3).toString());
                        double itemTotal = Double.parseDouble(model.getValueAt(row, 4).toString());
                        lineitems.add(new org.Canal.Models.BusinessUnits.OrderLineItem(itemName, itemId, itemQty, itemPrice, itemTotal));
                    }
                    newSalesOrder.setItems(lineitems);
                    newSalesOrder.setNetValue(Double.parseDouble(model.getTotalPrice()));
                    newSalesOrder.setTaxAmount(taxRate * Double.parseDouble(model.getTotalPrice()));
                    newSalesOrder.setTotal(taxRate * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice()));
                    newSalesOrder.setOrderedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")));
                    assignedPR.setStatus(LockeStatus.IN_USE);
                    assignedPR.save();
                    Pipe.save("/ORDS/SO", newSalesOrder);

                    if(createPurchaseOrder.isSelected()){
                        PurchaseOrder associatedPurchaseOrder = new PurchaseOrder();
                        String npoid = "OR" + (70000000 + (Engine.getPurchaseOrders().size() + 1));
                        newSalesOrder.setPurchaseOrder(npoid);
                        newSalesOrder.save();
                        associatedPurchaseOrder.setId(npoid);
                        associatedPurchaseOrder.setOwner((Engine.getAssignedUser().getId()));
                        associatedPurchaseOrder.setOrderId(npoid);
                        associatedPurchaseOrder.setVendor(selectSupplier.getText());
                        associatedPurchaseOrder.setBillTo(selectBillTo.getText());
                        associatedPurchaseOrder.setShipTo(selectShipTo.getText());
                        associatedPurchaseOrder.setPurchaseRequisition(availablePurchaseRequisitions.getText());
                        associatedPurchaseOrder.setExpectedDelivery(expectedDelivery.getSelectedDateString());
                        associatedPurchaseOrder.setStatus(LockeStatus.NEW);
                        ArrayList<org.Canal.Models.BusinessUnits.OrderLineItem> li = new ArrayList<>();
                        for (int row = 0; row < model.getRowCount(); row++) {
                            String itemName = model.getValueAt(row, 0).toString();
                            String itemId = model.getValueAt(row, 1).toString();
                            double itemQty = Double.parseDouble(model.getValueAt(row, 2).toString());
                            double itemPrice = Double.parseDouble(model.getValueAt(row, 3).toString());
                            double itemTotal = Double.parseDouble(model.getValueAt(row, 4).toString());
                            li.add(new org.Canal.Models.BusinessUnits.OrderLineItem(itemName, itemId, itemQty, itemPrice, itemTotal));
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
                                t.setObjex(buyerObjexType.getSelectedValue());
                                t.setLocation(selectBillTo.getText());
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

                    if(createOutboundDelivery.isSelected()){
                        Truck t = new Truck();
                        t.setId(((String) Engine.codex("TRANS/TRCKS", "prefix") + 1000 + (Engine.getTrucks().size() + 1)));
                        t.setCarrier(outboundCarriers.getSelectedValue());
                        Pipe.save("/TRANS/TRCKS", t);
                        Delivery d = new Delivery();
                        d.setId(((String) Engine.codex("TRANS/ODO", "prefix") + 1000 + (Engine.getInboundDeliveries().size() + 1)));
                        d.setType("ODO");
                        d.setName("Sales Order from " + selectBillTo.getText());
                        d.setSalesOrder(newSalesOrder.getOrderId());
                        d.setPurchaseOrder(newSalesOrder.getPurchaseOrder());
                        d.setExpectedDelivery(newSalesOrder.getExpectedDelivery());
                        d.setOrigin(selectSupplier.getText());
                        d.setDestination(selectShipTo.getText());
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
                        d.setType("IDO");
                        d.setSalesOrder(newSalesOrder.getOrderId());
                        d.setPurchaseOrder(newSalesOrder.getPurchaseOrder());
                        d.setExpectedDelivery(newSalesOrder.getExpectedDelivery());
                        d.setOrigin(selectSupplier.getText());
                        d.setDestination(selectShipTo.getText());
                        d.setStatus(LockeStatus.PROCESSING);
                        Pipe.save("/TRANS/IDO", d);
                    }

                    if(commitToLedger.isSelected()){
                        Ledger l = Engine.getLedger(ledgers.getSelectedValue());
                        if(l != null){
                            Transaction t = new Transaction();
                            t.setId(Constants.generateId(5));
                            t.setOwner(Engine.getAssignedUser().getId());
                            t.setLocke(getLocke());
                            t.setObjex(buyerObjexType.getSelectedValue());
                            t.setLocation(selectSupplier.getText());
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

    public void setSelectedSupplier(String supplierId){
        selectSupplier.setText(supplierId);
    }

    private JPanel orderInfoPanel(){
        Form f = new Form();
        selectBillTo = Elements.input(15);
        selectShipTo = Elements.input();
        selectSupplier = Elements.input();
        orderId = new Copiable("SO" + (60000000 + (Engine.orders.getSalesOrders().size() + 1)));
        f.addInput(Elements.coloredLabel("*New Order ID", Constants.colors[0]), orderId);
        f.addInput(Elements.coloredLabel("Vendor/Supplier", Constants.colors[1]), selectSupplier);
        f.addInput(Elements.coloredLabel("Bill To (Customer)", Constants.colors[2]), selectBillTo);
        f.addInput(Elements.coloredLabel("Ship To Location", Constants.colors[3]), selectShipTo);
        return f;
    }

    private JPanel moreOrderInfoPanel(){
        Form f = new Form();
        JTextField ordered = new Copiable(Constants.now());
        descriptionField = Elements.input();
        expectedDelivery = new DatePicker();
        f.addInput(Elements.coloredLabel("*Ordered", Constants.colors[10]), ordered);
        f.addInput(Elements.coloredLabel("Description", Constants.colors[9]), descriptionField);
        f.addInput(Elements.coloredLabel("Expected Delivery", Constants.colors[8]), expectedDelivery);
        f.addInput(Elements.coloredLabel("Status", Constants.colors[7]), new Copiable("DRAFT"));
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

    private JPanel items(){

        JPanel p = new JPanel(new BorderLayout());
        ArrayList<Item> items = Engine.products.getProducts();
        if(items.isEmpty()){
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
        IconButton addButton = new IconButton("Add Product", "add_rows", "Add products");
        addButton.addActionListener((ActionEvent _) -> {
            if (!items.isEmpty()) {
                model.addRow(items.getFirst());
            }
        });
        IconButton removeButton = new IconButton("Remove Product", "delete_rows", "Remove selected product");
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

    private JPanel delivery(){

        JPanel delivery = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form p = new Form();
        createOutboundDelivery = new JCheckBox();
        if((boolean) Engine.codex("ORDS/SO", "use_deliveries")){
            createOutboundDelivery.setSelected(true);
            createOutboundDelivery.setEnabled(false);
        }
        outboundCarriers = Selectables.carriers();
        outboundTruckId = Elements.input();
        p.addInput(Elements.coloredLabel("Create Outbound Delivery (ODO) for Supplier", Constants.colors[9]), createOutboundDelivery);
        p.addInput(Elements.coloredLabel("Carrier", Constants.colors[8]), outboundCarriers);
        p.addInput(Elements.coloredLabel("Truck ID/Number", Constants.colors[7]), outboundTruckId);
        delivery.add(p);
        return delivery;
    }

    private JPanel ledger(){

        JPanel ledger = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();
        commitToLedger = new JCheckBox();
        if((boolean) Engine.codex("ORDS/SO", "commit_to_ledger")){
            commitToLedger.setSelected(true);
            commitToLedger.setEnabled(false);
        }
        organizations = Selectables.organizations();
        buyerObjexType = Selectables.locationObjex("/CCS");
        ledgers = Selectables.ledgers();
        f.addInput(Elements.coloredLabel("Commit to Ledger", Constants.colors[9]), commitToLedger);
        f.addInput(Elements.coloredLabel("Trans. Type (Receiving location type)", Constants.colors[8]), buyerObjexType);
        f.addInput(Elements.coloredLabel("Purchasing Org.", Constants.colors[7]), organizations);
        f.addInput(Elements.coloredLabel("Ledger", Constants.colors[6]), ledgers);
        ledger.add(f);
        return ledger;
    }

    private JPanel purchaseOrder(){

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();
        createPurchaseOrder = new JCheckBox();
        if((boolean) Engine.codex("ORDS/SO", "auto_create_po")){
            createPurchaseOrder.setSelected(true);
            createPurchaseOrder.setEnabled(false);
        }
        HashMap<String, String> prs = new HashMap<>();
        for(PurchaseRequisition pr1 : Engine.orders.getPurchaseRequisitions()){
            prs.put(pr1.getId(), pr1.getId());
        }
        availablePurchaseRequisitions = Elements.input();
        createInboundDelivery = new JCheckBox();
        if((boolean) Engine.codex("ORDS/SO", "create_buyer_inbound")){
            createInboundDelivery.setSelected(true);
            createInboundDelivery.setEnabled(false);
        }
        inboundCarriers = Selectables.carriers();
        inboundTruckId = Elements.input();
        f.addInput(Elements.coloredLabel("Create Purchase Order?", Constants.colors[9]), createPurchaseOrder);
        f.addInput(Elements.coloredLabel("Purchase Requisition", Constants.colors[8]), availablePurchaseRequisitions);
        f.addInput(Elements.coloredLabel("Create IDO", Constants.colors[7]), createInboundDelivery);
        f.addInput(Elements.coloredLabel("Transporation Carrier", Constants.colors[6]), inboundCarriers);
        f.addInput(Elements.coloredLabel("Truck ID/Number", Constants.colors[5]), inboundTruckId);
        p.add(f);
        return p;
    }

    private JPanel shipping(){
        JPanel shipping = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return shipping;
    }

    private JScrollPane notes(){

        notes = Elements.simpleEditor();
        return notes;
    }
}