package org.Canal.UI.Views.Orders.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.DatePicker;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

/**
 * /ORDS/PR/NEW
 */
public class CreatePurchaseRequisition extends LockeState {

    private Copiable prOwnerField;
    private Selectable availableVendors;
    private Selectable availableBuyers;
    private DatePicker prStartDateField;
    private DatePicker prEndDateField;

    public CreatePurchaseRequisition(){
        super("Create Purchase Requisition", "/ORDS/PR/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreatePurchaseRequisition.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        availableVendors = Selectables.vendors();
        availableBuyers = Selectables.allLocations();
        JTextField prIdField = Elements.input("PR" + (10000000 + (Engine.orderProcessing.getPurchaseRequisitions().size() + 1)));
        JTextField prNumberField = Elements.input("PR" + (10000000 + (Engine.orderProcessing.getPurchaseRequisitions().size() + 1)));
        prOwnerField = new Copiable("U100001");
        JTextField prSpendAmount = Elements.input("500.00");
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
        JButton make = Elements.button("Commit Purchase Requisition");
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