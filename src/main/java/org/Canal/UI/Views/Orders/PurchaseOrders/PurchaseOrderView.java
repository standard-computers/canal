package org.Canal.UI.Views.Orders.PurchaseOrders;

import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Inputs.DatePicker;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * /ORDS/NEW, /ORDS/PO/NEW
 */
public class PurchaseOrderView extends LockeState {

    private PurchaseOrder po;
    private ItemTableModel model;
    private double taxRate = 0.05;
    private JLabel netValue;
    private JLabel taxAmount;
    private JLabel totalAmount;
    private Copiable poPurchaseReq;
    private Copiable selectVendor;
    private Copiable selectBillTo;
    private Copiable selectShipTo;
    private Copiable orderId;
    private DatePicker expectedDelivery;

    public PurchaseOrderView(PurchaseOrder po) {
        super("Purchase Order / " + po.getOrderId(), "/", false, true, false, true);
        Constants.checkLocke(this, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        this.po = po;
        JPanel coreValues = orderInfoPanel();
        JPanel moreInfo = moreOrderInfoPanel();
        coreValues.setBorder(new EmptyBorder(10, 10, 10, 10));
        moreInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel orderInfo = new JPanel(new BorderLayout());
        orderInfo.add(coreValues, BorderLayout.WEST);
        orderInfo.add(moreInfo, BorderLayout.EAST);
        setLayout(new BorderLayout());
        add(orderInfo, BorderLayout.NORTH);
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
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UIManager.getColor("Panel.background"));
        orderInfo.add(buttonPanel, BorderLayout.SOUTH);
        JButton save = Elements.button("Submit Order");
        JPanel orderSummary = new JPanel(new BorderLayout());
        JPanel genSummary = orderSummary();
        orderSummary.add(genSummary, BorderLayout.CENTER);
        orderSummary.add(save, BorderLayout.SOUTH);
        add(orderSummary, BorderLayout.SOUTH);
        model.addTableModelListener(_ -> updateTotal());
    }

    private JPanel orderInfoPanel(){
        Form f = new Form();
        selectBillTo = new Copiable(po.getBillTo());
        selectShipTo = new Copiable(po.getShipTo());
        selectVendor = new Copiable(po.getVendor());
        orderId = new Copiable("OR" + (70000000 + (Engine.getOrders().size() + 1)));
        f.addInput(new Label("*Order ID", Constants.colors[0]), orderId);
        f.addInput(new Label("Supplier/Vendor", Constants.colors[1]), selectVendor);
        f.addInput(new Label("Bill To", Constants.colors[2]), selectBillTo);
        f.addInput(new Label("Ship To", Constants.colors[3]), selectShipTo);
        f.addInput(new Label("Trans. Type", Constants.colors[3]), new Copiable("/DCSS"));
        return f;
    }

    private JPanel moreOrderInfoPanel(){
        Form f = new Form();
        JTextField ordered = new Copiable(LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        HashMap<String, String> prs = new HashMap<>();
        for(PurchaseRequisition pr1 : Engine.realtime.getPurchaseRequisitions()){
            prs.put(pr1.getId(), pr1.getId());
        }
        poPurchaseReq = new Copiable(po.getPurchaseRequisition());
        expectedDelivery = new DatePicker();
        f.addInput(new Label("*Ordered", Constants.colors[4]), ordered);
        f.addInput(new Label("Purchase Requisition", Constants.colors[5]), poPurchaseReq);
        f.addInput(new Label("Expected Delivery", Constants.colors[6]), expectedDelivery);
        f.addInput(new Label("Due Date", Constants.colors[7]), new JTextField());
        f.addInput(new Label("Status", Constants.colors[8]), new Copiable("DRAFT"));
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
}