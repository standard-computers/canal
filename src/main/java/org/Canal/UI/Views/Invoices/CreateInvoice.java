package org.Canal.UI.Views.Invoices;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
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
import java.util.Date;
import java.util.HashMap;

/**
 * /INVS/NEW
 */
public class CreateInvoice extends LockeState {

    //Operating Objects
    private DesktopState desktop;
    private Order newInvoice;
    private ItemTableModel model;

    //Value Labels
    private JLabel netAmount;
    private JLabel taxAmount;
    private JLabel totalAmount;

    //Header Data
    private JTextField supplierField;
    private JTextField purchaseRequisitionField;
    private JTextField billToCustomerField;
    private JTextField shipToCustomerField;
    private JTextField salesDocumentField;
    private DatePicker expectedDelivery;
    private DatePicker paymentDue;
    private JTextField accountIdField;

    //Ledger Tab
    private JCheckBox commitToLedger;
    private Selectable organizations;
    private Selectable buyerObjexType;
    private Selectable ledgerId;

    //Delivery Tab
    private JCheckBox createDelivery;
    private Selectable carriers;
    private JTextField truckNumberField;

    private ArrayList<Rate> rates = new ArrayList<>();
    private CustomTable ratesTable;

    //Notes Tab
    private RTextScrollPane notes;

