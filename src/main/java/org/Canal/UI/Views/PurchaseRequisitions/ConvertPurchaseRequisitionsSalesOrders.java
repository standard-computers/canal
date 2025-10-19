package org.Canal.UI.Views.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.*;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.PurchaseOrders.PurchaseOrders;
import org.Canal.UI.Views.System.LockeMessages;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * /ORDS/PR/SO
 */
public class ConvertPurchaseRequisitionsSalesOrders extends LockeState {

    private DesktopState desktop;
    private JPanel checkboxPanel;
    private ArrayList<PurchaseRequisition> purchaseRequisitions = new ArrayList<>();
    private ArrayList<JCheckBox> checkboxes = new ArrayList<>();

    //Ledger Tab
    private JCheckBox commitToLedger;
    private Selectable organizations;
    private Selectable ledgerId;

    //Delivery Tab
    private JCheckBox createDelivery;
    private Selectable carriers;
    private DatePicker expectedDelivery;

    //Notes Tab
    private RTextScrollPane notes;

    public ConvertPurchaseRequisitionsSalesOrders(DesktopState desktop) {

        super("Convert Purchase Requisitions", "/ORDS/PR/SO");
        setFrameIcon(new ImageIcon(ConvertPurchaseRequisitionsSalesOrders.class.getResource("/icons/start.png")));
        this.desktop = desktop;

        for(PurchaseRequisition purchaseRequisition : Engine.getPurchaseRequisitions()){
            if(purchaseRequisition.getStatus().equals(LockeStatus.ACTIVE)
            || purchaseRequisition.getStatus().equals(LockeStatus.NEW)){
                purchaseRequisitions.add(purchaseRequisition);
            }
        }
        if(purchaseRequisitions.isEmpty()){
            JOptionPane.showMessageDialog(null, "There are no purchase requisitions to convert!");
            //TODO Figure this out because the LockeCheck doesn't work either
            return;
        }

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Purchase Requisitions", purchaseRequisitions());
        tabs.addTab("Delivery", delivery());
        tabs.addTab("Ledger", ledger());
        tabs.addTab("Taxes & Rates", taxes());
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(toolbar(), BorderLayout.NORTH);
    }

