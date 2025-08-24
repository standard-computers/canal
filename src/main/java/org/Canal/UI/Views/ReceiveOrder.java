package org.Canal.UI.Views;

import org.Canal.Models.BusinessUnits.GoodsReceipt;
import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.DatePicker;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * /ORDS/RCV
 */
public class ReceiveOrder extends LockeState {

    private String location; //Location actually receiving
    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JTextField poField;
    private JTextField receivingLocation;
    private Selectable availablePutawayBin;
    private JCheckBox explodePackage;
    private Selectable statuses;
    private HashMap<String, String> putawayBinOptions;
    private CustomTable receivedItems;

    public ReceiveOrder(String location, DesktopState desktop, RefreshListener refreshListener) {

        super("Receive Order", "/ORDS/RCV", false, true, false, true);
        Constants.checkLocke(this, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/label.png")));
        setLayout(new BorderLayout());
        this.location = location;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabbedPane = new CustomTabbedPane();
        tabbedPane.addTab("Items", items());
        tabbedPane.addTab("Methods", methods());

        Form f = new Form();
        poField = Elements.input(12);
        f.addInput(Elements.coloredLabel("Purchase Order #", Constants.colors[0]), poField);
        HashMap<String, String> putAwayOptions = new HashMap<>();
        for (Area a : Engine.getAreas(location)) {
            putAwayOptions.put(a.getId(), a.getId());
        }
        if (putAwayOptions.isEmpty()) {
            dispose();
            JOptionPane.showMessageDialog(null, "Must establish an Area here!");
        }
        Copiable expDelivery = new Copiable("");

        add(tabbedPane, BorderLayout.CENTER);

        poField.getDocument().addDocumentListener(new DocumentListener() {
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
                String poId = poField.getText();
                PurchaseOrder foundPo = Engine.getPurchaseOrder(poId);
                if (foundPo != null) {
                    ArrayList<Object[]> its = new ArrayList<>();
                    ArrayList<OrderLineItem> items = foundPo.getItems();
                    expDelivery.setText(foundPo.getExpectedDelivery());
                    for (int i = 0; i < items.size(); i++) {
                        OrderLineItem oli = items.get(i);
                        its.add(new String[]{
                                oli.getId(),
                                oli.getName(),
                                String.valueOf(oli.getQuantity()),
                                String.valueOf(oli.getQuantity())
                        });
                    }
                    receivedItems.setRowData(its);
                }
            }
        });

        this.receivingLocation = Elements.input();
        this.receivingLocation.setText(location);
        putawayBinOptions = new HashMap<>();
        ArrayList<Area> as = (ArrayList<Area>) Engine.getAreas(location);
        for (Area a : as) {
            for (Bin b : a.getBins()) {
                putawayBinOptions.put(b.getId(), b.getId());
            }
        }
        availablePutawayBin = new Selectable(putawayBinOptions);
        availablePutawayBin.editable();

        f.addInput(Elements.coloredLabel("Receiving Location", Constants.colors[1]), this.receivingLocation);
        f.addInput(Elements.coloredLabel("Putaway Bin", Constants.colors[2]), availablePutawayBin);
        f.addInput(Elements.coloredLabel("Expected Delivery", Constants.colors[3]), expDelivery);

        String timestampFormat = "yyyy-MM-dd HH:mm:ss";
        DatePicker deliveryDate = new DatePicker();
        SimpleDateFormat sdf = new SimpleDateFormat(timestampFormat);

        try {
            Date currentDate = sdf.parse(Constants.now());
            deliveryDate.setSelectedDate(currentDate);
            f.addInput(Elements.coloredLabel("Delivery Date", UIManager.getColor("Label.foreground")), deliveryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JPanel heading = new JPanel(new BorderLayout());
        heading.add(Elements.header("Receive Purchase Order", SwingConstants.LEFT), BorderLayout.NORTH);
        heading.add(f, BorderLayout.CENTER);
        add(heading, BorderLayout.NORTH);

        JButton receive = Elements.button("Receive");
        JPanel receivePanel = new JPanel();
        JLabel userId = new JLabel(Engine.getEmployee(Engine.getAssignedUser().getEmployee()).getName() + "/" + Engine.getAssignedUser().getId());
        userId.setBackground(Color.lightGray);
        JPanel g = new JPanel();
        JCheckBox confirmAccuracy = new JCheckBox("Confirm Accuracy");
        g.add(confirmAccuracy);
        g.add(userId);
        g.add(receive);
        receivePanel.add(g, BorderLayout.SOUTH);

        add(receivePanel, BorderLayout.SOUTH);

        receive.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                String providedPurchaseOrder = poField.getText();

                if (confirmAccuracy.isSelected()) {
                    if (providedPurchaseOrder.isEmpty()) {

                        JOptionPane.showMessageDialog(null, "Purchase Order Number Required!", "Error", JOptionPane.ERROR_MESSAGE);

                    } else {

                        PurchaseOrder foundPurchaseOrder = Engine.getPurchaseOrder(providedPurchaseOrder);
                        if (foundPurchaseOrder != null) {
                            if ((boolean) Engine.codex.getValue("ORDS/RCV", "block_locations")) {
                                if (!foundPurchaseOrder.getShipTo().equals(location)) {

                                    if ((boolean) Engine.codex.getValue("ORDS/RCV", "allow_loc_bloc_override")) {

                                        int override = JOptionPane.showConfirmDialog(null, "The receiving location and ship to do not match. Override?");
                                        if (override == JOptionPane.NO_OPTION) {
                                            JOptionPane.showMessageDialog(null, "PO Receive canceled!", "Error", JOptionPane.ERROR_MESSAGE);
                                            dispose();
                                        }
                                    } else { //Location block override 'No'

                                        JOptionPane.showMessageDialog(null, "PO Receive canceled!", "Ship To location different than receiving location", JOptionPane.ERROR_MESSAGE);
                                        dispose();
                                    }
                                }
                            }

                            GoodsReceipt goodsReceipt = new GoodsReceipt();
                            goodsReceipt.setId(Constants.generateId(10));
                            goodsReceipt.setPurchaseOrder(providedPurchaseOrder);
                            goodsReceipt.setReceived(deliveryDate.getSelectedDateString());
                            goodsReceipt.setReceiver(Engine.getAssignedUser().getId());
                            goodsReceipt.setLocation(location);
                            goodsReceipt.setStatus(LockeStatus.DELIVERED);
                            ArrayList<OrderLineItem> lineitems = new ArrayList<>();

                            String selectedBinId = availablePutawayBin.getSelectedValue();

                            Bin selectedBin = Engine.getBin(selectedBinId);

                            if (selectedBin != null) {

                                if (selectedBin.isPutaway()) {

                                    Inventory i = Engine.getInventory(foundPurchaseOrder.getShipTo()); //TODO Double check

                                    for (int row = 0; row < receivedItems.getRowCount(); row++) {
                                        String itemId = receivedItems.getValueAt(row, 1).toString();
                                        String itemName = receivedItems.getValueAt(row, 2).toString();
                                        double itemQty = Double.parseDouble(receivedItems.getValueAt(row, 3).toString());
                                        double itemRcvd = Double.parseDouble(receivedItems.getValueAt(row, 4).toString());
                                        //double itemPrice = Double.parseDouble(receivedItems.getValueAt(row, 3).toString());
                                        //double itemTotal = Double.parseDouble(receivedItems.getValueAt(row, 4).toString());
                                        lineitems.add(new OrderLineItem(itemId, itemName, itemQty, itemRcvd));
                                        i.addStock(new StockLine("/ITS", itemId, "$SKU", itemQty, "UNKNOWN", selectedBinId));
                                        //TODO Request SKU
                                    }

                                    goodsReceipt.setItems(lineitems);
                                    Pipe.save("/GR", goodsReceipt);

                                    foundPurchaseOrder.setStatus(LockeStatus.DELIVERED);
                                    foundPurchaseOrder.save();

                                    //Mark associated delivery, if any, as DELIVERED
                                    for (Delivery d : Engine.getInboundDeliveries(foundPurchaseOrder.getShipTo())) {
                                        if (d.getPurchaseOrder().equals(providedPurchaseOrder)) {
                                            d.setStatus(LockeStatus.DELIVERED);
                                            d.save();
                                        }
                                    }

                                    i.save();
                                    dispose();
                                    JOptionPane.showMessageDialog(null, "Goods Receipt created and stock put away.");

                                } else {

                                    JOptionPane.showMessageDialog(null, "Selected bin does not allow put away.");
                                }
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "PO Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Must confirm accuracy");
                }
            }
        });
    }

    private JScrollPane items(){
        ArrayList<Object[]> data = new ArrayList<>();
        receivedItems = new CustomTable(new String[]{
                "Item Id",
                "Item",
                "Exp. Qty.",
                "Rcvd. Qty.",
        }, data);
        return new JScrollPane(receivedItems);
    }

    private JPanel methods(){

        JPanel methods = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();

        explodePackage = new JCheckBox("1 HU / EA BASE UOM");
        statuses = Selectables.statusTypes();

        f.addInput(Elements.coloredLabel("Explode Package", UIManager.getColor("Panel.background")), explodePackage);
        f.addInput(Elements.coloredLabel("Stock Status", UIManager.getColor("Panel.background")), statuses);

        methods.add(f);

        return methods;
    }

    public void setPO(String po) {
        poField.setText(po);
    }
}