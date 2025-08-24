package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * /ORDS/PR/NEW
 */
public class CreatePurchaseRequisition extends LockeState {

    private ItemTableModel model;
    private JLabel netAmount;
    private JTextField descriptionField;
    private JTextField maxSpendAmountField;
    private RTextScrollPane notes;
    private JTextField suppliers;
    private JTextField buyers;
    private JCheckBox isSingleOrder;
    private DatePicker startDateField;
    private DatePicker endDateField;

    public CreatePurchaseRequisition() {

        super("Create Purchase Requisition", "/ORDS/PR/NEW");
        setFrameIcon(new ImageIcon(CreatePurchaseRequisition.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Items", items());
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        model.addTableModelListener(_ -> updateTotal());
    }

    private JPanel toolbar() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(Elements.header("Create Purchase Requisition", SwingConstants.LEFT), BorderLayout.NORTH);
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy Purchase Requisition");
        copyFrom.addActionListener(_ -> {
            String prId = JOptionPane.showInputDialog(CreatePurchaseRequisition.this, "Enter Purchase Requisition ID");
            PurchaseRequisition pr = Engine.getPurchaseRequisition(prId);
            if (pr != null) {
                maxSpendAmountField.setText(String.valueOf(pr.getMaxSpend()));
                isSingleOrder.setSelected(pr.isSingleOrder());
                startDateField.setSelectedDate(new Date(pr.getStart()));
                endDateField.setSelectedDate(new Date(pr.getEnd()));
                notes.getTextArea().setText(pr.getNotes());
            }
        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(copyFrom);

        IconButton review = new IconButton("Review", "review", "Review Purchase Requisition");
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(review);

        IconButton execute = new IconButton("Execute", "execute", "Create Purchase Requisition");
        execute.addActionListener(_ -> {
            int ccc = JOptionPane.showConfirmDialog(null, "Confirm Purchase Requisition creation?", "Confirm Execution", JOptionPane.YES_NO_OPTION);
            if (ccc == JOptionPane.YES_OPTION) {
                String preqId = "PR" + (100000 + (Engine.getPurchaseRequisitions().size() + 1));
                String prName = descriptionField.getText();
                String forVendor = suppliers.getText();
                String forBuyer = buyers.getText();
                double poAmount = Double.parseDouble(maxSpendAmountField.getText());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String prNotes = notes.getTextArea().getText();

                PurchaseRequisition preq = new PurchaseRequisition();
                preq.setId(preqId);
                preq.setName(prName);
                preq.setSupplier(forVendor);
                preq.setBuyer(forBuyer);
                preq.setNumber(preqId);
                preq.setMaxSpend(poAmount);
                preq.setStart(dateFormat.format(startDateField.getSelectedDate()));
                preq.setEnd(dateFormat.format(endDateField.getSelectedDate()));
                preq.setNotes(prNotes);

                ArrayList<OrderLineItem> lineitems = new ArrayList<>();
                for (int row = 0; row < model.getRowCount(); row++) {
                    String itemName = model.getValueAt(row, 0).toString();
                    String itemId = model.getValueAt(row, 1).toString();
                    double itemQty = Double.parseDouble(model.getValueAt(row, 4).toString());
                    double itemPrice = Double.parseDouble(model.getValueAt(row, 5).toString());
                    double itemTotal = Double.parseDouble(model.getValueAt(row, 6).toString());
                    lineitems.add(new OrderLineItem(itemName, itemId, itemQty, itemPrice, itemTotal));
                }
                preq.setItems(lineitems);

                Pipe.save("/ORDS/PR", preq);
                dispose();
            }
        });
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(execute);

        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    public JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        suppliers = Elements.input(15);
        buyers = Elements.input();
        descriptionField = Elements.input("Purchase Orders to Vendor");
        maxSpendAmountField = Elements.input("500.00");
        startDateField = new DatePicker();
        endDateField = new DatePicker();
        isSingleOrder = new JCheckBox();

        Form f = new Form();
        f.addInput(Elements.coloredLabel("Description", Constants.colors[10]), descriptionField);
        f.addInput(Elements.coloredLabel("Supplier", Constants.colors[8]), suppliers);
        f.addInput(Elements.coloredLabel("Buyer", Constants.colors[7]), buyers);
        f.addInput(Elements.coloredLabel("Max Spend", Constants.colors[6]), maxSpendAmountField);
        f.addInput(Elements.coloredLabel("[or] Single Order", Constants.colors[5]), isSingleOrder);
        f.addInput(Elements.coloredLabel("Start Date", Constants.colors[4]), startDateField);
        f.addInput(Elements.coloredLabel("End Date", Constants.colors[3]), endDateField);
        isSingleOrder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (isSingleOrder.isSelected()) {
                    maxSpendAmountField.setText("");
                    maxSpendAmountField.setEditable(false);
                } else {
                    maxSpendAmountField.setEditable(true);
                }
            }
        });
        general.add(f);

        return general;
    }

    private JPanel items() {

        JPanel p = new JPanel(new BorderLayout());
        ArrayList<Item> items = Engine.getItems();
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products found", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
        model = new ItemTableModel(Collections.singletonList(items.getFirst()));
        JTable table = new JTable(model);

        TableCellRenderer centerRenderer = new CenteredRenderer();
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        JComboBox<Item> itemComboBox = new JComboBox<>(items.toArray(new Item[0]));
        itemComboBox.addActionListener(_ -> updateTotal());
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(itemComboBox));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setBackground(UIManager.getColor("Panel.background"));
        IconButton addButton = new IconButton("Add Product", "add_rows", "Add products");
        addButton.addActionListener((ActionEvent _) -> {
            if (!items.isEmpty()) {
                model.addRow(items.getFirst());
                updateTotal();
            }
        });
        IconButton removeButton = new IconButton("Remove Product", "delete_rows", "Remove selected product");
        removeButton.addActionListener((ActionEvent _) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
                updateTotal();
            }
        });
        buttons.add(removeButton);
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(addButton);
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(600, 300));
        p.add(sp, BorderLayout.CENTER);
        p.add(buttons, BorderLayout.NORTH);

        netAmount = Elements.label("");
        p.add(netAmount, BorderLayout.SOUTH);
        updateTotal(); // set initial text with proper formatting

        return p;
    }

    private void updateTotal() {

        if (model == null) return;
        System.out.println(model.getTotalPrice());
        String maxSpendCalc = model.getTotalPrice();
        netAmount.setText("Max Spend $" + maxSpendCalc);
        maxSpendAmountField.setText(maxSpendCalc);
    }

    private RTextScrollPane notes() {
        notes = Elements.simpleEditor();
        return notes;
    }
}