    private JPanel toolbar() {
        
        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton review = new IconButton("Review", "Review", "Review AutoMake");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton automake = new IconButton("Convert Purchase Reqs", "start", "AutoMake");
        automake.addActionListener(_ -> {

            // 1) Gather selection & do quick validations on the EDT
            java.util.List<Integer> selectedIdxs = new ArrayList<>();
            for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).isSelected()) selectedIdxs.add(i);
            }
            if (selectedIdxs.isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are no purchase requisitions selected!");
                return;
            }
            if (expectedDelivery.getSelectedDate() == null) {
                JOptionPane.showMessageDialog(null, "Must select a delivery date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2) Show the loader
            Loading loader = new Loading("Converting Purchase Requisitions", selectedIdxs.size() * 4);
            loader.setConsoleVisible(true);
            loader.open();

            // 3) Run heavy work off the EDT
            SwingWorker<Void, String> worker = new SwingWorker<>() {

                private void ui(Runnable r) { SwingUtilities.invokeLater(r); } // helper

                @Override
                protected Void doInBackground() {
                    for (int idx : selectedIdxs) {
                        JCheckBox checkbox = checkboxes.get(idx);
                        if (!checkbox.isSelected()) continue; // guard if user toggles mid-run

                        PurchaseRequisition purchaseRequisition = purchaseRequisitions.get(idx);

                        // --- Create PO ---
                        String purchaseOrderId = Engine.generateId("ORDS/PO");
                        Order purchaseOrder = new Order();
                        purchaseOrder.setType("ORDS/PO");
                        purchaseOrder.setId(purchaseOrderId);
                        purchaseOrder.setOrderId(purchaseOrderId);
                        purchaseOrder.setCreator("ORDS/PR/PO");
                        purchaseOrder.setOwner(Engine.getAssignedUser().getId());
                        purchaseOrder.setOrderedOn(Constants.now());
                        purchaseOrder.setExpectedDelivery(expectedDelivery.getSelectedDateString());
                        purchaseOrder.setPurchaseRequisition(purchaseRequisition.getId());
                        purchaseOrder.setBillTo(purchaseRequisition.getBuyer());
                        purchaseOrder.setShipTo(purchaseRequisition.getBuyer());
                        purchaseOrder.setCustomer(purchaseRequisition.getBuyer());
                        purchaseOrder.setVendor(purchaseRequisition.getSupplier());
                        purchaseOrder.setItems(purchaseRequisition.getItems());

                        double netValue = 0.0;
                        if (!purchaseRequisition.getItems().isEmpty()) {
                            for (OrderLineItem oi : purchaseRequisition.getItems()) {
                                netValue += (oi.getQuantity() * oi.getPrice());
                            }
                        }
                        purchaseOrder.setNetValue(netValue);
                        purchaseOrder.setTotal(netValue); // TODO: taxes/rates

                        Pipe.save("ORDS/PO", purchaseOrder);
                        ui(() -> {
                            loader.append("Creating Purchase Order " + purchaseOrderId + " for " + purchaseRequisition.getId());
                            loader.setMessage("Creating Purchase Order " + purchaseOrderId + "…");
                            loader.increment();
                        });

                        // --- Optional: Delivery ---
                        if (createDelivery.isSelected()) {
                            Truck t = new Truck();
                            String truckId = Engine.generateId("TRANS/TRCKS");
                            t.setId(truckId);
                            t.setNumber(truckId);
                            t.setCarrier(carriers.getSelectedValue());
                            Pipe.save("/TRANS/TRCKS", t);
                            ui(() -> {
                                loader.append("Creating Truck " + truckId + " for " + purchaseOrderId);
                                loader.setMessage("Creating Truck " + truckId + "…");
                                loader.increment();
                            });

                            Delivery d = new Delivery();
                            String inboundDeliveryId = Engine.generateId("TRANS/IDO");
                            d.setType("TRANS/IDO");
                            d.setId(inboundDeliveryId);
                            d.setPurchaseOrder(purchaseOrder.getId());
                            d.setExpectedDelivery(purchaseOrder.getExpectedDelivery());
                            d.setDestination(purchaseOrder.getShipTo());
                            d.setStatus(LockeStatus.PROCESSING);
                            Pipe.save("/TRANS/IDO", d);
                            ui(() -> {
                                loader.append("Creating Inbound Delivery " + inboundDeliveryId + " for " + purchaseOrderId);
                                loader.setMessage("Creating Inbound Delivery " + inboundDeliveryId + "…");
                                loader.increment();
                            });
                        }

                        // --- Optional: Ledger ---
                        if (commitToLedger.isSelected()) {
                            Ledger l = Engine.hasLedger(purchaseRequisition.getBuyer());
                            if (l == null) l = Engine.getLedger(ledgerId.getSelectedValue());

                            if (l != null) {
                                Location billToLocation = Engine.getLocationWithId(purchaseOrder.getBillTo());
                                Transaction t = new Transaction();
                                t.setId(Constants.generateId(5));
                                t.setCreator(getLocke());
                                t.setOwner(Engine.getAssignedUser().getId());
                                t.setLocke(getLocke());
                                t.setObjex(billToLocation.getType());
                                t.setLocation(purchaseOrder.getBillTo());
                                t.setReference(purchaseOrderId);
                                t.setAmount(-1 * purchaseOrder.getTotal());
                                t.setCommitted(Constants.now());
                                t.setStatus(LockeStatus.PROCESSING);
                                l.addTransaction(t);
                                l.save();
                                ui(() -> {
                                    loader.append("Creating Transaction " + t.getId() + " for " + purchaseOrderId);
                                    loader.setMessage("Creating Transaction " + t.getId() + "…");
                                    loader.increment();
                                });
                            } else {
                                // Surface the error back on the EDT
                                ui(() -> JOptionPane.showMessageDialog(null, "No such ledger.", "Error", JOptionPane.ERROR_MESSAGE));
                            }
                        }

                        // --- Finalize PR ---
                        purchaseRequisition.setStatus(LockeStatus.COMPLETED);
                        purchaseRequisition.save();
                        ui(() -> {
                            loader.append("Updated " + purchaseRequisition.getId() + " → COMPLETED");
                            loader.setMessage("Finalizing " + purchaseRequisition.getId() + "…");
                            loader.increment();
                        });
                    }
                    return null;
                }

                @Override
                protected void done() {
                    SwingUtilities.invokeLater(() -> {
                        desktop.put(new PurchaseOrders(desktop));
                        loader.close();
                        dispose();
                        JOptionPane.showMessageDialog(null, "Conversion of Purchase Reqs Complete");
                    });
                }
            };

            worker.execute();
        });

        tb.add(automake);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        toolbar.add(Elements.header("Convert Purchase Requisitions", SwingConstants.LEFT), BorderLayout.CENTER);
        toolbar.add(tb, BorderLayout.SOUTH);

