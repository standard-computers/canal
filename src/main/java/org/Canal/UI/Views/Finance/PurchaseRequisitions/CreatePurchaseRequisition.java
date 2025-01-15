package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * /ORDS/PR/NEW
 */
public class CreatePurchaseRequisition extends LockeState {

    private Copiable ownerField;
    private JTextField descriptionField;
    private JTextField numberField;
    private JTextField idField;
    private JTextField maxSpendAmountField;
    private RTextScrollPane notes;
    private Selectable suppliers;
    private Selectable buyers;
    private JCheckBox isSingleOrder;
    private DatePicker startDateField;
    private DatePicker endDateField;

    public CreatePurchaseRequisition(){

        super("Create Purchase Requisition", "/ORDS/PR/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreatePurchaseRequisition.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(Elements.header("Create Purchase Requisition", SwingConstants.LEFT), BorderLayout.NORTH);
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        IconButton copyFrom = new IconButton("Copy From", "open", "Copy Purchase Requisition");
        IconButton review = new IconButton("Review", "review", "Review Purchase Requisition");
        IconButton execute = new IconButton("Execute", "execute", "Create Purchase Requisition");
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(copyFrom);
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(review);
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(execute);
        copyFrom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String prId = JOptionPane.showInputDialog(CreatePurchaseRequisition.this, "Enter Purchase Requisition ID");
                PurchaseRequisition pr = Engine.orders.getPurchaseRequisition(prId);
                if(pr != null){
                    suppliers.setSelectedValue(pr.getSupplier());
                    buyers.setSelectedValue(pr.getBuyer());
                    maxSpendAmountField.setText(String.valueOf(pr.getMaxSpend()));
                    isSingleOrder.setSelected(pr.isSingleOrder());
                    startDateField.setSelectedDate(new Date(pr.getStart()));
                    endDateField.setSelectedDate(new Date(pr.getEnd()));
                    notes.getTextArea().setText(pr.getNotes());
                }
            }
        });
        execute.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               int ccc = JOptionPane.showConfirmDialog(null, "Confirm Purchase Requisition creation?", "Confirm Execution", JOptionPane.YES_NO_OPTION);
               if(ccc == JOptionPane.YES_OPTION){
                   String prId = numberField.getText();
                   String prName = descriptionField.getText();
                   String prNumber = numberField.getText();
                   String prOwner = ownerField.getText();
                   String forVendor = suppliers.getSelectedValue();
                   String forBuyer = buyers.getSelectedValue();
                   double poAmount = Double.parseDouble(maxSpendAmountField.getText());
                   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   String prNotes = notes.getTextArea().getText();
                   PurchaseRequisition po = new PurchaseRequisition(prId, prName, prOwner, forVendor, forBuyer, prNumber, poAmount, dateFormat.format(startDateField.getSelectedDate()), dateFormat.format(endDateField.getSelectedDate()), prNotes);
                   Pipe.save("/ORDS/PR", po);
                   JOptionPane.showMessageDialog(CreatePurchaseRequisition.this, "Purchase Order Created");
                   dispose();
               }
           }
        });
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    public JPanel general(){

        suppliers = Selectables.vendors();
        buyers = Selectables.allLocations();
        idField = Elements.input("PR" + (10000000 + (Engine.orders.getPurchaseRequisitions().size() + 1)));
        ownerField = new Copiable("U100001");
        descriptionField = Elements.input("Purchase Orders to Vendor");
        numberField = Elements.input("PR" + (10000000 + (Engine.orders.getPurchaseRequisitions().size() + 1)));
        maxSpendAmountField = Elements.input("500.00");
        startDateField = new DatePicker();
        endDateField = new DatePicker();
        isSingleOrder = new JCheckBox();

        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New ID", UIManager.getColor("Label.foreground")), idField);
        f.addInput(Elements.coloredLabel("*Created", UIManager.getColor("Label.foreground")), new Copiable(Constants.now()));
        f.addInput(Elements.coloredLabel("*Creator (Owner)", UIManager.getColor("Label.foreground")), ownerField);
        f.addInput(Elements.coloredLabel("Description", Constants.colors[10]), descriptionField);
        f.addInput(Elements.coloredLabel("Purchase Req. #", Constants.colors[9]), numberField);
        f.addInput(Elements.coloredLabel("Supplier", Constants.colors[8]), suppliers);
        f.addInput(Elements.coloredLabel("Buyer", Constants.colors[7]), buyers);
        f.addInput(Elements.coloredLabel("Max Spend", Constants.colors[6]), maxSpendAmountField);
        f.addInput(Elements.coloredLabel("[or] Single Order", Constants.colors[5]), isSingleOrder);
        f.addInput(Elements.coloredLabel("Start Date", Constants.colors[4]), startDateField);
        f.addInput(Elements.coloredLabel("End Date", Constants.colors[3]), endDateField);
        isSingleOrder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(isSingleOrder.isSelected()){
                    maxSpendAmountField.setText("");
                    maxSpendAmountField.setEditable(false);
                }else{
                    maxSpendAmountField.setEditable(true);
                }
            }
        });
        return f;
    }

    private RTextScrollPane notes(){
        notes = Elements.simpleEditor();
        return notes;
    }
}