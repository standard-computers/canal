package org.Canal.UI.Views;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.BusinessUnits.SalesOrder;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

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
    private JTextField odoId, poNumber, soNumber;
    private JCheckBox assignTasks, createGoodsIssue, individualizeGoodsIssue;
    private CustomTable fulfillItems;

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
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Stock Pick", stockPickInfo());
        tabs.add("Good Issue", goodsIssueInfo());
        add(tabs, BorderLayout.CENTER);
    }

    public JPanel orderInfo(){
        Form f = new Form();
        odoId = Elements.input(8);
        poNumber = Elements.input(8);
        soNumber = Elements.input(8);
        assignTasks = new JCheckBox();
        f.addInput(new Label("Outbound Delivery ID", Constants.colors[10]), odoId);
        f.addInput(new Label("[or] Purchase Order", Constants.colors[9]), poNumber);
        f.addInput(new Label("[or] Sales Order", Constants.colors[8]), soNumber);
        f.addInput(new Label("Assign Tasks", Constants.colors[7]), assignTasks);
        poNumber.getDocument().addDocumentListener(new DocumentListener() {
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
                String poId = poNumber.getText();
                PurchaseOrder foundPo = Engine.orderProcessing.getPurchaseOrder(poId);
                if(foundPo != null){
                    ArrayList<Object[]> its = new ArrayList<>();
                    ArrayList<OrderLineItem> items = foundPo.getItems();
                    for (int i = 0; i < items.size(); i++) {
                        OrderLineItem oli = items.get(i);
                        its.add(new String[]{
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
        soNumber.getDocument().addDocumentListener(new DocumentListener() {
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
                String poId = soNumber.getText();
                SalesOrder foundPo = Engine.orderProcessing.getSalesOrder(poId);
                if(foundPo != null){
                    ArrayList<Object[]> its = new ArrayList<>();
                    ArrayList<OrderLineItem> items = foundPo.getItems();
                    for (int i = 0; i < items.size(); i++) {
                        OrderLineItem oli = items.get(i);
                        its.add(new String[]{
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
        return f;
    }

    public JPanel optionsPanel(){
        JPanel buttons = new JPanel();
        IconButton stockCheck = new IconButton("Stock Check", "atp", "Check for missing product difference");
        IconButton review = new IconButton("Review", "review", "Review for warnings or potential errors");
        IconButton adjust = new IconButton("Adjust", "adjust", "Auto create picks");
        IconButton executeFulfillment = new IconButton("Execute", "start", "Execute Fulfillment");
        buttons.add(stockCheck);
        buttons.add(review);
        buttons.add(adjust);
        buttons.add(executeFulfillment);
        executeFulfillment.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Delivery td = Engine.getOutboundDeliveries(location).stream().filter(d -> d.getId().equals(odoId.getText())).findFirst().orElse(null);
                if(td == null) {
                    JOptionPane.showMessageDialog(null, "Outbound Delivery Not Found");
                }else{
                    if(assignTasks.isSelected()) {

                    }else{
                        String odId = odoId.getText();
                        String poNum = poNumber.getText();
                        if(odId != null && poNum != null) {
                            Delivery od = Engine.getOutboundDelivery(odId);
                            PurchaseOrder po = Engine.orderProcessing.getPurchaseOrder(poNum);
                            if(od != null){

                            }
                            if(po != null){

                            }
                            if(od == null && po == null){
                                JOptionPane.showMessageDialog(null, "PO and OBD Not Found");
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "Must provide OBD or PO!");
                        }
                    }
                }
            }
        });
        return buttons;
    }

    public JPanel goodsIssueInfo(){
        Form f = new Form();
        createGoodsIssue = new JCheckBox("If no tasks, create GI");
        individualizeGoodsIssue = new JCheckBox("Create GI for each stock pick also");
        f.addInput(new Label("Note", UIManager.getColor("Label.foreground")), new JLabel("GI Material Movements still created"));
        f.addInput(new Label("Create Goods Issue", Constants.colors[10]), createGoodsIssue);
        f.addInput(new Label("Individualize GIs", Constants.colors[9]), individualizeGoodsIssue);
        return f;
    }

    public JPanel stockPickInfo(){
        JPanel panel = new JPanel(new BorderLayout());
        ArrayList<Object[]> data = new ArrayList<>();
        fulfillItems = new CustomTable(new String[]{"Item Id", "Item", "Exp Qty", "Src Qty", "Src HU", "Src Avl Qty", "Src Bin"}, data);
        JScrollPane sp = new JScrollPane(fulfillItems);
        sp.setPreferredSize(new Dimension(600, 300));
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }
}