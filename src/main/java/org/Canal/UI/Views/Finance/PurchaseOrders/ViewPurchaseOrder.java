package org.Canal.UI.Views.Finance.PurchaseOrders;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.Order;
import org.Canal.Models.BusinessUnits.Rate;
import org.Canal.Models.SupplyChainUnits.Delivery;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Rates.ViewRate;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.LockeStatus;
import org.Canal.Utils.RefreshListener;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /ORDS/PO/$[PO_ID]
 */
public class ViewPurchaseOrder extends LockeState {

    private Order purchaseOrder;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    private Delivery delivery;
    private Truck truck;

    public ViewPurchaseOrder(Order purchaseOrder, DesktopState desktop, RefreshListener refreshListener) {

        super("Purchase Order", "/ORDS/PO/" + purchaseOrder.getOrderId());
        setFrameIcon(new ImageIcon(ViewPurchaseOrder.class.getResource("/icons/purchasereqs.png")));
        this.purchaseOrder = purchaseOrder;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        delivery = Engine.getInboundDeliveryForPO(purchaseOrder.getOrderId());
        truck = Engine.getTruckForDelivery(delivery.getId());


        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Activity", activity());
        tabs.addTab("Items", items());
        tabs.addTab("Delivery", delivery());
        tabs.addTab("Shipping", shipping());
        tabs.addTab("Packaging", packaging());
        tabs.addTab("Taxes & Rates", taxesAndRates());
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(toolbar(), BorderLayout.NORTH);

        if((boolean) Engine.codex.getValue("ORDS/PO", "start_maximized")){
            setMaximized(true);
        }
    }

