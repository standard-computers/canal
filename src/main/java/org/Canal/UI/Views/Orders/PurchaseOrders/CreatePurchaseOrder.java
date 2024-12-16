package org.Canal.UI.Views.Orders.PurchaseOrders;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Inputs.DatePicker;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;
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

    private PurchaseOrder newOrder;
    private ItemTableModel model;
    private double taxRate = 0.05;
    private JLabel netValue;
    private JLabel taxAmount;
    private JLabel totalAmount;
    private Selectable availablePrs, selectVendor, selectBillTo, selectShipTo, organizations;
    private Copiable orderId;
    private DatePicker expectedDelivery;
    private JCheckBox commitToLedger, makeSalesOrder, createDelivery;

    public CreatePurchaseOrder() {
        super("Create Purchase Order", "/ORDS/PO/NEW", false, true, false, true);
        Constants.checkLocke(this, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
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
                PurchaseRequisition assignedPR = Engine.orderProcessing.getPurchaseRequisitions(availablePrs.getSelectedValue());
                if(!selectVendor.getSelectedValue().equals(assignedPR.getSupplier())){
                    JOptionPane.showMessageDialog(null, "Selected PO is not for this vendor.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int ccc = JOptionPane.showConfirmDialog(null, "Confirm order?", "You have selected the Org ID as the charge account. Are you sure you want to charge the corp account?",JOptionPane.YES_NO_CANCEL_OPTION);
                if(ccc == JOptionPane.YES_OPTION){
                    newOrder.setOrderId(orderId.value());
                    newOrder.setVendor(selectVendor.getSelectedValue());
                    newOrder.setBillTo(selectBillTo.getSelectedValue());
                    newOrder.setShipTo(selectShipTo.getSelectedValue());
                    newOrder.setPurchaseRequisition(availablePrs.getSelectedValue());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    newOrder.setExpectedDelivery(dateFormat.format(expectedDelivery.getSelectedDate()));
                    newOrder.setStatus(LockeStatus.NEW);
                    ArrayList<OrderLineItem> lineitems = new ArrayList<>();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        for (int col = 0; col < model.getColumnCount(); col++) {
                            Object value = model.getValueAt(row, col);
                        }
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
                    dispose();
                    JOptionPane.showMessageDialog(null, "Order submitted.");
                }
            }
        });
        model.addTableModelListener(_ -> updateTotal());
    }

    public void setBuyer(String buyerId){

    }

    private JPanel orderInfoPanel(){
        Form f = new Form();
        selectBillTo = Selectables.allLocations();
        selectBillTo.editable();
        selectShipTo = Selectables.allLocations();
        selectShipTo.editable();
        selectVendor = Selectables.vendors();
        selectVendor.editable();
        orderId = new Copiable("OR" + (70000000 + (Engine.getOrders().size() + 1)));
        f.addInput(new Label("*Order ID", Constants.colors[0]), orderId);
        f.addInput(new Label("Supplier/Vendor", Constants.colors[1]), selectVendor);
        f.addInput(new Label("Bill To", Constants.colors[2]), selectBillTo);
        f.addInput(new Label("Ship To", Constants.colors[3]), selectShipTo);
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
        f.addInput(new Label("*Ordered", UIManager.getColor("Label.foreground")), ordered);
        f.addInput(new Label("Purchase Requisition", UIManager.getColor("Label.foreground")), availablePrs);
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
        createDelivery = new JCheckBox();
        p.addInput(new Label("", Constants.colors[9]), createDelivery);
        return p;
    }

    private JPanel ledgerDetails(){
        Form f = new Form();
        commitToLedger = new JCheckBox();
        makeSalesOrder = new JCheckBox();
        if((boolean) Engine.codex("ORDS/PO", "commit_to_ledger")){
            commitToLedger.setSelected(true);
        }
        organizations = Selectables.organizations();
        JTextField ledgerId = Elements.input();
        f.addInput(new Label("Commit to Ledger", UIManager.getColor("Label.foreground")), commitToLedger);
        f.addInput(new Label("Trans. Type", UIManager.getColor("Label.foreground")), new Copiable("/DCSS"));
        f.addInput(new Label("Purchasing Org.", UIManager.getColor("Label.foreground")), organizations);
        f.addInput(new Label("Ledger", UIManager.getColor("Label.foreground")), ledgerId);
        return f;
    }
}