    public CreateInvoice(DesktopState desktop) {

        super("Create Invoice", "/INVS/NEW");
        Constants.checkLocke(this, true, true);
        this.desktop = desktop;
        newInvoice = new Order();

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Items", items());
        tabs.addTab("Delivery", delivery());
        tabs.addTab("Ledger", ledger());
        tabs.addTab("Taxes & Rates", taxes());
        tabs.addTab("Notes", notes());

        JPanel moreInfo = orderInfo();
        moreInfo.setBorder(new EmptyBorder(5, 5, 5, 5));

        setLayout(new BorderLayout());
        JPanel infoHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel infoHolderH = new JPanel(new BorderLayout());
        JPanel orderInfo = new JPanel(new BorderLayout());
        orderInfo.add(orderInfoPanel(), BorderLayout.WEST);
        orderInfo.add(moreInfo, BorderLayout.EAST);
        orderInfo.add(toolbar(), BorderLayout.SOUTH);
        infoHolder.add(orderInfo);
        infoHolderH.add(Elements.header("New Invoice", SwingConstants.LEFT), BorderLayout.NORTH);
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
            String invoiceId = JOptionPane.showInputDialog("Enter Invoice ID");
            Order cpo = Engine.getPurchaseOrder(invoiceId);
            if (cpo != null) {

            } else {
                JOptionPane.showMessageDialog(CreateInvoice.this, "Invalid Invoice");
            }
        });
        p.add(copyFrom);
        p.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review for errors or warnings");
        review.addActionListener(_ -> performReview());
        p.add(review);
        p.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create Invoice");
        create.addActionListener(_ -> {

            if (expectedDelivery.getSelectedDate() == null) {
                JOptionPane.showMessageDialog(null, "Must select a delivery date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            PurchaseRequisition assignedPR = Engine.getPurchaseRequisition(purchaseRequisitionField.getText());
            if ((boolean) Engine.codex("INVS", "require_pr")) {
                if (assignedPR == null) {
                    JOptionPane.showMessageDialog(null, "Purchase Req required for PO", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if ((boolean) Engine.codex("INVS", "vendor_match")) {
                if (!supplierField.getText().equals(assignedPR.getSupplier())) {
                    JOptionPane.showMessageDialog(null, "Selected PO is not for this vendor.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            int ccc = JOptionPane.showConfirmDialog(null, "You have selected the Org ID as the charge account. Are you sure you want to charge the corp account?", "Confirm order?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (ccc == JOptionPane.YES_OPTION) {

                String newInvoiceId = Engine.generateId("INVS");
                newInvoice.setOwner(Engine.getAssignedUser().getId());
                newInvoice.setId(newInvoiceId);
                newInvoice.setOrderId(newInvoiceId);
                newInvoice.setVendor(supplierField.getText());
                newInvoice.setBillTo(billToCustomerField.getText());
                newInvoice.setShipTo(shipToCustomerField.getText());
                newInvoice.setPurchaseRequisition(purchaseRequisitionField.getText());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                newInvoice.setExpectedDelivery(dateFormat.format(expectedDelivery.getSelectedDate()));
                newInvoice.setStatus(LockeStatus.NEW);

                ArrayList<OrderLineItem> lineitems = new ArrayList<>();
                for (int row = 0; row < model.getRowCount(); row++) {
                    String itemName = model.getValueAt(row, 0).toString();
                    String itemId = model.getValueAt(row, 1).toString();
                    double itemQty = Double.parseDouble(model.getValueAt(row, 4).toString());
                    double itemPrice = Double.parseDouble(model.getValueAt(row, 5).toString());
                    double itemTotal = Double.parseDouble(model.getValueAt(row, 6).toString());
                    lineitems.add(new OrderLineItem(itemName, itemId, itemQty, itemPrice, itemTotal));
                }

                newInvoice.setItems(lineitems);
                newInvoice.setRates(rates);
                newInvoice.setOrderedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")));
                newInvoice.setNotes(notes.getTextArea().getText());
                assignedPR.setStatus(LockeStatus.IN_USE);
                assignedPR.save();
                Pipe.save("/INVS", newInvoice);

                if (createDelivery.isSelected()) {

                    Truck t = new Truck();
                    t.setId(((String) Engine.codex("TRANS/TRCKS", "prefix") + 1000 + (Engine.getTrucks().size() + 1)));
                    t.setNumber(truckNumberField.getText());
                    t.setCarrier(carriers.getSelectedValue());
                    Pipe.save("/TRANS/TRCKS", t);

                    Delivery d = new Delivery();
                    d.setType("TRANS/IDO");
                    d.setId(((String) Engine.codex("TRANS/IDO", "prefix") + 1000 + (Engine.getInboundDeliveries().size() + 1)));
                    d.setPurchaseOrder(newInvoice.getOrderId());
                    d.setExpectedDelivery(newInvoice.getExpectedDelivery());
                    d.setDestination(newInvoice.getShipTo());
                    d.setStatus(LockeStatus.PROCESSING);
                    Pipe.save("/TRANS/IDO", d);

                    t.setDelivery(d.getId());
                    t.save();
                }

                if (commitToLedger.isSelected()) {
                    Ledger l = Engine.getLedger(ledgerId.getSelectedValue());
                    if (l != null) {
                        Transaction t = new Transaction();
                        t.setId(Constants.generateId(5));
                        t.setOwner(Engine.getAssignedUser().getId());
                        t.setLocke(getLocke());
                        t.setObjex(buyerObjexType.getSelectedValue());
                        t.setLocation(billToCustomerField.getText());
                        t.setReference(newInvoiceId);
                        t.setAmount(-1 * newInvoice.getTotal());
                        t.setCommitted(Constants.now());
                        t.setStatus(LockeStatus.PROCESSING);
                        l.addTransaction(t);
                        l.setStatus(LockeStatus.IN_USE);
                        l.save();
                    } else {
                        JOptionPane.showMessageDialog(null, "No such ledger.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                dispose();
                JOptionPane.showMessageDialog(null, "Order submitted.");
            }
        });
        p.add(create);
        p.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        refresh.addActionListener(_ -> {

            if (!accountIdField.getText().isEmpty()) {
                Account a = Engine.getAccount(accountIdField.getText());
                if (a != null) {
                    Date paymentDueDate = Constants.addDays(expectedDelivery.getSelectedDate(), (int) a.getTerms());
                    paymentDue.setSelectedDate(paymentDueDate);
                }
            }
        });
        p.add(refresh);
        p.add(Box.createHorizontalStrut(5));

        return p;
    }

    private JPanel orderInfoPanel() {

        JPanel orderInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        billToCustomerField = Elements.input(15);
        shipToCustomerField = Elements.input();
        supplierField = Elements.input();
        salesDocumentField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Supplier"), supplierField);
        form.addInput(Elements.inputLabel("Bill To Customer"), billToCustomerField);
        form.addInput(Elements.inputLabel("Ship To Customer"), shipToCustomerField);
        form.addInput(Elements.inputLabel("Sales Document"), salesDocumentField);
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
                Account foundAccount = Engine.getAccount(accountId);
                if (foundAccount != null) {

                    shipToCustomerField.setText(foundAccount.getCustomer());
                    billToCustomerField.setText(foundAccount.getCustomer());

                    Date paymentDueDate = Constants.addDays(expectedDelivery.getSelectedDate(), (int) foundAccount.getTerms());
                    paymentDue.setSelectedDate(paymentDueDate);

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
                    supplierField.setText(foundPr.getSupplier());
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
        form.addInput(Elements.inputLabel("Account ID"), accountIdField);
        form.addInput(Elements.inputLabel("Purchase Requisition"), purchaseRequisitionField);
        form.addInput(Elements.inputLabel("Expected Delivery"), expectedDelivery);
        form.addInput(Elements.inputLabel("Payment Due"), paymentDue);

        return form;
    }

    private JPanel summary() {

        Form f = new Form();
        DecimalFormat df = new DecimalFormat("#0.00");
        netAmount = Elements.label("$" + model.getTotalPrice());
        f.addInput(Elements.inputLabel("Net Amount"), netAmount);

        //TODO Taxes and Rates
        taxAmount = Elements.label("$" + df.format(Double.parseDouble(model.getTotalPrice())));
        f.addInput(Elements.inputLabel("Tax Amount"), taxAmount);

        totalAmount = Elements.label("$" + df.format(Double.parseDouble("0.0") * Double.parseDouble(model.getTotalPrice()) + Double.parseDouble(model.getTotalPrice())));
        f.addInput(Elements.inputLabel("Total Amount"), totalAmount);
        return f;
    }

    private void updateTotal() {

        DecimalFormat df = new DecimalFormat("#0.00");

        double preTax = Double.parseDouble(model.getTotalPrice());
        newInvoice.setNetValue(preTax);
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

        newInvoice.setTaxAmount(tax);
        taxAmount.setText("$" + df.format(tax));

        newInvoice.setTotal(postTaxAmount);
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

        final JComponent[] jc = {Elements.button("Select Truck")};
        createDelivery = new JCheckBox();
        if ((boolean) Engine.codex("INVS", "use_deliveries")) {
            createDelivery.setSelected(true);
            createDelivery.setEnabled(false);
        }
        carriers = Selectables.carriers();
        truckNumberField = Elements.input();
        jc[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String truckId = JOptionPane.showInputDialog("Enter Truck ID");
                Truck t = null;
                ArrayList<Truck> ts = Engine.getTrucks();
                for (Truck tr : ts) {
                    if (tr.getId().equals(truckId)) {
                        t = tr;
                    }
                }
                if (t != null) {
                    jc[0] = Elements.label(t.getId());
                    repaint();
                    revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "Truck does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        Form form = new Form();
        form.addInput(Elements.inputLabel("Create Inbound Delivery (IDO) for Ship-To"), createDelivery);
        form.addInput(Elements.inputLabel("Carrier"), carriers);
        form.addInput(Elements.inputLabel("Truck (If Empty, makes new)"), jc[0]);
        form.addInput(Elements.inputLabel("Truck Number"), truckNumberField);
        delivery.add(form);

        return delivery;
    }

    private JPanel ledger() {

        JPanel ledger = new JPanel(new FlowLayout(FlowLayout.LEFT));

        commitToLedger = new JCheckBox();
        if ((boolean) Engine.codex("INVS", "commit_to_ledger")) {
            commitToLedger.setSelected(true);
            commitToLedger.setEnabled(false);
        }
        organizations = Selectables.organizations();
        buyerObjexType = Selectables.locationObjex("/DCSS");
        ledgerId = Selectables.ledgers();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Commit to Ledger"), commitToLedger);
        form.addInput(Elements.inputLabel("Trans. Type (Receiving location type)"), buyerObjexType);
        form.addInput(Elements.inputLabel("Purchasing Org."), organizations);
        form.addInput(Elements.inputLabel("Ledger"), ledgerId);
        ledger.add(form);

        return ledger;
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
                        String invoiceId = String.valueOf(t.getValueAt(r, 1));

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
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow != -1) {
//            }
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

        if (Engine.getLocation(supplierField.getText(), "VEND") == null) {
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
                if (!pr.getSupplier().equals(supplierField.getText())) {
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