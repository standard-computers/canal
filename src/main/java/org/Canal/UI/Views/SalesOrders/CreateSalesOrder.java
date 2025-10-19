package org.Canal.UI.Views.SalesOrders;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * /ORDS/SO/NEW
 */
public class CreateSalesOrder extends LockeState {

    //Operating Objects
    private DesktopState desktop;
    private Order salesOrder;
    private ItemTableModel model;

    //Value Labels
    private JLabel netAmount;
    private JLabel taxAmount;
    private JLabel totalAmount;

    //Header Data
    private JTextField vendorField;
    private JTextField purchaseRequisitionField;
    private JTextField billToCustomerField;
    private JTextField shipToCustomerField;
    private JTextField salesDocumentField;
    private DatePicker expectedDelivery;
    private DatePicker paymentDue;
    private JTextField accountIdField;

    private JCheckBox createPurchaseOrder;
    private JCheckBox commitPoToLedger;

    //Ledger Tab
    private JCheckBox commitSoToLedger;
    private Selectable organizations;
    private Selectable buyerObjexType;
    private Selectable ledgerId;

    //Delivery Tab
    private JCheckBox createInboundDelivery;
    private JCheckBox createOutboundDelivery;
    private Selectable carriers;
    private JTextField truckNumberField;
    private Truck truck;

    private ArrayList<Rate> rates = new ArrayList<>();
    private CustomTable ratesTable;

    //Notes Tab
    private RTextScrollPane notes;

    public CreateSalesOrder(DesktopState desktop) {

        super("Create Sales Order", "/ORDS/SO/NEW");
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        this.desktop = desktop;
        this.salesOrder = new Order();

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Items", items());
        tabs.addTab("Delivery", delivery());
        tabs.addTab("Ledger", ledger());
        tabs.addTab("Purchase Order", purchaseOrder());
        tabs.addTab("Shipping", shipping());
        tabs.addTab("Packaging", packaging());
        tabs.addTab("Taxes & Rates", taxes());

        if ((boolean) Engine.codex.getValue("ORDS/SO", "allow_notes")) {
            tabs.addTab("Notes", notes());
        }

        JPanel moreInfo = orderInfo();
        moreInfo.setBorder(new EmptyBorder(10, 10, 10, 10));

        setLayout(new BorderLayout());
        JPanel infoHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel infoHolderH = new JPanel(new BorderLayout());
        JPanel orderInfo = new JPanel(new BorderLayout());
        orderInfo.add(orderInfoPanel(), BorderLayout.WEST);
        orderInfo.add(moreInfo, BorderLayout.EAST);
        orderInfo.add(toolbar(), BorderLayout.SOUTH);
        infoHolder.add(orderInfo);
        infoHolderH.add(Elements.header("New Sales Order", SwingConstants.LEFT), BorderLayout.NORTH);
        infoHolderH.add(infoHolder, BorderLayout.CENTER);
        add(infoHolderH, BorderLayout.NORTH);

        add(tabs, BorderLayout.CENTER);
        JPanel orderSummary = new JPanel(new BorderLayout());
        JPanel genSummary = summary();
        orderSummary.add(genSummary, BorderLayout.CENTER);
        add(orderSummary, BorderLayout.SOUTH);
        model.addTableModelListener(_ -> updateTotal());
    }

