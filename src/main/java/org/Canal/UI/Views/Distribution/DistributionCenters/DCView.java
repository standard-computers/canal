package org.Canal.UI.Views.Distribution.DistributionCenters;

import org.Canal.Models.BusinessUnits.OrderLineItem;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Models.SupplyChainUnits.Vendor;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Views.AreasBins.AutoMakeAreasAndBins;
import org.Canal.UI.Views.AreasBins.CreateBin;
import org.Canal.UI.Views.Orders.PurchaseOrders.CreatePurchaseOrder;
import org.Canal.UI.Views.Orders.ReceiveOrder;
import org.Canal.UI.Views.Finance.AcceptPayment;
import org.Canal.UI.Views.AreasBins.CreateArea;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /DCSS/$[DC_ID]
 */
public class DCView extends JInternalFrame implements RefreshListener {

    private Location distributionCenter;
    private JTree dataTree;
    private DesktopState desktop;

    public DCView(Location dc, DesktopState desktop) {
        super("", true, true, true, true);
        setTitle("DC / " + dc.getId() + " - " + dc.getName());
        this.distributionCenter = dc;
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(DCView.class.getResource("/icons/distribution_centers.png")));
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
                        desktop.put(Engine.router(orgNode.getTransaction(), desktop));
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
        for(PurchaseOrder ibdo : Engine.getOrders(distributionCenter.getId())){
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
            Button openButton = new Button(taskInfo[3]);
            openButton.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent e) {
                   ReceiveOrder ro = new ReceiveOrder(distributionCenter.getId(), desktop);
                   ro.setPO(taskInfo[0]);
                   desktop.put(ro);
               }
            });
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
        IconButton acceptPayment = new IconButton("Accept Payment", "order", "Receiving an order or taking payment");
        IconButton payBill = new IconButton("Pay Bill", "bill", "Receiving a bill from a vendor");
        IconButton inventory = new IconButton("Inventory", "inventory", "Inventory of items in cost center");
        IconButton receive = new IconButton("Receive", "receive", "Receive an Inbound Delivery");
        IconButton areas = new IconButton("+ Areas", "areas", "Add an area cost center");
        IconButton addBin = new IconButton("+ Bin", "bins", "Add an area cost center");
        IconButton autoMake = new IconButton("AutoMake Areas/Bins", "automake", "Make areas and bins from templates");
        IconButton label = new IconButton("Barcodes", "label", "Print labels for properties");
        IconButton refresh = new IconButton("", "refresh", "Reload from store");
        order.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseOrder());
            }
        });
        acceptPayment.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new AcceptPayment());
            }
        });
        receive.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new ReceiveOrder(distributionCenter.getId(), desktop));
            }
        });
        refresh.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Engine.load();
                Locke rootNode = createRootNode();
                DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
                DefaultTreeModel model = (DefaultTreeModel) dataTree.getModel();
                model.setRoot(rootTreeNode);
                revalidate();
                repaint();
            }
        });
        areas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateArea(distributionCenter.getId()));
            }
        });
        addBin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateBin(distributionCenter.getId()));
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
        tb.add(acceptPayment);
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
        tb.add(autoMake);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
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
        Locke[] customers = new Locke[Engine.getCustomers(distributionCenter.getTie()).size()];
        for (int i = 0; i < Engine.getCustomers(distributionCenter.getTie()).size(); i++) {
            Location l = Engine.getCustomers(distributionCenter.getTie()).get(i);
            customers[i] = new Locke(l.getId() + " - " + l.getName(), false, "/CSTS/" + l.getId(), Color.PINK, null);
        }
        Locke[] vendors = new Locke[Engine.getVendors(distributionCenter.getTie()).size()];
        for (int i = 0; i < Engine.getVendors(distributionCenter.getTie()).size(); i++) {
            Vendor l = Engine.getVendors(distributionCenter.getTie()).get(i);
            vendors[i] = new Locke(l.getId() + " - " + l.getName(), false, "/VEND/" + l.getId(), Color.CYAN, null);
        }
        Locke[] items = new Locke[Engine.getItems(distributionCenter.getTie()).size()];
        for (int i = 0; i < Engine.getItems(distributionCenter.getTie()).size(); i++) {
            Item l = Engine.getItems(distributionCenter.getTie()).get(i);
            items[i] = new Locke(l.getId() + " - " + l.getName(), false, "/ITS/" + l.getId(), new Color(147, 70, 3), null);
        }
        Locke[] areas = new Locke[Engine.getAreas(distributionCenter.getId()).size()];
        for (int i = 0; i < Engine.getAreas(distributionCenter.getId()).size(); i++) {
            Area l = Engine.getAreas(distributionCenter.getId()).get(i);
            areas[i] = new Locke(l.getId() + " - " + l.getName(), false, "/ITS/" + l.getId(), new Color(147, 70, 3), null);
        }
        return new Locke(distributionCenter.getId() + " - " + distributionCenter.getName(), true, "/ORGS", new Locke[]{
                new Locke("Areas", true, "/AREAS", areas),
                new Locke("Bins", true, "/BNS", null),
                new Locke("Items", true, "/ITS", items),
                new Locke("Customers", true, "/CSTS", customers),
                new Locke("Materials", true, "/MTS", null),
                new Locke("Orders", true, "/ORDS", null),
                new Locke("Vendors", true, "/VEND", vendors),
                new Locke("Employees", true, "/EMPS", null),
                new Locke("Reports", true, "/RPTS", new Locke[]{
                    new Locke("Annual Ledger Report", false, "/RPTS/LGS/ANNUM", null),
                    new Locke("Monthly Ledger Report", false, "/RPTS/MONTH", null),
                    new Locke("CC Ledger", false, "/RPTS/CCS/LGS", null),
                    new Locke("Annual Labor", false, "/RPTS/ANUM_LBR", null),
                    new Locke("Monthly Labor", false, "/RPTS/MONTH_LBR", null),
                    new Locke("Daily Labor", false, "/RPTS/DL_LBR", null),
                    new Locke("Current Inventory", false, "/RPTS/CRNT_INV", null),
                    new Locke("Inventory Count", false, "/RPTS/COUNT_INV", null),
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

    }

    static class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
            Locke orgNode = (Locke) treeNode.getUserObject();
            if (orgNode.getStatus()) {
                setIcon(UIManager.getIcon("FileView.directoryIcon"));
            } else {
                setIcon(UIManager.getIcon("FileView.fileIcon"));
            }
            setForeground(orgNode.getColor());
            return component;
        }
    }
}