package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * /ORDS/PR/AUTO_MK
 */
public class AutoMakePurchaseRequisitions extends LockeState {

    //Operating Objects
    private DesktopState desktop;
    private ItemTableModel model;
    private ArrayList<Item> items;

    //General Information
    private JTextField descriptionField;
    private JTextField maxSpendField;
    private JCheckBox isSingleOrder;

    //Items Tab
    private JLabel netAmount;

    //Buyers Tab
    private JPanel checkboxPanel;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes = new ArrayList<>();
    private DatePicker prStartDateField;
    private DatePicker prEndDateField;
    private JTextField supplier;
    private RTextScrollPane notes;

    public AutoMakePurchaseRequisitions(DesktopState desktop) {

        super("AutoMake Purchase Reqs.", "/ORDS/PR/AUTO_MK");
        setFrameIcon(new ImageIcon(AutoMakePurchaseRequisitions.class.getResource("/icons/automake.png")));
        this.desktop = desktop;
        locations = Engine.getLocations();

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Information", information());
        tabs.addTab("Items", items());
        tabs.addTab("Buyers", buyers());
        tabs.addTab("Notes", notes());


        JPanel p = new JPanel(new BorderLayout());
        p.add(Elements.header("AutoMake Purchase Requisitions", SwingConstants.LEFT), BorderLayout.NORTH);
        p.add(toolbar(), BorderLayout.SOUTH);

        add(p, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        model.addTableModelListener(_ -> updateTotal());
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton copy = new IconButton("Copy From", "open", "Copy from PR");
        copy.addActionListener(_ -> {

            String prId = JOptionPane.showInputDialog("Enter PR ID");
            PurchaseRequisition pr = Engine.getPurchaseRequisition(prId);
            if(pr == null) {
                JOptionPane.showMessageDialog(null, "Purchase Requisition Not Found");
            }else{
                descriptionField.setText(pr.getName()); //TODO Inspect name for description on PR
                maxSpendField.setText(String.valueOf(pr.getMaxSpend()));
                isSingleOrder.setSelected(pr.isSingleOrder());
                supplier.setText(pr.getSupplier());
                prStartDateField.setSelectedDate(new Date(pr.getStart()));
                prEndDateField.setSelectedDate(new Date(pr.getEnd()));
            }
        });
        tb.add(copy);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "Review", "Review AutoMake");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton automake = new IconButton("Start AutoMake", "automake", "AutoMake");
        automake.addActionListener(_ -> {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (int i = 0; i < checkboxes.size(); i++) {
                JCheckBox checkbox = checkboxes.get(i);

                if (checkbox.isSelected()) {

                    String genId = Engine.generateId("ORDS/PR");
                    PurchaseRequisition newPurchaseRequisition = new PurchaseRequisition();
                    newPurchaseRequisition.setId(genId);
                    newPurchaseRequisition.setName(descriptionField.getText());
                    newPurchaseRequisition.setNumber(genId);
                    newPurchaseRequisition.setName(genId);
                    newPurchaseRequisition.setOwner("ORDS/PR/AUTO_MK");
                    newPurchaseRequisition.setSupplier(supplier.getText());
                    newPurchaseRequisition.setBuyer(locations.get(i).getId());
                    newPurchaseRequisition.setMaxSpend(Double.valueOf(maxSpendField.getText()));
                    newPurchaseRequisition.setStart(dateFormat.format(prStartDateField.getSelectedDate()));
                    newPurchaseRequisition.setEnd(dateFormat.format(prEndDateField.getSelectedDate()));
                    newPurchaseRequisition.setNotes(notes.getTextArea().getText());

                    ArrayList<OrderLineItem> lineitems = new ArrayList<>();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        String itemName = model.getValueAt(row, 0).toString();
                        String itemId = model.getValueAt(row, 1).toString();
                        double itemQty = Double.parseDouble(model.getValueAt(row, 4).toString());
                        double itemPrice = Double.parseDouble(model.getValueAt(row, 5).toString());
                        double itemTotal = Double.parseDouble(model.getValueAt(row, 6).toString());
                        lineitems.add(new OrderLineItem(itemName, itemId, itemQty, itemPrice, itemTotal));
                    }
                    newPurchaseRequisition.setItems(lineitems);

                    Pipe.save("/ORDS/PR", newPurchaseRequisition);
                }
            }
            dispose();
            JOptionPane.showMessageDialog(null, "AutoMake Purchase Reqs Complete");
        });
        tb.add(automake);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        return tb;
    }

    private JPanel information() {

        JPanel information = new JPanel(new FlowLayout(FlowLayout.LEFT));

        descriptionField = Elements.input(10);
        maxSpendField = Elements.input();
        isSingleOrder = new JCheckBox();

        supplier = Elements.input();
        supplier.addActionListener(_ -> {
            ArrayList<Item> allItems = Engine.getItems();
            for(Item item : allItems) {
                if(item.getVendor().equals(supplier.getText())) {
                    items.add(item);
                }
            }
            updateTotal();
            revalidate();
            repaint();
        });

        prStartDateField = new DatePicker();
        prEndDateField = new DatePicker();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Description", Constants.colors[9]), descriptionField);
        form.addInput(Elements.coloredLabel("Max Spend", Constants.colors[8]), maxSpendField);
        form.addInput(Elements.coloredLabel("[or] Single Order", UIManager.getColor("Label.foreground")), isSingleOrder);
        form.addInput(Elements.coloredLabel("Supplier", Constants.colors[7]), supplier);
        form.addInput(Elements.coloredLabel("Start Date", Constants.colors[6]), prStartDateField);
        form.addInput(Elements.coloredLabel("End Date", Constants.colors[5]), prEndDateField);
        information.add(form);

        return information;
    }

    private JPanel buyers() {

        JPanel buyers = new JPanel(new BorderLayout());

        JTextField search = Elements.input();
        search.addActionListener(_ -> {

            String searchValue = search.getText().trim();
            if (searchValue.endsWith("*")) { //Searching for ID starts with

                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().startsWith(searchValue.substring(0, searchValue.length() - 1))) {
                        checkbox.setSelected(!checkbox.isSelected());
                    }
                }
            } else if (searchValue.startsWith("/")) { //Objex type selection

                for (int i = 0; i < checkboxes.size(); i++) {
                    if (locations.get(i).getType().equals(searchValue.toUpperCase())) {
                        checkboxes.get(i).setSelected(!checkboxes.get(i).isSelected());
                    }
                }

            } else { //Select exact match

                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().equals(searchValue)) {
                        checkbox.setSelected(!checkbox.isSelected());
                    }
                }
            }
        });

        buyers.add(search, BorderLayout.NORTH);

        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        addCheckboxes();
        JScrollPane sp = new JScrollPane(checkboxPanel);
        sp.setPreferredSize(new Dimension(300, 300));

        JPanel opts = new JPanel(new GridLayout(1, 2));

        JButton sa = Elements.button("Select All");
        sa.addActionListener(_ -> {
            checkboxes.forEach(cb -> cb.setSelected(true));
            repaint();
        });
        opts.add(sa);

        JButton dsa = Elements.button("Deselect All");
        dsa.addActionListener(_ -> {
            checkboxes.forEach(cb -> cb.setSelected(false));
            repaint();
        });
        opts.add(dsa);

        buyers.add(opts, BorderLayout.SOUTH);
        buyers.add(sp, BorderLayout.CENTER);

        return buyers;
    }

    private void addCheckboxes() {

        for (Location location : locations) {
            String displayText = location.getId() + " - " + location.getName();
            JCheckBox checkbox = new JCheckBox(displayText);
            checkbox.setActionCommand(String.valueOf(location.getId())); // Set the value as ID
            checkboxes.add(checkbox);
            checkboxPanel.add(checkbox);
        }
    }

    private JPanel items() {

        JPanel p = new JPanel(new BorderLayout());
        items = Engine.getItems();
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

        IconButton addButton = new IconButton("Add Item", "add_rows", "Add item");
        addButton.addActionListener((ActionEvent _) -> {
            if (!items.isEmpty()) {
                model.addRow(items.getFirst());
                updateTotal();
            }
        });
        buttons.add(addButton);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton removeButton = new IconButton("Remove Item", "delete_rows", "Remove selected item");
        removeButton.addActionListener((ActionEvent _) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
                updateTotal();
            }
        });
        buttons.add(removeButton);
        buttons.add(Box.createHorizontalStrut(5));

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
        maxSpendField.setText(maxSpendCalc);
    }

    private JScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview() {

        //Warn if no PR description
        if (descriptionField.getText().isEmpty()) {
            addToQueue(new String[]{"WARNING", "No Purchase Req description set. Are you sure?"});
        }

        //Check if supplier selected and validity
        if (supplier.getText().isEmpty()) {

            addToQueue(new String[]{"CRITICAL", "NO SUPPLIER SELECTED!!!"});
        } else {

            Location foundVendor = Engine.getLocation(supplier.getText(), "VEND");
            if (foundVendor == null) {

                addToQueue(new String[]{"CRITICAL", "SELECTED SUPPLIER DOES NOT EXIST!!!"});
            }else{
                if(model.getRowCount() > 0){

                    for (int row = 0; row < model.getRowCount(); row++) {

                        String itemId = model.getValueAt(row, 1).toString();
                        Item item = Engine.getItem(itemId);
                        if(!item.getVendor().equals(supplier.getText())){
                            addToQueue(new String[]{"WARNING", "Item " + itemId + " is not supplied by selected vendor!"});
                        }
                    }
                }else{

                    if(!isSingleOrder.isSelected()){
                        addToQueue(new String[]{"MESSAGE", "No Items selected, are you sure?"});
                    }
                }
            }
        }

        //Check buyer count, must be greater than 0
        int buyerCount = 0;
        for (JCheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                buyerCount++;
            }
        }
        if (buyerCount == 0) {
            addToQueue(new String[]{"CRITICAL", "NO BUYERS SELECTED!!!"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}
