package org.Canal.UI.Views;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Areas.AutoMakeAreas;
import org.Canal.UI.Views.Bins.AutoMakeBins;
import org.Canal.UI.Views.Bins.CreateBin;
import org.Canal.UI.Views.Finance.SalesOrders.CreateSalesOrder;
import org.Canal.UI.Views.Inventory.ViewInventory;
import org.Canal.UI.Views.Areas.CreateArea;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        super(location.getId() + " – " + location.getName(), location.getType() + "/$", true, true, true, true);
        this.location = location;
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(ViewLocation.class.getResource("/icons/" + Engine.codex(location.getType().replace("/", ""), "icon") + ".png")));
        setLayout(new BorderLayout());
        JPanel tb = toolbar();
        add(tb, BorderLayout.NORTH);
        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Inbound Deliveries", new ImageIcon(ViewLocation.class.getResource("/icons/inbound.png")), inboundDeliveries());
        tabs.addTab("Outbound Deliveries", new ImageIcon(ViewLocation.class.getResource("/icons/outbound.png")), outboundDeliveries());
        tabs.addTab("Open Tasks", openTasks());
        tabs.addTab("Pending Tasks", pendingTasks());
        tabs.addTab("Events", events());
        dataTree = createTree();
        expandAllNodes(dataTree);
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
        isMaximized();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> refresh();
        scheduler.scheduleAtFixedRate(task, 60, 30, TimeUnit.SECONDS);
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
        IconButton order = new IconButton("Order", "create", "Order from a vendor", "/ORDS/PO/NEW");
        IconButton sell = new IconButton("Sell", "create", "Sell from this DC");
        IconButton inventory = new IconButton("Inventory", "inventory", "Inventory of items in cost center");
        IconButton receive = new IconButton("Receive", "receive", "Receive an Inbound Delivery");
        IconButton fulfill = new IconButton("Fulfill", "fulfill", "Fulfill Order");
        IconButton areas = new IconButton("+ Areas", "areas", "Add an area cost center");
        IconButton addBin = new IconButton("+ Bin", "bins", "Add an area cost center");
        IconButton autoMakeAreas = new IconButton("AutoMake Areas", "automake", "Automate the creation of areas");
        IconButton autoMakeBins = new IconButton("AutoMake Bins", "automake", "Automate the creation of bins");
        IconButton label = new IconButton("Labels", "label", "Print labels for properties");
        sell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CreateSalesOrder cso = new CreateSalesOrder();
                cso.setSelectedSupplier(location.getId());
                desktop.put(cso);
            }
        });
        inventory.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                desktop.put(new ViewInventory(desktop, location.getId()));
            }
        });
        receive.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new ReceiveOrder(location.getId(), desktop));
            }
        });
        fulfill.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new FulfillOrder(location.getId()));
            }
        });
        areas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateArea(location.getId(), ViewLocation.this));
            }
        });
        addBin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateBin(location.getId(), ViewLocation.this));
            }
        });
        autoMakeAreas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new AutoMakeAreas());
            }
        });
        autoMakeBins.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new AutoMakeBins());
            }
        });
        tb.add(order);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(sell);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(inventory);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(receive);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(fulfill);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(areas);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addBin);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMakeAreas);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMakeBins);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
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
        Locke[] areas = new Locke[Engine.getAreas(location.getId()).size()];
        for (int i = 0; i < Engine.getAreas(location.getId()).size(); i++) {
            Area l = Engine.getAreas(location.getId()).get(i);
            areas[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/ITS/" + l.getId(), Constants.colors[0], null);
        }
        int binCount = 0;
        ArrayList<Bin> bs = new ArrayList<>();
        for(Area a : Engine.getAreas(location.getId())){
            binCount += a.getBins().size();
            for(Bin b : a.getBins()){
                bs.add(b);
            }
        }
        Locke[] bins = new Locke[binCount];
        for (int i = 0; i < bs.size(); i++) {
            Bin b = bs.get(i);
            bins[i] = new Locke(b.getId() + " - " + b.getName(), UIManager.getIcon("FileView.fileIcon"), "/BNS/" + b.getId(), Constants.colors[1], null);
        }
        Locke[] customers = new Locke[Engine.getLocations(location.getOrganization(), "CSTS").size()];
        for (int i = 0; i < Engine.getLocations(location.getOrganization(), "CSTS").size(); i++) {
            Location l = Engine.getLocations(location.getOrganization(), "CSTS").get(i);
            customers[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/CSTS/" + l.getId(), Constants.colors[2], null);
        }
        Locke[] vendors = new Locke[Engine.getLocations(location.getOrganization(), "DCSS").size()];
        for (int i = 0; i < Engine.getLocations(location.getOrganization(), "DCSS").size(); i++) {
            Location l = Engine.getLocations(location.getOrganization(), "DCSS").get(i);
            vendors[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/VEND/" + l.getId(), Constants.colors[3], null);
        }
        Locke[] employees = new Locke[Engine.getEmployees(location.getId()).size()];
        for (int i = 0; i < Engine.getEmployees(location.getId()).size(); i++) {
            Employee e = Engine.getEmployees(location.getId()).get(i);
            employees[i] = new Locke(e.getId() + " - " + e.getName(), UIManager.getIcon("FileView.fileIcon"), "/VEND/" + e.getId(), Constants.colors[3], null);
        }
        Locke[] items = new Locke[Engine.products.getItems(location.getOrganization()).size()];
        for (int i = 0; i < Engine.products.getItems(location.getOrganization()).size(); i++) {
            Item l = Engine.products.getItems(location.getOrganization()).get(i);
            items[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/ITS/" + l.getId(), Constants.colors[4], null);
        }
        return new Locke(location.getName(), UIManager.getIcon("FileView.fileIcon"), "/DCSS/" + location.getId(), new Locke[]{
                new Locke("Areas", UIManager.getIcon("FileView.fileIcon"), "/AREAS", areas),
                new Locke("Bins", UIManager.getIcon("FileView.fileIcon"), "/BNS", bins),
                new Locke("Customers", UIManager.getIcon("FileView.fileIcon"), "/CSTS", customers),
                new Locke("Orders", UIManager.getIcon("FileView.fileIcon"), "/ORDS", null),
                new Locke("Vendors", UIManager.getIcon("FileView.fileIcon"), "/VEND", employees),
                new Locke("People", UIManager.getIcon("FileView.fileIcon"), "/EMPS", employees),
                new Locke("Items", UIManager.getIcon("FileView.fileIcon"), "/ITS", items),
                new Locke("Materials", UIManager.getIcon("FileView.fileIcon"), "/MTS", null),
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
        expandAllNodes(dataTree);
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
            setForeground(orgNode.getColor());
            return component;
        }
    }

    private void expandAllNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}