package org.Canal.UI.Views.Distribution.DistributionCenters;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.UI.Views.Areas.AutoMakeAreas;
import org.Canal.UI.Views.Bins.AutoMakeBins;
import org.Canal.UI.Views.Bins.CreateBin;
import org.Canal.UI.Views.Inventory.InventoryView;
import org.Canal.UI.Views.Orders.ReceiveOrder;
import org.Canal.UI.Views.Areas.CreateArea;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
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
 * /DCSS/$[DC_ID]
 */
public class DistributionCenterView extends LockeState implements RefreshListener {

    private Location location;
    private JTree dataTree;
    private DesktopState desktop;

    public DistributionCenterView(Location location, DesktopState desktop) {
        super("DC / " + location.getId() + " – " + location.getName(), "/DCSS/$", true, true, true, true);
        this.location = location;
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(DistributionCenterView.class.getResource("/icons/distribution_centers.png")));
        setLayout(new BorderLayout());
        JPanel tb = createToolBar();
        add(tb, BorderLayout.NORTH);
        JPanel dataView = new JPanel(new BorderLayout());
        JScrollPane tableScrollPane = new JScrollPane(makeOverview());
        dataView.add(tableScrollPane, BorderLayout.CENTER);
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
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, dataView);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.2);
        add(splitPane, BorderLayout.CENTER);
        isMaximized();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> onRefresh();
        scheduler.scheduleAtFixedRate(task, 60, 30, TimeUnit.SECONDS);
    }

    private JPanel makeOverview(){
        JPanel kanbanBoard = new JPanel();
        kanbanBoard.setLayout(new GridLayout(1, 3, 10, 10));
        ArrayList<JComponent[]> ibd = new ArrayList<>();
        if((boolean) Engine.codex("ORDS/PO", "use_deliveries")){
            for(Delivery ibdo : Engine.getInboundDeliveries(location.getId())){
                ibd.add(new JComponent[]{
                        Elements.link(ibdo.getId(), "Click to copy"),
                        new JLabel("PO" + ibdo.getPurchaseOrder() ),
                        new JLabel("$" + ibdo.getTotal() + " Total Value"),
                        new JLabel("Expected Delivery: " + ibdo.getExpectedDelivery()),
                        Elements.button("Receive")
                });
            }
        }else{
            for(PurchaseOrder ibdo : Engine.getOrders(location.getId(), LockeStatus.NEW)){
                double c = 0;
                for(OrderLineItem oli : ibdo.getItems()){
                    c += oli.getQuantity();
                }
                ibd.add(new JComponent[]{
                        Elements.link(ibdo.getOrderId(), ""),
                        new JLabel(c + " Total Items | $" + ibdo.getTotal() + " Total"),
                        new JLabel("Expected Delivery: " + ibdo.getExpectedDelivery()),
                        Elements.button("Receive")
                });
            }
        }
        ArrayList<JComponent[]> obd = new ArrayList<>();
        ArrayList<JComponent[]> ipt = new ArrayList<>();
        ArrayList<JComponent[]> myt = new ArrayList<>();
        kanbanBoard.add(createColumn("Inbound Deliveries", ibd));
        kanbanBoard.add(createColumn("Outbound Deliveries", obd));
        kanbanBoard.add(createColumn("In Progress Tasks", ipt));
        kanbanBoard.add(createColumn("Your Tasks", myt));
        return kanbanBoard;
    }

    private JPanel createColumn(String title, ArrayList<JComponent[]> tasks) {
        JPanel columnPanel = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleFont(UIManager.getFont("h3.font"));
        columnPanel.setBorder(titledBorder);
        columnPanel.setLayout(new BorderLayout());
        JPanel tasksContainer = new JPanel();
        tasksContainer.setLayout(new GridLayout(tasks.size() - 1, BoxLayout.Y_AXIS));
        for (JComponent[] taskInfo : tasks) {
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
            for (int i = 0; i < taskInfo.length; i++) {
                taskInfo[i].setAlignmentX(Component.LEFT_ALIGNMENT); // Align components to the left
                taskPanel.add(taskInfo[i]);
            }
            taskPanel.setBackground(UIManager.getColor("Panel.background").darker());
            taskPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//            taskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, taskPanel.getPreferredSize().height));
            taskPanel.setMinimumSize(new Dimension(tasksContainer.getWidth(), taskPanel.getPreferredSize().height));
            tasksContainer.add(taskPanel);
            tasksContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        JScrollPane sp = new JScrollPane(tasksContainer, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        columnPanel.add(sp, BorderLayout.CENTER);
        return columnPanel;
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton order = new IconButton("Order", "create", "Order from a vendor", "/ORDS/PO/NEW");
        IconButton sell = new IconButton("Sell", "create", "Sell from this DC", "/ORDS/SO/NEW");
        IconButton payBill = new IconButton("Pay Bill", "bill", "Receiving a bill from a vendor", "/FI/PYMNTS");
        IconButton inventory = new IconButton("Inventory", "inventory", "Inventory of items in cost center");
        IconButton receive = new IconButton("Receive", "receive", "Receive an Inbound Delivery");
        IconButton areas = new IconButton("+ Areas", "areas", "Add an area cost center");
        IconButton addBin = new IconButton("+ Bin", "bins", "Add an area cost center");
        IconButton autoMakeAreas = new IconButton("AutoMake Areas", "automake", "Automate the creation of areas");
        IconButton autoMakeBins = new IconButton("AutoMake Bins", "automake", "Automate the creation of bins");
        IconButton label = new IconButton("", "label", "Print labels for properties");
        inventory.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                desktop.put(new InventoryView(desktop, location.getId()));
            }
        });
        receive.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new ReceiveOrder(location.getId(), desktop));
            }
        });
        areas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateArea(location.getId(), DistributionCenterView.this));
            }
        });
        addBin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateBin(location.getId(), DistributionCenterView.this));
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
        tb.add(payBill);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(inventory);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(receive);
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
        Locke[] items = new Locke[Engine.getItems(location.getOrganization()).size()];
        for (int i = 0; i < Engine.getItems(location.getOrganization()).size(); i++) {
            Item l = Engine.getItems(location.getOrganization()).get(i);
            items[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/ITS/" + l.getId(), Constants.colors[4], null);
        }
        return new Locke(location.getName(), UIManager.getIcon("FileView.fileIcon"), "/DCSS/" + location.getId(), new Locke[]{
                new Locke("Areas", UIManager.getIcon("FileView.fileIcon"), "/AREAS", areas),
                new Locke("Bins", UIManager.getIcon("FileView.fileIcon"), "/BNS", bins),
                new Locke("Items", UIManager.getIcon("FileView.fileIcon"), "/ITS", items),
                new Locke("Customers", UIManager.getIcon("FileView.fileIcon"), "/CSTS", customers),
                new Locke("Materials", UIManager.getIcon("FileView.fileIcon"), "/MTS", null),
                new Locke("Orders", UIManager.getIcon("FileView.fileIcon"), "/ORDS", null),
                new Locke("Vendors", UIManager.getIcon("FileView.fileIcon"), "/VEND", vendors),
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
    public void onRefresh() {
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