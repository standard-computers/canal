package org.Canal.UI.Views.Finance.PurchaseOrders;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.DatePicker;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * /ORDS/PO/NEW
 */
public class CreatePurchaseOrder extends LockeState {

    private DesktopState desktop;
    private PurchaseOrder newOrder;
    private ItemTableModel model;
    private double taxRate = 0.05;
    private JLabel netAmount;
    private JLabel taxAmount;
    private JLabel totalAmount;
    private Selectable availablePrs;
    private Selectable selectVendor;
    private Selectable selectBillTo;
    private Selectable selectShipTo;
    private Selectable organizations;
    private Selectable carriers;
    private Selectable buyerObjexType;
    private Selectable ledgerId;
    private Selectable statuses;
    private JTextField orderId;
    private DatePicker expectedDelivery;
    private JCheckBox commitToLedger;
    private JCheckBox createDelivery;
    private Truck truck;

    public CreatePurchaseOrder(DesktopState desktop) {

        super("Create Purchase Order", "/ORDS/PO/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        this.desktop = desktop;
        newOrder = new PurchaseOrder();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Item Details", itemDetails());
        tabs.addTab("Delivery", deliveryDetails());
        tabs.addTab("Ledger", ledgerDetails());

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
        orderInfo.add(buttons(), BorderLayout.SOUTH);
        add(orderInfo, BorderLayout.NORTH);

        add(tabs, BorderLayout.CENTER);
        JPanel orderSummary = new JPanel(new BorderLayout());
        JPanel genSummary = orderSummary();
        orderSummary.add(genSummary, BorderLayout.CENTER);
        add(orderSummary, BorderLayout.SOUTH);
        model.addTableModelListener(_ -> updateTotal());
    }

    private JPanel buttons(){

        JPanel p = new JPanel(new FlowLayout());
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        IconButton copyFrom = new IconButton("Copy From","open", "Review for errors or warnings");
        IconButton review = new IconButton("Review","review", "Review for errors or warnings");
        IconButton create = new IconButton("Create","start", "Create Purchase Order");
        p.add(copyFrom);
        p.add(Box.createHorizontalStrut(5));
        p.add(review);
        p.add(Box.createHorizontalStrut(5));
        p.add(create);
        p.setBorder(new EmptyBorder(5, 5, 5, 5));
        review.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(!selectShipTo.getSelectedValue().equals(selectBillTo.getSelectedValue())){
                    addToQueue(new String[]{"WARNING", "Bill To & Ship To do not match"});
                }
                if(truck == null){
                    addToQueue(new String[]{"WARNING", "Truck not selected. One will be created"});
                }
                if(Engine.getLocation(selectVendor.getSelectedValue(), "VEND") == null){
                    addToQueue(new String[]{"WARNING", "Selected supplier is not a technical vendor"});
                }
                if(availablePrs.getSelectedValue() != null){
                    PurchaseRequisition pr = Engine.orderProcessing.getPurchaseRequisition(availablePrs.getSelectedValue());
                    if(pr == null){
                        addToQueue(new String[]{"ERROR", "Selected Purchase Requisition was not found!!!"});
                    }else{
                        if(!pr.getBuyer().equals(selectBillTo.getSelectedValue())){
                            addToQueue(new String[]{"WARNING", "Bill To does not match buyer in selected Purchase Requisition"});
                        }
                        if(!pr.getBuyer().equals(selectShipTo.getSelectedValue())){
                            addToQueue(new String[]{"WARNING", "Ship To does not match buyer in selected Purchase Requisition"});
                        }
                        if(!pr.getSupplier().equals(selectVendor.getSelectedValue())){
                            addToQueue(new String[]{"WARNING", "Selected Purchase Requisition NOT for selected Vendor!"});
                        }
                    }
                }else{
                    addToQueue(new String[]{"WARNING", "No Purchase Requisition selected. Is this intentional?"});
                }
                desktop.put(new LockeMessages(getQueue()));
                purgeQueue();
            }
        });
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(expectedDelivery.getSelectedDate() == null){
                    JOptionPane.showMessageDialog(null, "Must select a delivery date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                PurchaseRequisition assignedPR = Engine.orderProcessing.getPurchaseRequisitions(availablePrs.getSelectedValue());
                if(!selectVendor.getSelectedValue().equals(assignedPR.getSupplier())){
                    JOptionPane.showMessageDialog(null, "Selected PO is not for this vendor.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int ccc = JOptionPane.showConfirmDialog(null, "You have selected the Org ID as the charge account. Are you sure you want to charge the corp account?", "Confirm order?", JOptionPane.YES_NO_CANCEL_OPTION);
                if(ccc == JOptionPane.YES_OPTION){
                    newOrder.setOwner((Engine.getAssignedUser().getId()));
                    newOrder.setOrderId(orderId.getText());
                    newOrder.setVendor(selectVendor.getSelectedValue());
                    newOrder.setBillTo(selectBillTo.getSelectedValue());
                    newOrder.setShipTo(selectShipTo.getSelectedValue());
                    newOrder.setPurchaseRequisition(availablePrs.getSelectedValue());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    newOrder.setExpectedDelivery(dateFormat.format(expectedDelivery.getSelectedDate()));
                    newOrder.setStatus(LockeStatus.NEW);
                    ArrayList<OrderLineItem> lineitems = new ArrayList<>();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        String itemName = model.getValueAt(row, 0).toString();
                        String itemId = model.getValueAt(row, 1).toString();
                        double itemQty = Double.parseDouble(model.getValueAt(row, 2).toString());
                        double itemPrice = Double.parseDouble(model.getValueAt(row, 3).toString());
                        double itemTotal = Double.parseDouble(model.getValueAt(row, 4).toString());
                        lineitems.add(new OrderLineItem(itemName, itemId, itemQty, itemPrice, itemTotal));
                    }
                    newOrder.setItems(lineitems);
                    newOrder.setNetValue(Double.parseDouble(model.getTotalPrice()));
                    newOrder.setTaxAmount(taxRate * Double.parseDouble(model.getTotalPrice()));
                    newOrder.setTotal(taxRate * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice()));
                    newOrder.setOrderedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")));
                    assignedPR.setStatus(LockeStatus.IN_USE);
                    assignedPR.save();
                    Pipe.save("/ORDS/PO", newOrder);

                    if(createDelivery.isSelected()){

                        Truck t = new Truck();
                        t.setId(((String) Engine.codex("TRANS/TRCKS", "prefix") + 1000 + (Engine.getTrucks().size() + 1)));
                        t.setCarrier(carriers.getSelectedValue());
                        Pipe.save("/TRANS/TRCKS", t);

                        Delivery d = new Delivery();
                        d.setId(((String) Engine.codex("TRANS/IDO", "prefix") + 1000 + (Engine.getInboundDeliveries().size() + 1)));
                        d.setPurchaseOrder(newOrder.getOrderId());
                        d.setExpectedDelivery(newOrder.getExpectedDelivery());
                        d.setDestination(newOrder.getShipTo());
                        d.setStatus(LockeStatus.PROCESSING);
                        Pipe.save("/TRANS/IDO", d);
                    }

                    if(commitToLedger.isSelected()){
                        Ledger l = Engine.getLedger(ledgerId.getSelectedValue());
                        if(l != null){
                            Transaction t = new Transaction();
                            t.setId(Constants.generateId(5));
                            t.setOwner(Engine.getAssignedUser().getId());
                            t.setLocke(getLocke());
                            t.setObjex(buyerObjexType.getSelectedValue());
                            t.setLocation(selectBillTo.getSelectedValue());
                            t.setReference(orderId.getText());
                            t.setAmount(-1 * Double.parseDouble(model.getTotalPrice()));
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
        return p;
    }

    private JPanel orderInfoPanel(){

        Form f = new Form();
        selectBillTo = Selectables.allLocations();
        selectBillTo.editable();
        selectShipTo = Selectables.allLocations();
        selectShipTo.editable();
        selectVendor = Selectables.allLocations();
        selectVendor.editable();
        orderId = Elements.input(((String) Engine.codex("ORDS/PO", "prefix")) + (70000000 + (Engine.getOrders().size() + 1)));
        f.addInput(new Label("*Order ID", Constants.colors[0]), orderId);
        f.addInput(new Label("Supplier/Vendor", Constants.colors[1]), selectVendor);
        f.addInput(new Label("Bill To Location", Constants.colors[2]), selectBillTo);
        f.addInput(new Label("Ship To Location", Constants.colors[3]), selectShipTo);
        return f;
    }

    private JPanel moreOrderInfoPanel(){

        Form f = new Form();
        JTextField ordered = new Copiable(LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        HashMap<String, String> prs = new HashMap<>();
        for(PurchaseRequisition pr1 : Engine.orderProcessing.getPurchaseRequisitions()){
            prs.put(pr1.getId(), pr1.getId());
        }
        availablePrs = new Selectable(prs);
        availablePrs.editable();
        expectedDelivery = new DatePicker();
        statuses = Selectables.statusTypes();
        f.addInput(new Label("*Ordered On", Constants.colors[10]), ordered);
        f.addInput(new Label("Purchase Requisition", Constants.colors[9]), availablePrs);
        f.addInput(new Label("Expected Delivery", Constants.colors[8]), expectedDelivery);
        f.addInput(new Label("Status", Constants.colors[7]), statuses);
        return f;
    }

    private JPanel orderSummary(){

        Form f = new Form();
        DecimalFormat df = new DecimalFormat("#0.00");
        netAmount = Elements.label("$" + model.getTotalPrice());
        f.addInput(new Label("Net Amount", UIManager.getColor("Label.foreground")), netAmount);
        taxAmount = Elements.label("$" + df.format(taxRate * Double.parseDouble(model.getTotalPrice())));

        JTextField taxRateField = Elements.input("0.05");
        f.addInput(new Label("Tax Rate", UIManager.getColor("Label.foreground")), taxRateField);
        f.addInput(new Label("Tax Amount", UIManager.getColor("Label.foreground")), taxAmount);
        totalAmount = Elements.label("$" + df.format(Double.parseDouble(taxRateField.getText()) * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice())));
        f.addInput(new Label("Total Amount", Constants.colors[5]), totalAmount);
        return f;
    }

    private void updateTotal(){

        DecimalFormat df = new DecimalFormat("#0.00");
        netAmount.setText("$" + model.getTotalPrice());
        taxAmount.setText("$" + df.format(taxRate * Double.parseDouble(model.getTotalPrice())));
        totalAmount.setText("$" + df.format(taxRate * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice())));
    }

    private JPanel itemDetails(){

        JPanel p = new JPanel(new BorderLayout());
        ArrayList<Item> items = Engine.getProducts();
        if(items.isEmpty()){
            JOptionPane.showMessageDialog(this, "No products found", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
        model = new ItemTableModel(Collections.singletonList(items.getFirst()));
        JTable table = new JTable(model);

        TableCellRenderer centerRenderer = new CenteredRenderer();
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
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
        createDelivery = new JCheckBox();
        if((boolean) Engine.codex("ORDS/PO", "use_deliveries")){
            createDelivery.setSelected(true);
            createDelivery.setEnabled(false);
        }
        carriers = Selectables.carriers();
        JButton selectTruck = Elements.button("Select Truck");
        p.addInput(new Label("Create Inbound Delivery (IDO) for Ship-To", Constants.colors[9]), createDelivery);
        p.addInput(new Label("Carrier", Constants.colors[8]), carriers);
        p.addInput(new Label("Truck (If Empty, makes new)", Constants.colors[9]), selectTruck);
        return p;
    }

    private JPanel ledgerDetails(){

        Form f = new Form();
        commitToLedger = new JCheckBox();
        if((boolean) Engine.codex("ORDS/PO", "commit_to_ledger")){
            commitToLedger.setSelected(true);
            commitToLedger.setEnabled(false);
        }
        organizations = Selectables.organizations();
        buyerObjexType = Selectables.locationObjex("/DCSS");
        ledgerId = Selectables.ledgers();
        f.addInput(new Label("Commit to Ledger", UIManager.getColor("Label.foreground")), commitToLedger);
        f.addInput(new Label("Trans. Type (Receiving location type)", UIManager.getColor("Label.foreground")), buyerObjexType);
        f.addInput(new Label("Purchasing Org.", UIManager.getColor("Label.foreground")), organizations);
        f.addInput(new Label("Ledger", UIManager.getColor("Label.foreground")), ledgerId);
        return f;
    }
}