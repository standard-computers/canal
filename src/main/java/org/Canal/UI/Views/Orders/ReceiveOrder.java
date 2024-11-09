package org.Canal.UI.Views.Orders;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.CustomJTable;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * /ORDS/RCV
 */
public class ReceiveOrder extends JInternalFrame {

    private JTextField poField;
    private Selectable availRcvLocations, availablePutaway;

    public ReceiveOrder(String receivingLocation, DesktopState desktop){
        super("Receive an Order", false, true, false, true);
        Constants.checkLocke(this, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));
        setLayout(new BorderLayout());

        Form f = new Form();
        poField = new JTextField(12);
        f.addInput(new Label("Purchase Order #", Constants.colors[0]), poField);
        HashMap<String, String> putAwayOptions = new HashMap<>();
        for(Area a : Engine.getAreas(receivingLocation)){
            putAwayOptions.put(a.getId(), a.getId());
        }
        JPanel itemInput = new JPanel();
        String[][] data = new String[][]{};
        CustomJTable receivedItems = new CustomJTable(data, new String[]{"Item", "Exp. Qty.", "Rcvd. Qty."});
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
                PurchaseOrder foundPo = Engine.orderProcessing.getPurchaseOrders(poId);
                if(foundPo != null){
                    String[][] rcvData = new String[foundPo.getItems().size()][3];
                    ArrayList<OrderLineItem> items = foundPo.getItems();
                    expDelivery.setText(foundPo.getExpectedDelivery());
                    for (int i = 0; i < items.size(); i++) {
                        OrderLineItem oli = items.get(i);
                        System.out.println(oli.toString());
                        rcvData[i][0] = oli.getName();
                        rcvData[i][1] = String.valueOf(oli.getQuantity());
                        rcvData[i][2] = String.valueOf(oli.getQuantity());
                    }
                    receivedItems.setRowData(rcvData);
                }
            }
        });
        availRcvLocations = Selectables.allLocations();
        availRcvLocations.editable();
        availRcvLocations.setSelectedValue(receivingLocation);
        availablePutaway = new Selectable(putAwayOptions);
        f.addInput(new Label("Receiving Location", Constants.colors[1]), availRcvLocations);
        f.addInput(new Label("Putaway Area", Constants.colors[2]), availablePutaway);
        f.addInput(new Label("Expected Delivery", Constants.colors[3]), expDelivery);
        f.addInput(new Label("Delivery Date", Constants.colors[4]), availRcvLocations);
        add(f, BorderLayout.NORTH);

        Button receive = new Button("Receive");
        JPanel receivePanel = new JPanel();
        JPanel g = new JPanel();
        JCheckBox confirmAccuracy = new JCheckBox("Confirm Accuracy");
        g.add(confirmAccuracy);
        g.add(receive);
        receivePanel.add(g, BorderLayout.SOUTH);

        add(receivePanel, BorderLayout.SOUTH);
        receive.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String po = poField.getText();
                String rlid = availRcvLocations.getSelectedValue();
                if(po.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Purchase Order Number Required!", "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    PurchaseOrder spo = Engine.orderProcessing.getPurchaseOrders(po);
                    if(spo == null){
                        JOptionPane.showMessageDialog(null, "PO or Order Number required!", "Error", JOptionPane.ERROR_MESSAGE);
                        if(!rlid.equals(spo.getVendor())){
                            int override = JOptionPane.showConfirmDialog(null, "The receiving location and buyer do not match. Override?");
                            if(override == JOptionPane.NO_OPTION){

                            }else{

                            }
                        }
                    }
                }
            }
        });
    }

    public void setPO(String po){
        poField.setText(po);
    }
}