package org.Canal.UI.Views.Orders.PurchaseRequisitions;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Warehouse;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Inputs.DatePicker;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * /ORDS/PR/NEW
 */
public class CreatePurchaseRequisition extends JInternalFrame {

    private Copiable prOwnerField;
    private Selectable availableVendors, availableBuyers;
    private DatePicker prStartDateField, prEndDateField;

    public CreatePurchaseRequisition(){
        super("Create Purchase Requisition", false, true, false, true);
        setFrameIcon(new ImageIcon(CreatePurchaseRequisition.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        HashMap<String, String> opts = new HashMap<>();
        for(Location cs : Engine.getCustomers()){
            opts.put(cs.getId() + " – " + cs.getName(), cs.getId());
        }
        for(Location vs : Engine.getVendors()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        for(Location vs : Engine.getDistributionCenters()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        for(Location vs : Engine.getCostCenters()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        for(Warehouse vs : Engine.getWarehouses()){
            opts.put(vs.getId() + " – " + vs.getName(), vs.getId());
        }
        availableVendors = new Selectable(opts);
        availableBuyers = new Selectable(opts);
        JTextField prIdField = new JTextField("PR" + (10000000 + (Engine.realtime.getPurchaseRequisitions().size() + 1)));
        JTextField prNumberField = new JTextField("PR" + (10000000 + (Engine.realtime.getPurchaseRequisitions().size() + 1)));
        prOwnerField = new Copiable("U100001");
        JTextField prSpendAmount = new JTextField("500.00");
        prStartDateField = new DatePicker();
        prEndDateField = new DatePicker();
        JTextArea prNotesField = new JTextArea();
        JCheckBox singleOrder = new JCheckBox();
        Form f = new Form();
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), prIdField);
        f.addInput(new Label("*Created", UIManager.getColor("Label.foreground")), new Copiable(Constants.now()));
        f.addInput(new Label("*Creator (Owner)", UIManager.getColor("Label.foreground")), prOwnerField);
        f.addInput(new Label("Purchase Req. #", UIManager.getColor("Label.foreground")), prNumberField);
        f.addInput(new Label("Supplier ID", Constants.colors[9]), availableVendors);
        f.addInput(new Label("Customer (Buyer/Ship To)", Constants.colors[8]), availableBuyers);
        f.addInput(new Label("Max Spend", Constants.colors[7]), prSpendAmount);
        f.addInput(new Label("[or] Single Order", Constants.colors[6]), singleOrder);
        f.addInput(new Label("Valid From", Constants.colors[5]), prStartDateField);
        f.addInput(new Label("To", Constants.colors[4]), prEndDateField);
        f.addInput(new Label("Notes", Constants.colors[3]), prNotesField);
        singleOrder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(singleOrder.isSelected()){
                    prSpendAmount.setText("");
                    prSpendAmount.setEditable(false);
                }else{
                    prSpendAmount.setEditable(true);
                }
            }
        });
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button make = new Button("Commit Purchase Requisition");
        getRootPane().setDefaultButton(make);
        add(make, BorderLayout.SOUTH);
        make.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String prId = prNumberField.getText();
                String prName = prNumberField.getText();
                String prNumber = prNumberField.getText();
                String prOwner = prOwnerField.getText();
                String forVendor = availableVendors.getSelectedValue();
                String forBuyer = availableBuyers.getSelectedValue();
                double poAmount = Double.parseDouble(prSpendAmount.getText());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String prNotes = prNotesField.getText().trim();
                PurchaseRequisition po = new PurchaseRequisition(prId, prName, prOwner, forVendor, forBuyer, prNumber, poAmount, dateFormat.format(prStartDateField.getSelectedDate()), dateFormat.format(prEndDateField.getSelectedDate()), prNotes);
                Pipe.save("/PR", po);
                JOptionPane.showMessageDialog(CreatePurchaseRequisition.this, "Purchase Order Created");
                dispose();
            }
        });
    }
}