package org.Canal.UI.Views.Finance.CostCenters;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.UI.Views.Areas.CreateArea;
import org.Canal.UI.Views.Bins.CreateBin;
import org.Canal.UI.Views.Orders.PurchaseOrders.CreatePurchaseOrder;
import org.Canal.UI.Views.Areas.AutoMakeAreas;
import org.Canal.Utils.Locke;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CCS/$[COST_CENTER_ID]
 */
public class CostCenterView extends LockeState implements RefreshListener {

    private Location thisCostCenter;
    private JTree dataTree;
    private DesktopState desktop;

    public CostCenterView(Location loc, DesktopState desktop) {
        super("Cost Center / " + loc.getId() + " - " + loc.getName(), "/CCS/$", true, true, true, true);
        this.thisCostCenter = loc;
        this.desktop = desktop;
        setLayout(new BorderLayout());
        JPanel tb = createToolBar();
        add(tb, BorderLayout.NORTH);
        JTable table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        JPanel dataView = new JPanel(new BorderLayout());
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
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton order = new IconButton("Order", "create", "Order from a vendor");
        IconButton payBill = new IconButton("Pay Bill", "bill", "Receiving a bill from a vendor");
        IconButton inventory = new IconButton("Inventory", "inventory", "Inventory of items in cost center");
        IconButton addArea = new IconButton("+ Area", "areas", "Add an area cost center");
        IconButton addBin = new IconButton("+ Bin", "bins", "Add an area cost center");
        IconButton autoMake = new IconButton("Auto Make Areas/Bins", "automake", "Make areas and bins from templates");
        IconButton pos = new IconButton("POS", "pos", "Launch Point-of-Sale");
        IconButton label = new IconButton("", "label", "Print labels for properties");
        order.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseOrder());
            }
        });
        addArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateArea(thisCostCenter.getId(), CostCenterView.this));
            }
        });
        addBin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateBin(thisCostCenter.getId(), CostCenterView.this));
            }
        });
        autoMake.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new AutoMakeAreas());
            }
        });
        tb.add(order);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(payBill);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(inventory);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addArea);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addBin);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(autoMake);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(pos);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    private JTable createTable() {
        String[] columns = new String[]{"Property", "Value"};
        String[][] data = {
                {"Id", thisCostCenter.getId()},
                {"Tie", thisCostCenter.getOrganization()},
                {"Name", thisCostCenter.getName()},
                {"Address Line 1", thisCostCenter.getLine1()},
                {"City", thisCostCenter.getCity()},
                {"State", thisCostCenter.getState()},
                {"Postal", thisCostCenter.getPostal()},
                {"Country", thisCostCenter.getCountry()},
                {"Tax Exempt Status", String.valueOf(thisCostCenter.isTaxExempt())}
        };
        JTable table = new JTable(data, columns);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Engine.adjustColumnWidths(table);
        return table;
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

        Locke[] customers = new Locke[Engine.getLocations(thisCostCenter.getOrganization(), "CSTS").size()];
        for (int i = 0; i < Engine.getLocations(thisCostCenter.getOrganization(), "CSTS").size(); i++) {
            Location l = Engine.getLocations(thisCostCenter.getOrganization(), "CSTS").get(i);
            customers[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/CSTS/" + l.getId(), Color.PINK, null);
        }
        Locke[] vendors = new Locke[Engine.getLocations(thisCostCenter.getOrganization(), "VEND").size()];
        for (int i = 0; i < Engine.getLocations(thisCostCenter.getOrganization(), "VEND").size(); i++) {
            Location l = Engine.getLocations(thisCostCenter.getOrganization(), "VEND").get(i);
            vendors[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/VEND/" + l.getId(), Color.CYAN, null);
        }
        Locke[] items = new Locke[Engine.getItems(thisCostCenter.getOrganization()).size()];
        for (int i = 0; i < Engine.getItems(thisCostCenter.getOrganization()).size(); i++) {
            Item l = Engine.getItems(thisCostCenter.getOrganization()).get(i);
            items[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/ITS/" + l.getId(), new Color(147, 70, 3), null);
        }
        Locke[] areas = new Locke[Engine.getAreas(thisCostCenter.getId()).size()];
        for (int i = 0; i < Engine.getAreas(thisCostCenter.getId()).size(); i++) {
            Area l = Engine.getAreas(thisCostCenter.getId()).get(i);
            areas[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/ITS/" + l.getId(), new Color(147, 70, 3), null);
        }
        return new Locke(thisCostCenter.getId() + " - " + thisCostCenter.getName(), UIManager.getIcon("FileView.fileIcon"), "/ORGS", new Locke[]{
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
}