    private JPanel toolbar() {

        JPanel p = new JPanel(new FlowLayout());
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Review for errors or warnings");
        copyFrom.addActionListener(_ -> {
            String salesOrderId = JOptionPane.showInputDialog("Enter Sales Order ID");
            Order foundSalesOrder = Engine.getSalesOrder(salesOrderId);
            if (foundSalesOrder != null) {

                vendorField.setText(foundSalesOrder.getVendor());
                purchaseRequisitionField.setText(foundSalesOrder.getPurchaseRequisition());
                billToCustomerField.setText(foundSalesOrder.getBillTo());
                shipToCustomerField.setText(foundSalesOrder.getShipTo());

                for (int i = 0; i < foundSalesOrder.getItems().size(); i++) {

                    OrderLineItem it = foundSalesOrder.getItems().get(i);
                    Item fi = Engine.getItem(it.getId());
                    model.addRow(fi);
                    model.setValueAt(it.getQuantity(), i, 4);
                }
                updateTotal();

            } else {
                JOptionPane.showMessageDialog(CreateSalesOrder.this, "Invalid Sales Order");
            }
        });
        p.add(copyFrom);
        p.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review for errors or warnings");
        review.addActionListener(_ -> performReview());
        p.add(review);
        p.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create Sales Order");
        create.addActionListener(_ -> {

            PurchaseRequisition assignedPR = Engine.getPurchaseRequisition(purchaseRequisitionField.getText());

            int ccc = JOptionPane.showConfirmDialog(null, "You have selected the Org ID as the charge account. Are you sure you want to charge the corp account?", "Confirm order?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (ccc == JOptionPane.YES_OPTION) {

                System.out.println("Creating Sales Order");
                //Set Sales Order Data
                String newSalesOrderId = Engine.generateId("ORDS/SO");
                salesOrder.setType("ORDS/SO");
                salesOrder.setOwner((Engine.getAssignedUser().getId()));
                salesOrder.setId(newSalesOrderId);
                salesOrder.setOrderId(newSalesOrderId);
                salesOrder.setVendor(vendorField.getText());
                salesOrder.setBillTo(billToCustomerField.getText());
                salesOrder.setShipTo(shipToCustomerField.getText());
                //TODO set account
                salesOrder.setPurchaseRequisition(purchaseRequisitionField.getText());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                salesOrder.setExpectedDelivery(dateFormat.format(expectedDelivery.getSelectedDate()));
                //TODO Payment date for account
                salesOrder.setStatus(LockeStatus.NEW);

                ArrayList<OrderLineItem> lineitems = new ArrayList<>();
                for (int row = 0; row < model.getRowCount(); row++) {
                    String itemName = model.getValueAt(row, 0).toString();
                    String itemId = model.getValueAt(row, 1).toString();
                    double itemQty = Double.parseDouble(model.getValueAt(row, 4).toString());
                    double itemPrice = Double.parseDouble(model.getValueAt(row, 5).toString());
                    double itemTotal = Double.parseDouble(model.getValueAt(row, 6).toString());
                    lineitems.add(new OrderLineItem(itemName, itemId, itemQty, itemPrice, itemTotal));
                }

                salesOrder.setItems(lineitems);
                salesOrder.setRates(rates);
                salesOrder.setOrderedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")));
                salesOrder.setNotes(notes.getTextArea().getText());
                Pipe.save("/ORDS/SO", salesOrder);
                System.out.println("Sales Order Saved");

                if (assignedPR != null) {
                    System.out.println("Updating Purchase Requisition");
                    assignedPR.setStatus(LockeStatus.IN_USE);
                    assignedPR.save();
                }

                Order purchaseOrder = new Order();
                if (createPurchaseOrder.isSelected()) {
                    System.out.println("Creating Purchase Order");
                    purchaseOrder = salesOrder;
                    purchaseOrder.setId(Engine.generateId("ORDS/PO"));
                    purchaseOrder.setType("ORDS/PO");
                    Pipe.save("/ORDS/PO", purchaseOrder);
                    System.out.println("Puchase Order Saved");
                }


                Truck truck = new Truck();
                if (truckNumberField.getText().isEmpty()) { //If truck number provided

                    System.out.println("Creating Truck");
                    truck.setId(Engine.generateId("TRANS/TRCKS"));
                    truck.setNumber(truckNumberField.getText());
                    truck.setCarrier(carriers.getSelectedValue());
                    Pipe.save("/TRANS/TRCKS", truck);
                    System.out.println("Truck Saved");
                } else {

                    System.out.println("Getting Selected Truck");
                    truck = Engine.getTruck(truckNumberField.getText());
                }

                if (createInboundDelivery.isSelected()) {

                    System.out.println("Creating Inbound Delivery");
                    Delivery inboundDelivery = new Delivery();
                    inboundDelivery.setType("TRANS/IDO");
                    inboundDelivery.setId(Engine.generateId("TRANS/IDO"));
                    inboundDelivery.setSalesOrder(salesOrder.getOrderId());
                    if (createPurchaseOrder.isSelected()) inboundDelivery.setPurchaseOrder(purchaseOrder.getId());
                    inboundDelivery.setExpectedDelivery(salesOrder.getExpectedDelivery());
                    inboundDelivery.setDestination(salesOrder.getShipTo());
                    inboundDelivery.setStatus(LockeStatus.PROCESSING);
                    inboundDelivery.setTruck(truck.getId());
                    Pipe.save("/TRANS/IDO", inboundDelivery);
                    System.out.println("Inbound Delivery Saved");

                    truck.setDelivery(inboundDelivery.getId());
                    truck.save();
                    System.out.println("Saved Truck");
                }

                if (createOutboundDelivery.isSelected()) {

                    System.out.println("Creating Outbound Delivery");
                    Delivery outboundDelivery = new Delivery();
                    outboundDelivery.setType("TRANS/ODO");
                    outboundDelivery.setId(Engine.generateId("TRANS/ODO"));
                    outboundDelivery.setSalesOrder(salesOrder.getOrderId());
                    if (createPurchaseOrder.isSelected()) outboundDelivery.setPurchaseOrder(purchaseOrder.getId());
                    outboundDelivery.setExpectedDelivery(salesOrder.getExpectedDelivery());
                    outboundDelivery.setOrigin(salesOrder.getVendor());
                    outboundDelivery.setDestination(salesOrder.getShipTo());
                    outboundDelivery.setStatus(LockeStatus.PROCESSING);
                    outboundDelivery.setTruck(truck.getId());
                    Pipe.save("/TRANS/ODO", outboundDelivery);
                    System.out.println("Outbound Delivery Saved");
                }

                //Commit Sales Order transaction to ledger
                if (commitSoToLedger.isSelected()) {

                    System.out.println("Committing Sales Order to Ledger");
                    Ledger supplierLedger = Engine.hasLedger(salesOrder.getVendor());
                    if(supplierLedger == null){
                        supplierLedger = Engine.getLedger(ledgerId.getSelectedValue());
                    }
                    if (supplierLedger != null) {
                        Transaction transaction = new Transaction();
                        transaction.setId(Constants.generateId(5));
                        transaction.setOwner(Engine.getAssignedUser().getId());
                        transaction.setLocke(getLocke());
                        transaction.setObjex(buyerObjexType.getSelectedValue());
                        transaction.setLocation(salesOrder.getVendor());
                        transaction.setReference(newSalesOrderId);
                        transaction.setAmount(salesOrder.getTotal());
                        transaction.setCommitted(Constants.now());
                        transaction.setStatus(LockeStatus.PROCESSING);
                        supplierLedger.addTransaction(transaction);
                        supplierLedger.setStatus(LockeStatus.IN_USE);
                        supplierLedger.save();
                        System.out.println("Transaction saved");
                    } else {
                        JOptionPane.showMessageDialog(null, "No such ledger.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                //Commit Purchase Order transaction to ledger
                if (commitPoToLedger.isSelected() && createPurchaseOrder.isSelected()) {

                    System.out.println("Committing Purchase Order to Ledger");
                    Ledger billToLedger = Engine.hasLedger(salesOrder.getBillTo());
                    if(billToLedger == null){
                        billToLedger = Engine.hasLedger(purchaseOrder.getBillTo());
                    }
                    if (billToLedger != null) {
                        Transaction transaction = new Transaction();
                        transaction.setId(Constants.generateId(5));
                        transaction.setOwner(Engine.getAssignedUser().getId());
                        transaction.setLocke(getLocke());
                        transaction.setObjex(buyerObjexType.getSelectedValue());
                        transaction.setLocation(salesOrder.getBillTo());
                        transaction.setReference(salesOrder.getId());
                        transaction.setAmount(-1 * salesOrder.getTotal());
                        transaction.setCommitted(Constants.now());
                        transaction.setStatus(LockeStatus.PROCESSING);
                        billToLedger.addTransaction(transaction);
                        billToLedger.setStatus(LockeStatus.IN_USE);
                        billToLedger.save();
                        System.out.println("Purchase Order Transaction Saved");
                    } else {
                        JOptionPane.showMessageDialog(null, "No such ledger.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                System.out.println("End Process");

                dispose();
                JOptionPane.showMessageDialog(null, "Order submitted.");
            }
        });
        p.add(create);
        p.add(Box.createHorizontalStrut(5));

        return p;
    }

    private JPanel orderInfoPanel() {

        JPanel orderInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        billToCustomerField = Elements.input(15);
        shipToCustomerField = Elements.input();
        vendorField = Elements.input();
        salesDocumentField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Supplier", Constants.colors[1]), vendorField);
        form.addInput(Elements.coloredLabel("Bill To Customer", Constants.colors[2]), billToCustomerField);
        form.addInput(Elements.coloredLabel("Ship To Customer", Constants.colors[3]), shipToCustomerField);
        form.addInput(Elements.coloredLabel("Sales Document", Constants.colors[4]), salesDocumentField);
        orderInfo.add(form);

        return orderInfo;
    }

    private JPanel orderInfo() {

        HashMap<String, String> prs = new HashMap<>();
        for (PurchaseRequisition pr1 : Engine.getPurchaseRequisitions()) {
            prs.put(pr1.getId(), pr1.getId());
        }

        accountIdField = Elements.input(15);
        accountIdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChange();
            }

            private void onChange() {

                String accountId = accountIdField.getText();
                Account account = Engine.getAccount(accountId);
                if (account != null) {


                    shipToCustomerField.setText(account.getCustomer());
                    billToCustomerField.setText(account.getCustomer());
                }
            }
        });

        purchaseRequisitionField = Elements.input();
        purchaseRequisitionField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChange();
            }

            private void onChange() {

                String prid = purchaseRequisitionField.getText();
                PurchaseRequisition foundPr = Engine.getPurchaseRequisition(prid);
                if (foundPr != null) {
                    vendorField.setText(foundPr.getSupplier());
                    shipToCustomerField.setText(foundPr.getBuyer());
                    billToCustomerField.setText(foundPr.getBuyer());

                    if (!foundPr.getItems().isEmpty()) {
                        model.empty();
                    }

                    for (int i = 0; i < foundPr.getItems().size(); i++) {

                        OrderLineItem it = foundPr.getItems().get(i);
                        Item fi = Engine.getItem(it.getId());

                        model.addRow(fi);
                        model.setValueAt(it.getQuantity(), i, 4);
                    }
                    updateTotal();
                }
            }
        });

        expectedDelivery = new DatePicker();
        paymentDue = new DatePicker();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Account ID", Constants.colors[9]), accountIdField);
        form.addInput(Elements.coloredLabel("Purchase Requisition", Constants.colors[8]), purchaseRequisitionField);
        form.addInput(Elements.coloredLabel("Expected Delivery", Constants.colors[7]), expectedDelivery);
        form.addInput(Elements.coloredLabel("Payment Due", Constants.colors[6]), paymentDue);

        return form;
    }

