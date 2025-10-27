package org.Canal.UI.Views;

import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.Order;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /TRANS/ODO/FF
 */
public class FulfillOrder extends LockeState {

    private String location;
    private JTextField odoId;
    private JTextField poIdField;
    private JTextField soIdField;
    private JCheckBox assignTasks;
    private JCheckBox createGoodsIssue;
    private JCheckBox individualizeGoodsIssue;
    private JCheckBox createConfirmTasks;
    private CustomTable fulfillItems;
    private Order order;

    public FulfillOrder(String location) {

        super("Fulfill Order", "/TRANS/ODO/FF", false, true, false, true);
        setFrameIcon(new ImageIcon(FulfillOrder.class.getResource("/icons/fulfill.png")));
        this.location = location;

        JPanel topInfo = new JPanel(new BorderLayout());
        topInfo.add(Elements.header("Fulfill Order", SwingConstants.LEFT), BorderLayout.NORTH);
        topInfo.add(orderInfo(), BorderLayout.CENTER);
        topInfo.add(optionsPanel(), BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topInfo, BorderLayout.NORTH);

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.add("Stock Pick", stockPickInfo());
        tabs.add("Good Issue", goodsIssueInfo());
        tabs.add("Delivery", deliveryInfo());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel orderInfo() {

        odoId = Elements.input(15);
        poIdField = Elements.input();
        soIdField = Elements.input();
        assignTasks = new JCheckBox();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Outbound Delivery ID"), odoId);
        form.addInput(Elements.inputLabel("[or] Purchase Order"), poIdField);
        form.addInput(Elements.inputLabel("[or] Sales Order"), soIdField);
        form.addInput(Elements.inputLabel("Assign Tasks"), assignTasks);
        poIdField.getDocument().addDocumentListener(new DocumentListener() {
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
                String poId = poIdField.getText();
                Order foundPo = Engine.getPurchaseOrder(poId);
                if (foundPo != null) {
                    order = foundPo;
                    ArrayList<Object[]> its = new ArrayList<>();
                    ArrayList<OrderLineItem> items = foundPo.getItems();
                    for (int i = 0; i < items.size(); i++) {
                        OrderLineItem oli = items.get(i);
                        its.add(new Object[]{
                                i,
                                oli.getId(),
                                oli.getName(),
                                String.valueOf(oli.getQuantity()),
                                String.valueOf(oli.getQuantity()),
                                "", "", ""
                        });
                    }
                    fulfillItems.setRowData(its);
                }
            }
        });
        soIdField.getDocument().addDocumentListener(new DocumentListener() {
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
                String poId = soIdField.getText();
                Order foundSalesOrder = Engine.getSalesOrder(poId);
                if (foundSalesOrder != null) {
                    ArrayList<Object[]> its = new ArrayList<>();
                    ArrayList<OrderLineItem> items = foundSalesOrder.getItems();
                    for (int i = 0; i < items.size(); i++) {
                        OrderLineItem oli = items.get(i);
                        its.add(new Object[]{i, oli.getId(), oli.getName(), String.valueOf(oli.getQuantity()), String.valueOf(oli.getQuantity()), "", "", ""});
                    }
                    fulfillItems.setRowData(its);
                }
            }
        });
        return form;
    }

    private JPanel optionsPanel() {
        JPanel buttons = new JPanel();
        IconButton stockCheck = new IconButton("Stock Check", "atp", "Check for missing product difference");
        IconButton review = new IconButton("Review", "review", "Review for warnings or potential errors");
        IconButton adjust = new IconButton("Adjust", "adjust", "Auto create picks");
        IconButton executeFulfillment = new IconButton("Execute", "start", "Execute Fulfillment");
        buttons.add(stockCheck);
        buttons.add(review);
        buttons.add(adjust);
        buttons.add(executeFulfillment);
        stockCheck.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Inventory i = Engine.getInventory(location);
                int linesInStock = 0;
                for (OrderLineItem oi : order.getItems()) {
                    if (i.inStock(oi.getId()) && i.getStock(oi.getId()) >= oi.getQuantity()) {
                        linesInStock += 1;
                    }
                }
                if (linesInStock == order.getItems().size()) {
                    JOptionPane.showMessageDialog(null, "All products are in stock!");
                } else {
                    JOptionPane.showMessageDialog(null, (order.getItems().size() - linesInStock) + " Products NOT in stock!");
                }
            }
        });
        executeFulfillment.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Delivery td = Engine.getOutboundDeliveries(location).stream().filter(d -> d.getId().equals(odoId.getText())).findFirst().orElse(null);
                if (td == null) {
                    JOptionPane.showMessageDialog(null, "Outbound Delivery Not Found");
                } else {

                    //Prepare for product consumptions

                    if (assignTasks.isSelected()) {

                    } else {
                        int ccc = JOptionPane.showConfirmDialog(null, "Confirm fulfillment?", "This cannot be auto undone.", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (ccc == JOptionPane.YES_OPTION) {

                            Inventory i = Engine.getInventory(location);
                            for (OrderLineItem oi : order.getItems()) {
                                i.goodsIssue(i.getStockLine(oi.getId()), oi.getQuantity());
                            }

                            String odId = odoId.getText();
                            String poNum = poIdField.getText();
                            String soNum = soIdField.getText();
                            if (odId != null && poNum != null) {
                                Delivery od = Engine.getOutboundDelivery(odId);
                                ArrayList<Delivery> ids = Engine.getInboundDeliveries(order.getShipTo());
                                for (Delivery d : ids) {
                                    if (d.getPurchaseOrder().equals(order.getOrderId())) {
                                        d.setStatus(LockeStatus.FULFILLED);
                                        d.save();
                                    }
                                }
                                order = Engine.getPurchaseOrder(poNum);
                                Order so = Engine.getSalesOrder(soNum);
                                if (od != null) {
                                    od.setStatus(LockeStatus.FULFILLED);
                                    od.save();
                                }
                                if (order != null) {
                                    order.setStatus(LockeStatus.FULFILLED);
                                    order.save();
                                }
                                if (so != null) {
                                    so.setStatus(LockeStatus.FULFILLED);
                                    so.save();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Must provide a valid OBD or PO!");
                            }
                            dispose();
                            JOptionPane.showMessageDialog(null, "Fulfillment completed!");
                        }
                    }
                }
            }
        });
        return buttons;
    }

    private JPanel goodsIssueInfo() {

        JPanel goodsIssue = new JPanel(new FlowLayout(FlowLayout.LEFT));

        createGoodsIssue = new JCheckBox("If no tasks, create GI");
        individualizeGoodsIssue = new JCheckBox("Create GI for each stock pick also");
        createConfirmTasks = new JCheckBox("Create/confirm associated tasks in background?");

        Form form = new Form();
        form.addInput(Elements.inputLabel("Note"), new JLabel("GI Material Movements still created"));
        form.addInput(Elements.inputLabel("Create Goods Issue"), createGoodsIssue);
        form.addInput(Elements.inputLabel("Individualize GIs"), individualizeGoodsIssue);
        form.addInput(Elements.inputLabel("Create & Cofirm Tasks"), createConfirmTasks);
        goodsIssue.add(form);

        return goodsIssue;
    }

    private JPanel stockPickInfo() {

        JPanel panel = new JPanel(new BorderLayout());
        ArrayList<Object[]> data = new ArrayList<>();
        fulfillItems = new CustomTable(new String[]{"Pick", "Item Id", "Item", "Exp Qty", "Src Qty", "Src HU", "Src Avl Qty", "Src Bin"}, data);
        JScrollPane sp = new JScrollPane(fulfillItems);
        sp.setPreferredSize(new Dimension(400, 400));
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    private JPanel deliveryInfo() {

        JPanel panel = new JPanel(new BorderLayout());
        ArrayList<Object[]> data = new ArrayList<>();
        CustomTable palletize = new CustomTable(new String[]{"Pallet", "Item Id", "Item", "Qty", "Src HU"}, data);
        JScrollPane sp = new JScrollPane(palletize);
        sp.setPreferredSize(new Dimension(400, 400));
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    public void setSalesOrder(String salesOrderId) {

        //TODO Fix SO/PO Reference in this Locke
        poIdField.setText(salesOrderId);
        repaint();
        revalidate();
    }
}