    private JPanel toolbar(){

        JPanel buttons = new JPanel(new BorderLayout());
        buttons.add(Elements.header("Viewing Purchase Order " + purchaseOrder.getOrderId(), SwingConstants.LEFT), BorderLayout.NORTH);

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        if(!purchaseOrder.getStatus().equals(LockeStatus.DELIVERED)
            || !purchaseOrder.getStatus().equals(LockeStatus.ARCHIVED)
            || !purchaseOrder.getStatus().equals(LockeStatus.DELETED)){

            IconButton block = new IconButton("Block", "block", "Block/Pause PO, can't be used");
            block.addActionListener(_ -> {
                int cnf = JOptionPane.showConfirmDialog(null, "Set Purchase Requisision Status to BLOCKED?", "Confirm status change?", JOptionPane.YES_NO_OPTION);
                if(cnf == JOptionPane.YES_OPTION) {
                    purchaseOrder.setStatus(LockeStatus.BLOCKED);
                    purchaseOrder.save();
                    if(refreshListener != null){
                        refreshListener.refresh();
                    }
                }
            });
            tb.add(block);
            tb.add(Box.createHorizontalStrut(5));

            IconButton suspend = new IconButton("Suspend", "suspend", "Suspend PO, can't be used");
            suspend.addActionListener(_ -> {
                int cnf = JOptionPane.showConfirmDialog(null, "Set Purchase Requisision Status to SUSPENDED?", "Confirm status change?", JOptionPane.YES_NO_OPTION);
                if(cnf == JOptionPane.YES_OPTION) {
                    purchaseOrder.setStatus(LockeStatus.SUSPENDED);
                    purchaseOrder.save();
                    if(refreshListener != null){
                        refreshListener.refresh();
                    }
                }
            });
            tb.add(suspend);
            tb.add(Box.createHorizontalStrut(5));

            IconButton activate = new IconButton("Start", "start", "Resume/Activate PO");
            tb.add(activate);
            tb.add(Box.createHorizontalStrut(5));
        }

        IconButton archive = new IconButton("Archive", "archive", "Archive PO");
        archive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

                int cnf = JOptionPane.showConfirmDialog(null, "If you archive this purchase requisition, it will no longer be able to be used.", "Confirm archival?", JOptionPane.YES_NO_OPTION);
                if(cnf == JOptionPane.YES_OPTION) {
                    purchaseOrder.setStatus(LockeStatus.ARCHIVED);
                    purchaseOrder.save();
                    if(refreshListener != null){
                        refreshListener.refresh();
                    }
                }
            }
        });
        tb.add(archive);
        tb.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Labels", "label", "Print labels");
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));

        IconButton print = new IconButton("Print", "print", "Print Pururchase Order");
        tb.add(print);
        tb.add(Box.createHorizontalStrut(5));

        buttons.add(tb, BorderLayout.SOUTH);
        return buttons;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Form form = new Form();
        form.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(purchaseOrder.getId()));
        form.addInput(Elements.coloredLabel("Created", UIManager.getColor("Label.foreground")), new Copiable(purchaseOrder.getCreated()));
        form.addInput(Elements.coloredLabel("Creator (Owner)", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getOwner()));
        form.addInput(Elements.coloredLabel("Purchase Req. #", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getOrderId()));
        form.addInput(Elements.coloredLabel("Vendor ID", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getVendor()));
        form.addInput(Elements.coloredLabel("Bill To", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getBillTo()));
        form.addInput(Elements.coloredLabel("Ship To", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getBillTo()));
        form.addInput(Elements.coloredLabel("Net Amount", UIManager.getColor("Label.foreground")),  new Copiable(String.valueOf(purchaseOrder.getNetValue())));
        form.addInput(Elements.coloredLabel("Tax", UIManager.getColor("Label.foreground")),  new Copiable(String.valueOf(purchaseOrder.getTaxAmount())));
        form.addInput(Elements.coloredLabel("Total", UIManager.getColor("Label.foreground")),  new Copiable(String.valueOf(purchaseOrder.getTotal())));
        form.addInput(Elements.coloredLabel("Created", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getCreated()));
        form.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")),  new Copiable(String.valueOf(purchaseOrder.getStatus())));
        form.addInput(Elements.coloredLabel("Created", UIManager.getColor("Label.foreground")),  new Copiable(purchaseOrder.getCreated()));
        general.add(form);

        return general;
    }

    private JScrollPane items(){

        String[] columns = new String[]{
                "Item Name",
                "Item",
                "Vendor",
                "Qty",
                "Price",
                "Total",
                "Status",
                "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (OrderLineItem poLineItem : purchaseOrder.getItems()) {
            Item i = Engine.getItem(poLineItem.getId());
            data.add(new Object[]{
                    i.getName(),
                    poLineItem.getId(),
                    i.getVendor(),
                    poLineItem.getQuantity(),
                    poLineItem.getPrice(),
                    poLineItem.getTotal(),
                    poLineItem.getStatus(),
                    poLineItem.getCreated(),
            });
        }
        CustomTable poLineItems = new CustomTable(columns, data);
        return new JScrollPane(poLineItems);
    }

    private JScrollPane activity(){

//        String[] columns = new String[]{
//                "User",
//                "Type",
//                "Created",
//                "Status",
//        };
//        ArrayList<Object[]> data = new ArrayList<>();
//        data.add(new Object[]{});
//        CustomTable ibds = new CustomTable(columns, data);
        return new JScrollPane();
    }

    private JScrollPane delivery() {
        // Main vertical container
        JPanel deliveryTab = new JPanel();
        deliveryTab.setLayout(new BoxLayout(deliveryTab, BoxLayout.Y_AXIS));

        // Delivery Info Panel
        JPanel deliveryView = new JPanel();
        deliveryView.setLayout(new BoxLayout(deliveryView, BoxLayout.Y_AXIS));
        deliveryView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        Form deliveryInfo = new Form();
        deliveryInfo.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(delivery.getId()));
        deliveryInfo.addInput(Elements.coloredLabel("Expected Delivery", UIManager.getColor("Label.foreground")), new Copiable(delivery.getExpectedDelivery()));
        deliveryInfo.addInput(Elements.coloredLabel("Destination", UIManager.getColor("Label.foreground")), new Copiable(delivery.getDestination()));
        deliveryInfo.addInput(Elements.coloredLabel("Pallets", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(delivery.getPallets().size())));
        deliveryInfo.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(delivery.getStatus())));

        deliveryView.add(Elements.header("Delivery Info"));
        deliveryView.add(Box.createVerticalStrut(5)); // Spacer
        deliveryView.add(deliveryInfo);

        // Truck Info Panel
        JPanel truckView = new JPanel();
        truckView.setLayout(new BoxLayout(truckView, BoxLayout.Y_AXIS));
        truckView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        if(truck != null){

            Form truckInfo = new Form();
            truckInfo.addInput(Elements.coloredLabel("ID", UIManager.getColor("Label.foreground")), new Copiable(truck.getId()));
            truckInfo.addInput(Elements.coloredLabel("Carrier", UIManager.getColor("Label.foreground")), new Copiable(truck.getCarrier()));
            truckInfo.addInput(Elements.coloredLabel("Driver", UIManager.getColor("Label.foreground")), new Copiable(truck.getDriver()));
            truckInfo.addInput(Elements.coloredLabel("Year", UIManager.getColor("Label.foreground")), new Copiable(truck.getYear()));
            truckInfo.addInput(Elements.coloredLabel("Make", UIManager.getColor("Label.foreground")), new Copiable(truck.getMake()));
            truckInfo.addInput(Elements.coloredLabel("Model", UIManager.getColor("Label.foreground")), new Copiable(truck.getModel())); // fixed model line
            truckInfo.addInput(Elements.coloredLabel("Notes", UIManager.getColor("Label.foreground")), new Copiable(truck.getNotes()));
            truckInfo.addInput(Elements.coloredLabel("Status", UIManager.getColor("Label.foreground")), new Copiable(String.valueOf(truck.getStatus())));

            truckView.add(Elements.header("Truck Info"));
            truckView.add(Box.createVerticalStrut(5)); // Spacer
            truckView.add(truckInfo);
        }

        // Stack both views
        deliveryTab.add(deliveryView);
        deliveryTab.add(Box.createVerticalStrut(20)); // space between sections
        deliveryTab.add(truckView);

        return new JScrollPane(deliveryTab);
    }

    private JScrollPane shipping(){

        return new JScrollPane();
    }

    private JScrollPane packaging(){

        return new JScrollPane();
    }

    private JScrollPane taxesAndRates(){

        String[] columns = new String[]{
                "#",
                "ID",
                "Name",
                "Description",
                "Percent",
                "Tax",
                "Value",
        };

        ArrayList<Object[]> data = new ArrayList<>();
        for(int s = 0; s < purchaseOrder.getRates().size(); s++){
            Rate rate = purchaseOrder.getRates().get(s);
            data.add(new Object[]{
                    String.valueOf(s + 1),
                    rate.getId(),
                    rate.getName(),
                    rate.getDescription(),
                    rate.isPercent(),
                    rate.isTax(),
                    rate.getValue()
            });
        }

        CustomTable ct = new CustomTable(columns, data);
        ct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) {
                        String v = String.valueOf(t.getValueAt(r, 1));
                        desktop.put(new ViewRate(Engine.getRate(v), desktop, null));
                    }
                }
            }
        });
        return new JScrollPane(ct);
    }

    private RTextScrollPane notes() {

        RTextScrollPane notes = Elements.simpleEditor();
        notes.getTextArea().setText(purchaseOrder.getNotes());
        notes.getTextArea().setEditable(false);
        return notes;
    }
}