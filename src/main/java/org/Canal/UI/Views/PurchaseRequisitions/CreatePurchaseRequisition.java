package org.Canal.UI.Views.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.StockLine;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.DesktopState;
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

    private DesktopState desktop;

    //General Info Tab
    private JTextField description;
    private JTextField supplier;
    private JTextField buyer;
    private JTextField maxSpendAmount;
    private JCheckBox isSingleOrder;
    private DatePicker startDate;
    private DatePicker endDate;

    //Items Tab
    private ItemTableModel model;
    private JLabel netAmount;

    //Notes Tab
    private RTextScrollPane notes;

    public CreatePurchaseRequisition(DesktopState desktop) {

        super("Create Purchase Requisition", "/ORDS/PR/NEW");
        setFrameIcon(new ImageIcon(CreatePurchaseRequisition.class.getResource("/icons/create.png")));
        this.desktop = desktop;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Items", items());

        if ((boolean) Engine.codex.getValue("ORDS/PR", "allow_notes")) {
            tabs.addTab("Notes", notes());
        }

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        model.addTableModelListener(_ -> updateTotal());
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        toolbar.add(Elements.header("Create Purchase Requisition", SwingConstants.LEFT), BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy Purchase Requisition");
        copyFrom.addActionListener(_ -> {
            String prId = JOptionPane.showInputDialog(CreatePurchaseRequisition.this, "Enter Purchase Requisition ID");
            PurchaseRequisition pr = Engine.getPurchaseRequisition(prId);
            if (pr != null) {
                maxSpendAmount.setText(String.valueOf(pr.getMaxSpend()));
                isSingleOrder.setSelected(pr.isSingleOrder());
                startDate.setSelectedDate(new Date(pr.getStart()));
                endDate.setSelectedDate(new Date(pr.getEnd()));
                notes.getTextArea().setText(pr.getNotes());
            }
        });
        buttons.add(copyFrom);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Purchase Requisition");
        review.addActionListener(_ -> performReview());
        buttons.add(review);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create Purchase Requisition");
        create.addActionListener(_ -> {

            if (supplier.getText().isEmpty()) {
                JOptionPane.showMessageDialog(CreatePurchaseRequisition.this, "You must enter a supplier ID!");
                return;
            }

            if (buyer.getText().isEmpty()) {
                JOptionPane.showMessageDialog(CreatePurchaseRequisition.this, "You must enter a supplier ID!");
                return;
            }

            String preqId = Engine.generateId("ORDS/PR");
            String prName = description.getText();
            String forVendor = supplier.getText();
            String forBuyer = buyer.getText();
            double poAmount = Double.parseDouble(maxSpendAmount.getText());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String prNotes = notes.getTextArea().getText();

            PurchaseRequisition preq = new PurchaseRequisition();
            preq.setId(preqId);
            preq.setName(prName);
            preq.setSupplier(forVendor);
            preq.setBuyer(forBuyer);
            preq.setNumber(preqId);
            preq.setMaxSpend(poAmount);
            preq.setStart(dateFormat.format(startDate.getSelectedDate()));
            preq.setEnd(dateFormat.format(endDate.getSelectedDate()));
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
        });
        buttons.add(create);
        buttons.add(Box.createHorizontalStrut(5));
        toolbar.add(buttons, BorderLayout.SOUTH);

        return toolbar;
    }

    public JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        supplier = Elements.input(15);
        buyer = Elements.input();
        description = Elements.input("Purchase Orders to Vendor");
        maxSpendAmount = Elements.input("500.00");
        startDate = new DatePicker();
        endDate = new DatePicker();
        isSingleOrder = new JCheckBox();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Description"), description);
        form.addInput(Elements.inputLabel("Supplier"), supplier);
        form.addInput(Elements.inputLabel("Buyer"), buyer);
        form.addInput(Elements.inputLabel("Max Spend"), maxSpendAmount);
        form.addInput(Elements.inputLabel("[or] Single Order"), isSingleOrder);
        form.addInput(Elements.inputLabel("Start Date"), startDate);
        form.addInput(Elements.inputLabel("End Date"), endDate);
        isSingleOrder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (isSingleOrder.isSelected()) {
                    maxSpendAmount.setText("");
                    maxSpendAmount.setEditable(false);
                } else {
                    maxSpendAmount.setEditable(true);
                }
            }
        });
        general.add(form);

        return general;
    }

    private JPanel items() {

        JPanel p = new JPanel(new BorderLayout());
        ArrayList<Item> items = Engine.getItems();
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

        JTextField itemIdField = new JTextField(items.getFirst().getId());
        itemIdField.addActionListener(_ -> updateTotal());
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(itemIdField));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setBackground(UIManager.getColor("Panel.background"));
        model.removeRow(0);

        IconButton addItem = new IconButton("Add Item", "add_rows", "Add item");
        addItem.addActionListener((ActionEvent _) -> {
            if (!items.isEmpty()) {
                model.addRow(items.getFirst());
                updateTotal();
            }
        });
        buttons.add(addItem);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton removeItem = new IconButton("Remove Item", "delete_rows", "Remove selected item");
        removeItem.addActionListener((ActionEvent _) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
                updateTotal();
            }
        });
        buttons.add(removeItem);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton explodeItem = new IconButton("Explode Item", "automake", "Get item components");
        explodeItem.addActionListener((ActionEvent _) -> {

            String itemId = JOptionPane.showInputDialog("Enter Item ID");
            if (itemId != null) {
                Item i = Engine.getItem(itemId);
                if (i != null) {
                    for (StockLine ol : i.getComponents()) {
                        Item i2 = Engine.getItem(ol.getItem());
                        model.addRow(i2);
                        updateTotal();
                    }
                }
            }
        });
        buttons.add(explodeItem);
        buttons.add(Box.createHorizontalStrut(5));

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(600, 300));
        p.add(sp, BorderLayout.CENTER);
        p.add(buttons, BorderLayout.NORTH);

        netAmount = Elements.label("");
        p.add(netAmount, BorderLayout.SOUTH);
        updateTotal();

        return p;
    }

    private void updateTotal() {

        if (model == null) return;
        String maxSpendCalc = model.getTotalPrice();
        netAmount.setText("Max Spend $" + maxSpendCalc);
        maxSpendAmount.setText(maxSpendCalc);
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview() {

        if (description.getText().isEmpty()) {
            addToQueue(new String[]{"WARNING", "No description."});
        }

        if (supplier.getText().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "No supplier!!!"});
        } else {
            Location vendor = Engine.getLocationWithId(supplier.getText().trim());
            if (vendor == null) {
                addToQueue(new String[]{"CRITICAL", "Vendor " + supplier.getText().trim() + " does not exist!!!"});
            }
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}