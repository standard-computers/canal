package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.UI.Elements.*;
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
        f.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), prIdField);
        f.addInput(Elements.coloredLabel("*Created", UIManager.getColor("Label.foreground")), new Copiable(Constants.now()));
        f.addInput(Elements.coloredLabel("*Creator (Owner)", UIManager.getColor("Label.foreground")), prOwnerField);
        f.addInput(Elements.coloredLabel("Purchase Req. #", UIManager.getColor("Label.foreground")), prNumberField);
        f.addInput(Elements.coloredLabel("Supplier ID", Constants.colors[9]), availableVendors);
        f.addInput(Elements.coloredLabel("Customer (Buyer/Ship To)", Constants.colors[8]), availableBuyers);
        f.addInput(Elements.coloredLabel("Max Spend", Constants.colors[7]), prSpendAmount);
        f.addInput(Elements.coloredLabel("[or] Single Order", Constants.colors[6]), singleOrder);
        f.addInput(Elements.coloredLabel("Valid From", Constants.colors[5]), prStartDateField);
        f.addInput(Elements.coloredLabel("To", Constants.colors[4]), prEndDateField);
        f.addInput(Elements.coloredLabel("Notes", Constants.colors[3]), prNotesField);
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