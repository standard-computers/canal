package org.Canal.UI.Views.Orders;

import org.Canal.Models.BusinessUnits.GoodsReceipt;
import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Bin;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Inputs.DatePicker;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Inputs.Selectable;
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
public class ReceiveOrder extends JInternalFrame {

    private JTextField poField;
    private Selectable availRcvLocations, availablePutawayBin;
    private HashMap<String, String> putawayBinOptions;
    private CustomJTable receivedItems;

    public ReceiveOrder(String receivingLocation, DesktopState desktop){
        super("Receive Order", false, true, false, true);
        Constants.checkLocke(this, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/label.png")));
        setLayout(new BorderLayout());

        Form f = new Form();
        poField = Elements.input(12);
        f.addInput(new Label("Purchase Order #", Constants.colors[0]), poField);
        HashMap<String, String> putAwayOptions = new HashMap<>();
        for(Area a : Engine.getAreas(receivingLocation)){
            putAwayOptions.put(a.getId(), a.getId());
        }
        if(putAwayOptions.isEmpty()){
            dispose();
            JOptionPane.showMessageDialog(null, "Must establish an Area here!");
        }
        JPanel itemInput = new JPanel();
        String[][] data = new String[][]{};
        receivedItems = new CustomJTable(data, new String[]{"Item Id", "Item", "Exp. Qty.", "Rcvd. Qty."});
        JScrollPane p = new JScrollPane(receivedItems);
        Copiable expDelivery = new Copiable("");
        itemInput.add(p);
        add(itemInput, BorderLayout.CENTER);
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
                PurchaseOrder foundPo = Engine.orderProcessing.getPurchaseOrder(poId);
                if(foundPo != null){
                    String[][] rcvData = new String[foundPo.getItems().size()][4];
                    ArrayList<OrderLineItem> items = foundPo.getItems();
                    expDelivery.setText(foundPo.getExpectedDelivery());
                    for (int i = 0; i < items.size(); i++) {
                        OrderLineItem oli = items.get(i);
                        rcvData[i][0] = oli.getId();
                        rcvData[i][1] = oli.getName();
                        rcvData[i][2] = String.valueOf(oli.getQuantity());
                        rcvData[i][3] = String.valueOf(oli.getQuantity());
                    }
                    receivedItems.setRowData(rcvData);
                }
            }
        });
        availRcvLocations = Selectables.allLocations();
        availRcvLocations.editable();
        availRcvLocations.setSelectedValue(receivingLocation);
        putawayBinOptions = new HashMap<>();
        ArrayList<Area> as = (ArrayList<Area>) Engine.getAreas(receivingLocation);
        for(Area a : as){
            for(Bin b : a.getBins()){
                putawayBinOptions.put(b.getId(), b.getId());
            }
        }
        availablePutawayBin = new Selectable(putawayBinOptions);
        f.addInput(new Label("Receiving Location", Constants.colors[1]), availRcvLocations);
        f.addInput(new Label("Putaway Bin", Constants.colors[2]), availablePutawayBin);
        f.addInput(new Label("Expected Delivery", Constants.colors[3]), expDelivery);
        String timestampFormat = "yyyy-MM-dd HH:mm:ss";
        DatePicker deliveryDate = new DatePicker();
        SimpleDateFormat sdf = new SimpleDateFormat(timestampFormat);
        try {
            Date currentDate = sdf.parse(Constants.now());
            deliveryDate.setSelectedDate(currentDate);
            f.addInput(new Label("Delivery Date", UIManager.getColor("Label.foreground")), deliveryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JPanel heading = new JPanel(new BorderLayout());
        heading.add(Elements.header("Receive Purchase Order", SwingConstants.LEFT), BorderLayout.NORTH);
        heading.add(f, BorderLayout.CENTER);
        add(heading, BorderLayout.NORTH);

        Button receive = new Button("Receive");
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
                String po = poField.getText();
                String receivingLocationId = availRcvLocations.getSelectedValue();
                if(confirmAccuracy.isSelected()){
                    if(po.isEmpty()){
                        JOptionPane.showMessageDialog(null, "Purchase Order Number Required!", "Error", JOptionPane.ERROR_MESSAGE);
                    }else{
                        PurchaseOrder spo = Engine.orderProcessing.getPurchaseOrder(po);
                        if(spo != null){
                            if(!receivingLocationId.equals(spo.getShipTo())){
                                int override = JOptionPane.showConfirmDialog(null, "The receiving location and ship to do not match. Override?");
                                if(override == JOptionPane.NO_OPTION){
                                    JOptionPane.showMessageDialog(null, "PO Receive canceled!", "Error", JOptionPane.ERROR_MESSAGE);
                                    dispose();
                                }
                            }
                            GoodsReceipt gr = new GoodsReceipt();
                            gr.setId(Constants.generateId(10));
                            gr.setPurchaseOrder(po);
                            gr.setReceived(deliveryDate.getSelectedDateString());
                            gr.setReceiver(Engine.getAssignedUser().getId());
                            gr.setLocation(receivingLocationId);
                            gr.setStatus(LockeStatus.DELIVERED);
                            ArrayList<OrderLineItem> lineitems = new ArrayList<>();

                            Inventory i = Engine.getInventory(spo.getShipTo()); //TODO Double check
                            for (int row = 0; row < receivedItems.getRowCount(); row++) {
                                String itemId = receivedItems.getValueAt(row, 0).toString();
                                String itemName = receivedItems.getValueAt(row, 1).toString();
                                double itemQty = Double.parseDouble(receivedItems.getValueAt(row, 2).toString());
                                double itemRcvd = Double.parseDouble(receivedItems.getValueAt(row, 3).toString());
    //                                double itemPrice = Double.parseDouble(receivedItems.getValueAt(row, 3).toString());
    //                                double itemTotal = Double.parseDouble(receivedItems.getValueAt(row, 4).toString());
                                lineitems.add(new OrderLineItem(itemId, itemName, itemQty, itemRcvd));
                                i.addStock(new StockLine("/ITS", itemId, itemQty, "UNKNOWN", availablePutawayBin.getSelectedValue()));
                            }
                            gr.setItems(lineitems);
                            Pipe.save("/GR", gr);
                            spo.setStatus(LockeStatus.DELIVERED);
                            spo.save();
                            i.save();
                            dispose();
                            JOptionPane.showMessageDialog(null, "Goods Receipt created and stock put away.");
                        }else{
                            JOptionPane.showMessageDialog(null, "PO Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Must confirm accuracy");
                }
            }
        });
    }

    public void setPO(String po){
        poField.setText(po);
    }
}