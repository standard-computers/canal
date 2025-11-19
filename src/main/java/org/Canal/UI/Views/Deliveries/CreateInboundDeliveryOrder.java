package org.Canal.UI.Views.Deliveries;

import org.Canal.Models.BusinessUnits.Account;
import org.Canal.Models.BusinessUnits.Order;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * /TRANS/IDO/NEW
 */
public class CreateInboundDeliveryOrder extends LockeState {

    DesktopState desktop;
    RefreshListener refreshListener;

    //General Info
    private JTextField salesOrderId;
    private JTextField purchaseOrderId;
    private JTextField origin;
    private JTextField destination;
    private DatePicker expectedDeliveryDate;
    private JTextField deliveryValue;

    //Controls Tab
    private JTextField truckId;
    private JTextField destinationArea;
    private JTextField destinationBin;

    //Notes Tab
    private RTextScrollPane notes;

    public CreateInboundDeliveryOrder(DesktopState desktop, RefreshListener refreshListener) {

        super("Create Inbound Delivery Order", "/TRANS/IDO/NEW", false, true, false, true);
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());
        tabs.addTab("Packaging", packaging());

        if ((boolean) Engine.codex.getValue("TRANS/IDO", "allow_notes")) {
            tabs.addTab("Notes", notes());
        }

        setLayout(new BorderLayout());
        add(toolbar(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy from Delivery");
        copyFrom.addActionListener(_ -> {
            String accountId = JOptionPane.showInputDialog("Enter Delivery ID");
            Account account = Engine.getAccount(accountId);
            if (account == null) {
            }
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Details");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create Inbound Delivery (Ctrl + S)");
        create.addActionListener(_ -> {

            if (origin.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must select an origin!", "Origin Location", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                Location originLocation = Engine.getLocationWithId(origin.getText().trim());
                if (originLocation == null) {
                    JOptionPane.showMessageDialog(null, "Selected origin does not exist!", "Origin Location", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (destination.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must select an destination!", "Destination Location", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                Location originLocation = Engine.getLocationWithId(destination.getText().trim());
                if (originLocation == null) {
                    JOptionPane.showMessageDialog(null, "Selected destination does not exist!", "Destination Location", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!truckId.getText().trim().isEmpty()) {
                Truck inboundTruck = Engine.getTruck(truckId.getText().trim());
                if (inboundTruck == null) {
                    JOptionPane.showMessageDialog(null, "Selected truck does not exist!", "Truck ID", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Delivery newInboundDelivery = new Delivery();
            newInboundDelivery.setType("TRANS/IDO");
            newInboundDelivery.setId(Engine.generateId("TRANS/IDO"));
            newInboundDelivery.setSalesOrder(salesOrderId.getText().trim());
            newInboundDelivery.setPurchaseOrder(purchaseOrderId.getText().trim());
            newInboundDelivery.setOrigin(origin.getText().trim());
            newInboundDelivery.setDestination(destination.getText().trim());
            newInboundDelivery.setDestinationArea(destinationArea.getText().trim());
            newInboundDelivery.setDestinationDoor(destinationBin.getText().trim());
            newInboundDelivery.setTotal(deliveryValue.getText().trim());
            newInboundDelivery.setTruck(truckId.getText().trim());

            dispose();
            if (refreshListener != null) refreshListener.refresh();
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-create");
        rp.getActionMap().put("do-create", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create.doClick();
            }
        });

        toolbar.add(Elements.header("Create an Inbound Delivery", SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);
        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        salesOrderId = Elements.input();
        salesOrderId.getDocument().addDocumentListener(new DocumentListener() {
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
                Order salesOrder = Engine.getSalesOrder(salesOrderId.getText().trim());
                origin.setText(salesOrder.getVendor());
                destination.setText(salesOrder.getShipTo());
            }
        });

        purchaseOrderId = Elements.input();
        purchaseOrderId.getDocument().addDocumentListener(new DocumentListener() {
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
                Order purchaseOrder = Engine.getPurchaseOrder(purchaseOrderId.getText().trim());
                origin.setText(purchaseOrder.getVendor());
                destination.setText(purchaseOrder.getShipTo());

            }
        });

        origin = Elements.input();

        destination = Elements.input();

        expectedDeliveryDate = new DatePicker();
        deliveryValue = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Sales Order", "Sales Order ID."), salesOrderId);
        form.addInput(Elements.inputLabel("Purchase Order", "Purchase Order ID."), purchaseOrderId);
        form.addInput(Elements.inputLabel("Origin", "Location ID of where this delivery is from (originating out of)."), origin);
        form.addInput(Elements.inputLabel("Destination", "Location ID of where this delivery is going to."), destination);
        form.addInput(Elements.inputLabel("Expected Delivery", "Date when this delivery will arrive to its destination."), expectedDeliveryDate);
        form.addInput(Elements.inputLabel("Value (Total)", "The total value of the stock in this delivery. Will be automatically set when entering packaging."), deliveryValue);
        general.add(form);

        return general;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        truckId = Elements.input();
        destinationArea = Elements.input();
        destinationBin = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Truck", "ID of the Truck this delivery is on."), truckId);
        form.addInput(Elements.inputLabel("Destination Area", "Area ID of where this area is going to (if no Bin ID provided)."), destinationArea);
        form.addInput(Elements.inputLabel("Destination Door (Bin)", "Bin ID of where this delivery is going to with the Bin as a Door."), destinationBin);
        controls.add(form);

        return controls;
    }

    private JPanel packaging() {

        JPanel packaging = new JPanel(new FlowLayout(FlowLayout.LEFT));

        return packaging;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview() {

        if (salesOrderId.getText().trim().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Sales Order is not selected, are you sure?"});
        }

        if (purchaseOrderId.getText().trim().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Purchase Order is not selected, are you sure?"});
        }

        if (origin.getText().trim().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "No origin location selected!!!"});
        } else {
            Location l = Engine.getLocationWithId(origin.getText().trim());
            if (l == null) {
                addToQueue(new String[]{"CRITICAL", "Origin location selected does not exist!!!"});
            }
        }

        if (destination.getText().trim().isEmpty()) {
            addToQueue(new String[]{"CRITICAL", "No destination location selected!!!"});
        } else {
            Location l = Engine.getLocationWithId(destination.getText().trim());
            if (l == null) {
                addToQueue(new String[]{"CRITICAL", "Destination location selected does not exist!!!"});
            }
        }

        if (Double.parseDouble(deliveryValue.getText()) <= 0) {
            addToQueue(new String[]{"WARNING", "Delivery value is less than or equal to 0, are you sure?"});
        }

        if (truckId.getText().trim().isEmpty()) {
            addToQueue(new String[]{"WARNING", "Truck ID is not selected, are you sure?"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}