    private JPanel summary() {

        Form f = new Form();
        DecimalFormat df = new DecimalFormat("#0.00");
        netAmount = Elements.label("$" + model.getTotalPrice());
        f.addInput(Elements.coloredLabel("Net Amount", UIManager.getColor("Label.foreground")), netAmount);

        //TODO Taxes and Rates
        taxAmount = Elements.label("$" + df.format(Double.parseDouble(model.getTotalPrice())));
        f.addInput(Elements.coloredLabel("Tax Amount", UIManager.getColor("Label.foreground")), taxAmount);

        totalAmount = Elements.label("$" + df.format(Double.parseDouble("0.0") * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice())));
        f.addInput(Elements.coloredLabel("Total Amount", UIManager.getColor("Label.foreground")), totalAmount);
        return f;
    }

    private void updateTotal() {

        DecimalFormat df = new DecimalFormat("#0.00");

        double preTax = Double.parseDouble(model.getTotalPrice());
        salesOrder.setNetValue(preTax);
        netAmount.setText("$" + preTax);

        double tax = 0.0;
        for (Rate r : rates) {
            if (r.isTax()) {
                if (r.isPercent()) {
                    tax += preTax * r.getValue();
                } else {
                    tax += r.getValue();
                }
            } else {
                tax += r.getValue();
            }
        }
        double postTaxAmount = preTax + tax;

        salesOrder.setTaxAmount(tax);
        taxAmount.setText("$" + df.format(tax));

        salesOrder.setTotal(postTaxAmount);
        totalAmount.setText("$" + df.format(postTaxAmount));
    }

    private JPanel items() {

        JPanel p = new JPanel(new BorderLayout());
        ArrayList<Item> items = Engine.getItems();
        if (items.isEmpty()) {
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

        JTextField itemIdField = new JTextField(items.getFirst().getId());
        itemIdField.addActionListener(_ -> updateTotal());
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(itemIdField));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setBackground(UIManager.getColor("Panel.background"));

        IconButton addItem = new IconButton("Add Item", "add_rows", "Add item");
        addItem.addActionListener((ActionEvent _) -> {
            if (!items.isEmpty()) {
                model.addRow(items.getFirst());
            }
        });
        buttons.add(addItem);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton removeItem = new IconButton("Remove Item", "delete_rows", "Remove selected item");
        removeItem.addActionListener((ActionEvent _) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
            }
        });
        buttons.add(removeItem);
        buttons.add(Box.createHorizontalStrut(5));

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(600, 300));
        p.add(sp, BorderLayout.CENTER);
        p.add(buttons, BorderLayout.NORTH);
        return p;
    }

    private JPanel delivery() {

        JPanel delivery = new JPanel(new FlowLayout(FlowLayout.LEFT));

        createInboundDelivery = new JCheckBox();
        createOutboundDelivery = new JCheckBox();
        if ((boolean) Engine.codex("ORDS/SO", "use_deliveries")) {
            createInboundDelivery.setSelected(true);
            createInboundDelivery.setEnabled(false);
            createOutboundDelivery.setSelected(true);
            createOutboundDelivery.setEnabled(false);
        }
        carriers = Selectables.carriers();
        truckNumberField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Create Inbound Delivery for Ship-To", Constants.colors[10]), createInboundDelivery);
        form.addInput(Elements.coloredLabel("Create Outbound Delivery for Supplier", Constants.colors[9]), createOutboundDelivery);
        form.addInput(Elements.coloredLabel("Carrier", Constants.colors[8]), carriers);
        form.addInput(Elements.coloredLabel("Truck Number", Constants.colors[7]), truckNumberField);
        delivery.add(form);

        return delivery;
    }

    private JPanel ledger() {

        JPanel ledger = new JPanel(new FlowLayout(FlowLayout.LEFT));

        commitSoToLedger = new JCheckBox();
        if ((boolean) Engine.codex("ORDS/SO", "commit_to_ledger")) {
            commitSoToLedger.setSelected(true);
            commitSoToLedger.setEnabled(false);
        }
        organizations = Selectables.organizations();
        buyerObjexType = Selectables.locationObjex("/DCSS");
        ledgerId = Selectables.ledgers();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Commit to Ledger",  Constants.colors[0]), commitSoToLedger);
        form.addInput(Elements.coloredLabel("Trans. Type (Receiving location type)",  Constants.colors[1]), buyerObjexType);
        form.addInput(Elements.coloredLabel("Purchasing Org.",  Constants.colors[2]), organizations);
        form.addInput(Elements.coloredLabel("Ledger", Constants.colors[3]), ledgerId);
        ledger.add(form);

        return ledger;
    }

    private JPanel purchaseOrder() {

        JPanel purchaseOrder = new JPanel(new FlowLayout(FlowLayout.LEFT));

        createPurchaseOrder = new JCheckBox();
        commitPoToLedger = new JCheckBox();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Create Purchase Order for Supplier", Constants.colors[10]), createPurchaseOrder);
        form.addInput(Elements.coloredLabel("Commit PO to Ledger", Constants.colors[9]), commitPoToLedger);
        purchaseOrder.add(form);

        return purchaseOrder;
    }

    private JPanel shipping() {

        JPanel shipping = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return shipping;
    }

    private JPanel packaging() {

        JPanel packaging = new JPanel(new BorderLayout());

        ArrayList<Object[]> stockLines = new ArrayList<>();

        CustomTable packagingTable = new CustomTable(new String[]{
                "HU",
                "Item Name",
                "Qty",
                "Qty UOM",
                "Value",
                "Status",
                "Weight",
                "Volume",
        }, stockLines);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setBackground(UIManager.getColor("Panel.background"));
        IconButton addButton = new IconButton("Add Line", "add_rows", "Add line");
        addButton.addActionListener((ActionEvent _) -> {
            if (!stockLines.isEmpty()) {
            }
        });
        buttons.add(addButton);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton removeButton = new IconButton("Remove Line", "delete_rows", "Remove selected line");
        removeButton.addActionListener((ActionEvent _) -> {
        });
        buttons.add(removeButton);
        buttons.add(Box.createHorizontalStrut(5));

        JScrollPane sp = new JScrollPane(packagingTable);
        sp.setPreferredSize(new Dimension(600, 300));
        packaging.add(sp, BorderLayout.CENTER);
        packaging.add(buttons, BorderLayout.NORTH);

        return packaging;
    }

    private CustomTable taxesAndRatesTable() {
        String[] columns = new String[]{
                "#",
                "ID",
                "Name",
                "Description",
                "Percent",
                "Tax",
                "Value",
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for (int s = 0; s < rates.size(); s++) {
            Rate rate = rates.get(s);
            data.add(new Object[]{
                    String.valueOf(s + 1),
                    rate.getId(),
                    rate.getName(),
                    rate.getDescription(),
                    rate.isPercent(),
                    rate.isTax(),
                    rate.getValue()
            });
        }

        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));

                    }
                }
            }
        });
        return ct;
    }

    private JPanel taxes() {

        JPanel taxes = new JPanel(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setBackground(UIManager.getColor("Panel.background"));

        IconButton addButton = new IconButton("Add Tax", "add_rows", "Add tax");
        addButton.addActionListener((ActionEvent _) -> {
            String rateId = JOptionPane.showInputDialog("Enter the Tax or Rate ID");
            Rate rate = Engine.getRate(rateId);
            rates.add(rate);
            refreshTaxesAndRates();
            updateTotal();
        });
        toolbar.add(addButton);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton removeButton = new IconButton("Remove Tax", "delete_rows", "Remove selected tax");
        removeButton.addActionListener((ActionEvent _) -> {
        });
        toolbar.add(removeButton);
        toolbar.add(Box.createHorizontalStrut(5));

        taxes.add(toolbar, BorderLayout.NORTH);
        ratesTable = taxesAndRatesTable();
        taxes.add(new JScrollPane(ratesTable), BorderLayout.CENTER);

        return taxes;
    }

    private void refreshTaxesAndRates() {

        CustomTable newTable = taxesAndRatesTable();
        JScrollPane scrollPane = (JScrollPane) ratesTable.getParent().getParent();
        scrollPane.setViewportView(newTable);
        ratesTable = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    private void performReview() {

        if (!shipToCustomerField.getText().equals(billToCustomerField.getText())) {
            addToQueue(new String[]{"WARNING", "Bill To & Ship To do not match"});
        }
        if (truck == null) {
            addToQueue(new String[]{"WARNING", "Truck not selected. One will be created"});
        }
        if (Engine.getLocation(vendorField.getText(), "VEND") == null) {
            addToQueue(new String[]{"WARNING", "Selected supplier is not a technical vendor"});
        }
        if (rates.isEmpty()) {
            addToQueue(new String[]{"WARNING", "No rates or taxes have been added. ARE YOU SURE?"});
        }
        if (purchaseRequisitionField.getText().equals("Available")) {
            PurchaseRequisition pr = Engine.getPurchaseRequisition(purchaseRequisitionField.getText());
            if (pr == null) {
                addToQueue(new String[]{"ERROR", "Selected Purchase Requisition was not found!!!"});
            } else {
                if (!pr.getBuyer().equals(billToCustomerField.getText())) {
                    addToQueue(new String[]{"WARNING", "Bill To does not match buyer in selected Purchase Requisition"});
                }
                if (!pr.getBuyer().equals(shipToCustomerField.getText())) {
                    addToQueue(new String[]{"WARNING", "Ship To does not match buyer in selected Purchase Requisition"});
                }
                if (!pr.getSupplier().equals(vendorField.getText())) {
                    addToQueue(new String[]{"WARNING", "Selected Purchase Requisition NOT for selected Vendor!"});
                }
            }
        } else {
            addToQueue(new String[]{"WARNING", "No Purchase Requisition selected. Is this intentional?"});
        }
        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }
}