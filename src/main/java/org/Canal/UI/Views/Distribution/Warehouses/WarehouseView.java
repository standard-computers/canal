package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.UI.Views.Areas.AutoMakeAreasAndBins;
import org.Canal.UI.Views.Bins.CreateBin;
import org.Canal.UI.Views.Areas.CreateArea;
import org.Canal.UI.Views.Finance.Payments.IssuePayment;
import org.Canal.UI.Views.Inventory.InventoryView;
import org.Canal.UI.Views.Orders.ReceiveOrder;
import org.Canal.Utils.Locke;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

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

/**
 * /WHS/$[WAREHOUSE_ID]
 */
public class WarehouseView extends LockeState implements RefreshListener {

    private Warehouse warehouse;
    private JTree dataTree;
    private DesktopState desktop;

    public WarehouseView(Warehouse loc, DesktopState desktop) {
        super("Warehouse / " + loc.getId() + " - " + loc.getName(), "/WHS/$", true, true, true, true);
        this.warehouse = loc;
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(WarehouseView.class.getResource("/icons/warehouses.png")));
        setLayout(new BorderLayout());
        JPanel tb = createToolBar();
        add(tb, BorderLayout.NORTH);
        JPanel dataView = new JPanel(new BorderLayout());
        JScrollPane tableScrollPane = new JScrollPane(makeOverview());
        dataView.add(tableScrollPane, BorderLayout.CENTER);
        dataTree = createTree();
        dataTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath path = dataTree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        Locke orgNode = (Locke) node.getUserObject();
                        Engine.router(orgNode.getTransaction(), desktop);
                    }
                }
            }
        });
        JScrollPane treeScrollPane = new JScrollPane(dataTree);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, dataView);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.2);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel makeOverview(){
        JPanel kanbanBoard = new JPanel();
        kanbanBoard.setLayout(new GridLayout(1, 3, 10, 10));
        ArrayList<String[]> ibd = new ArrayList<>();
        for(PurchaseOrder ibdo : Engine.getOrders(warehouse.getId())){
            double c = 0;
            for(OrderLineItem oli : ibdo.getItems()){
                c += oli.getQuantity();
            }
            ibd.add(new String[]{
                    ibdo.getOrderId(),
                    c + " Total Items | $" + ibdo.getTotal() + " Total",
                    "Expected Delivery: " + ibdo.getExpectedDelivery(),
                    "Receive"
            });
        }
        ArrayList<String[]> obd = new ArrayList<>();
        ArrayList<String[]> ipt = new ArrayList<>();
        ArrayList<String[]> myt = new ArrayList<>();
        kanbanBoard.add(createColumn("Inbound Deliveries", ibd));
        kanbanBoard.add(createColumn("Outbound Deliveries", obd));
        kanbanBoard.add(createColumn("In Progress Tasks", ipt));
        kanbanBoard.add(createColumn("Your Tasks", myt));
        return kanbanBoard;
    }

    private JPanel createColumn(String title, ArrayList<String[]> tasks) {
        JPanel columnPanel = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleFont(UIManager.getFont("h3.font"));
        columnPanel.setBorder(titledBorder);
        columnPanel.setLayout(new BorderLayout());
        JPanel tasksContainer = new JPanel();
        tasksContainer.setLayout(new BoxLayout(tasksContainer, BoxLayout.Y_AXIS));
        for (String[] taskInfo : tasks) {
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
            JLabel primaryLabel = Elements.h3(taskInfo[0]);
            JLabel secondaryLabel = Elements.label(taskInfo[1]);
            JLabel tertiaryLabel = Elements.label(taskInfo[2]);
            taskPanel.add(primaryLabel);
            taskPanel.add(secondaryLabel);
            taskPanel.add(tertiaryLabel);
            JButton openButton = new JButton(taskInfo[3]);
            taskPanel.add(openButton);
            taskPanel.setBackground(UIManager.getColor("Panel.background").darker());
            taskPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            taskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, taskPanel.getPreferredSize().height));
            taskPanel.setMinimumSize(new Dimension(tasksContainer.getWidth(), taskPanel.getPreferredSize().height));
            tasksContainer.add(taskPanel);
            tasksContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        JScrollPane sp = new JScrollPane(tasksContainer, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(null);
        columnPanel.add(sp, BorderLayout.CENTER);
        return columnPanel;
    }


    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton order = new IconButton("Order", "create", "Order from a vendor");
        IconButton payBill = new IconButton("Pay Bill", "bill", "Receiving a bill from a vendor");
        IconButton inventory = new IconButton("Inventory", "inventory", "Inventory of items in cost center");
        IconButton receive = new IconButton("Receive", "receive", "Receive an Inbound Delivery");
        IconButton addArea = new IconButton("+ Area", "areas", "Add an area cost center");
        IconButton addBin = new IconButton("+ Bin", "bins", "Add an area cost center");
        IconButton autoMake = new IconButton("AutoMake Areas/Bins", "automake", "Make areas and bins from templates");
        IconButton label = new IconButton("", "label", "Print labels for properties");
        payBill.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new IssuePayment());
            }
        });
        inventory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new InventoryView(desktop, warehouse.getId()));
            }
        });
        receive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new ReceiveOrder(warehouse.getId(), desktop));
            }
        });
        addArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateArea(warehouse.getId(), WarehouseView.this));
            }
        });
        addBin.addMouseListener(new MouseAdapter() {
            @Override
           public void mouseClicked(MouseEvent e) {
               desktop.put(new CreateBin(warehouse.getId(), WarehouseView.this));
           }
        });
        autoMake.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new AutoMakeAreasAndBins());
            }
        });
        tb.add(order);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(payBill);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(inventory);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(receive);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addArea);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addBin);
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
        expandAllNodes(tree);
        return tree;
    }

    private Locke createRootNode() {
        Locke[] customers = new Locke[Engine.getCustomers(warehouse.getOrg()).size()];
        for (int i = 0; i < Engine.getCustomers(warehouse.getOrg()).size(); i++) {
            Location l = Engine.getCustomers(warehouse.getOrg()).get(i);
            customers[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/CSTS/" + l.getId(), Color.PINK, null);
        }
        Locke[] vendors = new Locke[Engine.getVendors(warehouse.getOrg()).size()];
        for (int i = 0; i < Engine.getVendors(warehouse.getOrg()).size(); i++) {
            Vendor l = Engine.getVendors(warehouse.getOrg()).get(i);
            vendors[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/VEND/" + l.getId(), Color.CYAN, null);
        }
        Locke[] items = new Locke[Engine.getItems(warehouse.getOrg()).size()];
        for (int i = 0; i < Engine.getItems(warehouse.getOrg()).size(); i++) {
            Item l = Engine.getItems(warehouse.getOrg()).get(i);
            items[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/ITS/" + l.getId(), new Color(147, 70, 3), null);
        }
        Locke[] areas = new Locke[Engine.getAreas(warehouse.getId()).size()];
        for (int i = 0; i < Engine.getAreas(warehouse.getId()).size(); i++) {
            Area l = Engine.getAreas(warehouse.getId()).get(i);
            areas[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/ITS/" + l.getId(), new Color(147, 70, 3), null);
        }
        return new Locke(warehouse.getId() + " - " + warehouse.getName(), UIManager.getIcon("FileView.fileIcon"), "/ORGS", new Locke[]{
                new Locke("Areas", UIManager.getIcon("FileView.fileIcon"), "/AREAS", areas),
                new Locke("Bins", UIManager.getIcon("FileView.fileIcon"), "/BNS", null),
                new Locke("Items", UIManager.getIcon("FileView.fileIcon"), "/ITS", items),
                new Locke("Customers", UIManager.getIcon("FileView.fileIcon"), "/CSTS", customers),
                new Locke("Materials", UIManager.getIcon("FileView.fileIcon"), "/MTS", null),
                new Locke("Orders", UIManager.getIcon("FileView.fileIcon"), "/ORDS", null),
                new Locke("Vendors", UIManager.getIcon("FileView.fileIcon"), "/VEND", vendors),
                new Locke("Employees", UIManager.getIcon("FileView.fileIcon"), "/EMPS", null),
                new Locke("Reports", UIManager.getIcon("FileView.fileIcon"), "/RPTS", new Locke[]{
                    new Locke("Annual Ledger Report", UIManager.getIcon("FileView.fileIcon"), "/RPTS/LGS/ANNUM", null),
                    new Locke("Monthly Ledger Report", UIManager.getIcon("FileView.fileIcon"), "/RPTS/MONTH", null),
                    new Locke("CC Ledger", UIManager.getIcon("FileView.fileIcon"), "/RPTS/CCS/LGS", null),
                    new Locke("Annual Labor", UIManager.getIcon("FileView.fileIcon"), "/RPTS/ANUM_LBR", null),
                    new Locke("Monthly Labor", UIManager.getIcon("FileView.fileIcon"), "/RPTS/MONTH_LBR", null),
                    new Locke("Daily Labor", UIManager.getIcon("FileView.fileIcon"), "/RPTS/DL_LBR", null),
                    new Locke("Current Inventory", UIManager.getIcon("FileView.fileIcon"), "/RPTS/CRNT_INV", null),
                    new Locke("Inventory Count", UIManager.getIcon("FileView.fileIcon"), "/RPTS/COUNT_INV", null),
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