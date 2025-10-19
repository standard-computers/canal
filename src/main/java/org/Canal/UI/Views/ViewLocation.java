package org.Canal.UI.Views;

import org.Canal.Models.BusinessUnits.Inventory;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Areas.AutoMakeAreas;
import org.Canal.UI.Views.Bins.AutoMakeBins;
import org.Canal.UI.Views.Bins.CreateBin;
import org.Canal.UI.Views.SalesOrders.CreateSalesOrder;
import org.Canal.UI.Views.Inventory.ViewInventory;
import org.Canal.UI.Views.Areas.CreateArea;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * /$/$[DC_ID]
 */
public class ViewLocation extends LockeState implements RefreshListener {

    private Location location;
    private JTree dataTree;
    private DesktopState desktop;

    public ViewLocation(Location location, DesktopState desktop) {

        super(location.getId() + " – " + location.getName(), "/" + location.getType() + "/" + location.getId());
        setFrameIcon(new ImageIcon(ViewLocation.class.getResource("/icons/windows/" + Engine.codex(location.getType().replace("/", ""), "icon") + ".png")));
        this.location = location;
        this.desktop = desktop;
        Engine.setLocation(location.getId());

        setLayout(new BorderLayout());
        JPanel tb = toolbar();
        add(tb, BorderLayout.NORTH);
        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Dashboard", dashboard());
        tabs.addTab("Inbound Deliveries", inboundDeliveries());
        tabs.addTab("Outbound Deliveries", outboundDeliveries());
        tabs.addTab("Open Tasks", openTasks());
        tabs.addTab("Pending Tasks", pendingTasks());
        tabs.addTab("Events", events());
        dataTree = createTree();
        dataTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath path = dataTree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        Locke orgNode = (Locke) node.getUserObject();
                        desktop.put(Engine.router(orgNode.getTransaction(), desktop));
                    }
                }
            }
        });
        JScrollPane treeScrollPane = new JScrollPane(dataTree);
        JPanel holder = new JPanel(new BorderLayout());
        holder.add(Elements.header("Manage " + location.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        holder.add(tabs, BorderLayout.CENTER);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, holder);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.2);
        add(splitPane, BorderLayout.CENTER);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> refresh();
        scheduler.scheduleAtFixedRate(task, 60, 30, TimeUnit.SECONDS);
        setMaximized(true);
    }

    private JScrollPane dashboard(){
        JScrollPane scrollPane = new JScrollPane();

        return scrollPane;
    }

    private JScrollPane inboundDeliveries(){
        String[] columns = new String[]{
                "ID",
                "Description",
                "Type",
                "Sales Order",
                "Pur. Order",
                "Exp. Delivery",
                "Origin",
                "Destination",
                "Destination Name",
                "Dest. Area",
                "Dest. Door",
                "Value",
                "Truck ID",
                "Pallet Cnt.",
                "Status",
                "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Delivery delivery : Engine.getInboundDeliveries(location.getId())) {
            if(!delivery.getStatus().equals(LockeStatus.DELIVERED)) {
                data.add(new Object[]{
                        delivery.getId(),
                        delivery.getName(),
                        delivery.getType(),
                        delivery.getSalesOrder(),
                        delivery.getPurchaseOrder(),
                        delivery.getExpectedDelivery(),
                        delivery.getOrigin(),
                        delivery.getDestination(),
                        "TBD",
                        delivery.getDestinationArea(),
                        delivery.getDestinationDoor(),
                        delivery.getTotal(),
                        "",
                        delivery.getPallets().size(),
                        delivery.getStatus(),
                        delivery.getCreated()
                });
            }
        }
        CustomTable ibds = new CustomTable(columns, data);
        return new JScrollPane(ibds);
    }

    private JScrollPane outboundDeliveries(){
        String[] columns = new String[]{
                "ID",
                "Description",
                "Type",
                "Sales Order",
                "Pur. Order",
                "Exp. Delivery",
                "Origin",
                "Destination",
                "Destination Name",
                "Dest. Area",
                "Dest. Door",
                "Value",
                "Truck ID",
                "Pallet Cnt.",
                "Status",
                "Created"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Delivery delivery : Engine.getOutboundDeliveries(location.getId())) {
            if(!delivery.getStatus().equals(LockeStatus.DELIVERED) && !delivery.getStatus().equals(LockeStatus.FULFILLED)) {
                data.add(new Object[]{
                        delivery.getId(),
                        delivery.getName(),
                        delivery.getType(),
                        delivery.getSalesOrder(),
                        delivery.getPurchaseOrder(),
                        delivery.getExpectedDelivery(),
                        delivery.getOrigin(),
                        delivery.getDestination(),
                        Engine.getLocation(delivery.getDestination(), "CCS").getName(),
                        delivery.getDestinationArea(),
                        delivery.getDestinationDoor(),
                        delivery.getTotal(),
                        "",
                        delivery.getPallets().size(),
                        delivery.getStatus(),
                        delivery.getCreated()
                });
            }
        }
        CustomTable obds = new CustomTable(columns, data);
        return new JScrollPane(obds);
    }

    private JPanel openTasks(){
        JPanel p = new JPanel(new BorderLayout());

        return p;
    }

    private JPanel pendingTasks(){
        JPanel p = new JPanel(new BorderLayout());

        return p;
    }

    private JPanel events(){
        JPanel p = new JPanel(new BorderLayout());

        return p;
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton order = new IconButton("Order", "create", "Order from a vendor", "/ORDS/PO/NEW");
        tb.add(order);
        tb.add(Box.createHorizontalStrut(5));

        IconButton sell = new IconButton("Sell", "create", "Sell from this DC", "/ORDS/SO/NEW");
        sell.addActionListener(_ -> {
            CreateSalesOrder cso = new CreateSalesOrder(desktop);
            desktop.put(cso);
        });
        tb.add(sell);
        tb.add(Box.createHorizontalStrut(5));

        IconButton inventory = new IconButton("Inventory", "inventory", "Inventory of items in cost center");
        inventory.addActionListener(_ -> {
           Inventory i = Engine.getInventory(location.getId());
           if(i == null){
               System.out.println("Initializing Inventory");
               i = new Inventory();
               i.setLocation(location.getId());
               i.setId(Constants.generateId(6));
               Pipe.save("/STK", i);
           }
           desktop.put(new ViewInventory(desktop, location.getId()));
        });
        inventory.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        tb.add(inventory);
        tb.add(Box.createHorizontalStrut(5));

        IconButton receive = new IconButton("Receive", "receive", "Receive an Inbound Delivery");
        receive.addActionListener(_ -> desktop.put(new ReceiveOrder(location.getId(), desktop, this)));
        tb.add(receive);
        tb.add(Box.createHorizontalStrut(5));

        IconButton fulfill = new IconButton("Fulfill", "fulfill", "Fulfill Order");
        fulfill.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new FulfillOrder(location.getId()));
            }
        });
        tb.add(fulfill);
        tb.add(Box.createHorizontalStrut(5));

        IconButton areas = new IconButton("+ Areas", "areas", "Add an area cost center", "/AREAS/NEW");
        areas.addActionListener(_ -> desktop.put(new CreateArea(location.getId(), desktop, ViewLocation.this)));
        tb.add(areas);
        tb.add(Box.createHorizontalStrut(5));

        IconButton addBin = new IconButton("+ Bin", "bins", "Add an area cost center", "/BNS/NEW");
        addBin.addActionListener(_ -> desktop.put(new CreateBin(location.getId(), desktop, ViewLocation.this)));
        tb.add(addBin);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeAreas = new IconButton("AutoMake Areas", "automake", "Automate the creation of areas", "/AREAS/AUTO_MK");
        autoMakeAreas.addActionListener(_ -> desktop.put(new AutoMakeAreas(desktop, null)));
        tb.add(autoMakeAreas);
        tb.add(Box.createHorizontalStrut(5));

        IconButton autoMakeBins = new IconButton("AutoMake Bins", "automake", "Automate the creation of bins", "/BNS/AUTO_MK");
        autoMakeBins.addActionListener(_ -> desktop.put(new AutoMakeBins(desktop)));
        tb.add(autoMakeBins);
        tb.add(Box.createHorizontalStrut(5));

        IconButton label = new IconButton("Labels", "label", "Print labels for properties");
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));

        IconButton modify = new IconButton("Modify", "modify", "Modify this location", "/" + location.getType() + "/MOD");
        modify.addActionListener(_ -> desktop.put(new ModifyLocation(location, null)));
        tb.add(modify);
        tb.add(Box.createHorizontalStrut(5));

        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh data");
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }

    private JTree createTree() {

        Locke rootNode = createRootNode();
        DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootTreeNode);
        JTree tree = new JTree(treeModel);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        return tree;
    }

    private Locke createRootNode() {

        return new Locke(location.getName(), UIManager.getIcon("FileView.fileIcon"), "/DCSS/" + location.getId(), new Locke[]{
                new Locke("Orders", UIManager.getIcon("FileView.fileIcon"), "/ORDS", null),
                new Locke("Stock", UIManager.getIcon("FileView.directoryIcon"), "/STK", new Locke[] {
                        new Locke("Move Stock Internally", UIManager.getIcon("FileView.fileIcon"), "/STK/MOD/MOV", null),
                        new Locke("Move Bin to Bin", UIManager.getIcon("FileView.fileIcon"), "/STK/MV/BB", null),
                        new Locke("Move Qty to Bin", UIManager.getIcon("FileView.fileIcon"), "/STK/MV/BN", null),
                        new Locke("Move HU to Bin", UIManager.getIcon("FileView.fileIcon"), "/STK/MV/FULL", null),
                        new Locke("Directed Move HU to Bin", UIManager.getIcon("FileView.fileIcon"), "/STK/MV/DFULL", null),
                }),
                new Locke("Orders", UIManager.getIcon("FileView.fileIcon"), "/ORDS", new Locke[] {
                        new Locke("Orders", UIManager.getIcon("FileView.fileIcon"), "/ORDS", null)
                }),
                new Locke("Bins", UIManager.getIcon("FileView.fileIcon"), "/ORDS", new Locke[] {
                        new Locke("Orders", UIManager.getIcon("FileView.fileIcon"), "/ORDS", null)
                }),
        });
    }

    private DefaultMutableTreeNode createTreeNodes(Locke node) {

        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node);
        if (node.getChildren() != null) {
            for (Locke child : node.getChildren()) {
                treeNode.add(createTreeNodes(child));
            }
        }
        return treeNode;
    }

    @Override
    public void refresh() {

        Locke rootNode = createRootNode();
        DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
        DefaultTreeModel model = (DefaultTreeModel) dataTree.getModel();
        model.setRoot(rootTreeNode);
//        expandAllNodes(dataTree);
        revalidate();
        repaint();
    }

    static class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
            Locke orgNode = (Locke) treeNode.getUserObject();
            setIcon(orgNode.getIcon());
            return component;
        }
    }

    private void expandAllNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}