        return toolbar;
    }

    private JPanel purchaseRequisitions() {

        JPanel purchaseReqsView = new JPanel(new BorderLayout());

        JTextField search = Elements.input();
        search.addActionListener(_ -> {

            String searchValue = search.getText().trim();
            if (searchValue.endsWith("*")) { //Searching for ID starts with

                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().startsWith(searchValue.substring(0, searchValue.length() - 1))) {
                        checkbox.setSelected(!checkbox.isSelected());
                    }
                }
            } else if (searchValue.startsWith("*")) { //Searching for ID starts with

                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.getText().endsWith(searchValue.substring(1))) {
                        checkbox.setSelected(!checkbox.isSelected());
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

        purchaseReqsView.add(search, BorderLayout.NORTH);

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

        purchaseReqsView.add(opts, BorderLayout.SOUTH);
        purchaseReqsView.add(sp, BorderLayout.CENTER);

        return purchaseReqsView;
    }

    private void addCheckboxes() {

        for (PurchaseRequisition purchaseRequisition : purchaseRequisitions) {
            String displayText = purchaseRequisition.getId() + " - " + purchaseRequisition.getBuyer();
            JCheckBox checkbox = new JCheckBox(displayText);
            checkbox.setActionCommand(String.valueOf(purchaseRequisition.getId())); // Set the value as ID
            checkboxes.add(checkbox);
            checkboxPanel.add(checkbox);
        }
    }

    private JPanel delivery() {

        JPanel delivery = new JPanel(new BorderLayout());

        JPanel formHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));

        createDelivery = new JCheckBox();
        if ((boolean) Engine.codex("ORDS/PO", "use_deliveries")) {
            createDelivery.setSelected(true);
            createDelivery.setEnabled(false);
        }
        carriers = Selectables.carriers();
        expectedDelivery = new DatePicker();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Create Inbound Delivery (IDO) for Ship-To", Constants.colors[10]), createDelivery);
        form.addInput(Elements.coloredLabel("Carrier", Constants.colors[9]), carriers);
        form.addInput(Elements.coloredLabel("Expected Delivery Date", Constants.colors[8]), expectedDelivery);

        formHolder.add(form);
        JLabel warning = Elements.label("THIS APPLIES TO ALL GENERATED PURCHASE ORDERS");
        warning.setBorder(BorderFactory.createLineBorder(new Color(255, 183, 15), 5));
        warning.setBackground(new Color(255, 183, 15));
        delivery.add(warning, BorderLayout.NORTH);
        delivery.add(form, BorderLayout.CENTER);

        return delivery;
    }

    private JPanel ledger() {

        JPanel ledger = new JPanel(new FlowLayout(FlowLayout.LEFT));
        commitToLedger = new JCheckBox();
        if ((boolean) Engine.codex("ORDS/PO", "commit_to_ledger")) {
            commitToLedger.setSelected(true);
            commitToLedger.setEnabled(false);
        }
        organizations = Selectables.organizations();
        ledgerId = Selectables.ledgers();

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Commit to Ledger", UIManager.getColor("Label.foreground")), commitToLedger);
        form.addInput(Elements.coloredLabel("Purchasing Org.", UIManager.getColor("Label.foreground")), organizations);
        form.addInput(Elements.coloredLabel("Ledger", UIManager.getColor("Label.foreground")), ledgerId);
        ledger.add(form);

        return ledger;
    }

    private JPanel taxes() {

        JPanel taxes = new JPanel(new BorderLayout());
        ArrayList<Object[]> ts = new ArrayList<>();

        CustomTable taxesTable = new CustomTable(new String[]{
                "Name",
                "Jurisdiction",
                "Percent",
                "Amount",
        }, ts);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setBackground(UIManager.getColor("Panel.background"));

        IconButton addButton = new IconButton("Add Tax", "add_rows", "Add tax");
        addButton.addActionListener((ActionEvent _) -> {
            if (!ts.isEmpty()) {
            }
        });
        buttons.add(addButton);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton removeButton = new IconButton("Remove Tax", "delete_rows", "Remove selected tax");
        removeButton.addActionListener((ActionEvent _) -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow != -1) {
//            }
        });
        buttons.add(removeButton);
        buttons.add(Box.createHorizontalStrut(5));

        JScrollPane sp = new JScrollPane(taxesTable);
        sp.setPreferredSize(new Dimension(600, 300));
        taxes.add(sp, BorderLayout.CENTER);
        taxes.add(buttons, BorderLayout.NORTH);
        return taxes;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview() {

        //Check buyer count, must be greater than 0
        int purchaseReqsCount = 0;
        for (JCheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                purchaseReqsCount++;
            }
        }
        if (purchaseReqsCount == 0) {
            addToQueue(new String[]{"CRITICAL", "NO PURCHASE REQUISITIONS SELECTED!!!"});
        }

        desktop.put(new LockeMessages(getQueue()));
        purgeQueue();
    }
}
