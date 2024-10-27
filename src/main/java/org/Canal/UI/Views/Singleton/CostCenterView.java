package org.Canal.UI.Views.Singleton;

import org.Canal.Models.SupplyChainUnits.Area;
import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Views.New.*;
import org.Canal.UI.Views.Transactions.Orders.CreatePurchaseOrder;
import org.Canal.Utils.Canal;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CostCenterView extends JInternalFrame implements RefreshListener {

    private Location thisCostCenter;
    private JTree dataTree;
    private DesktopState desktop;

    public CostCenterView(Location loc, DesktopState desktop) {
        this.thisCostCenter = loc;
        this.desktop = desktop;
        setTitle("Cost Center / " + loc.getId() + " - " + loc.getName());
        setLayout(new BorderLayout());
        JPanel tb = createToolBar();
        JScrollPane tbHolder = new JScrollPane(tb);
        tbHolder.setPreferredSize(new Dimension(600, 60));
        add(tbHolder, BorderLayout.NORTH);
        JTable table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        JPanel dataView = new JPanel(new BorderLayout());
        JTextField cmd = new JTextField("/CCS/" + thisCostCenter.getId());
        dataView.add(cmd, BorderLayout.NORTH);
        dataView.add(tableScrollPane, BorderLayout.CENTER);
        dataTree = createTree();
        dataTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath path = dataTree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        Canal orgNode = (Canal) node.getUserObject();
                        desktop.put(Engine.router(orgNode.getTransaction(), desktop));
                    }
                }
            }
        });
        JScrollPane treeScrollPane = new JScrollPane(dataTree);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, dataView);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.3);
        add(splitPane, BorderLayout.CENTER);
        setSize(800, 600);
        setIconifiable(true);
        setClosable(true);
        setResizable(true);
        setMaximizable(true);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton order = new IconButton("Order", "create", "Order from a vendor");
        IconButton payBill = new IconButton("Pay Bill", "bill", "Receiving a bill from a vendor");
        IconButton inventory = new IconButton("Inventory", "inventory", "Inventory of items in cost center");
        IconButton addArea = new IconButton("+ Area", "areas", "Add an area cost center");
        IconButton addBin = new IconButton("+ Bin", "bins", "Add an area cost center");
        IconButton autoMake = new IconButton("Auto Make Areas/Bins", "auto_bin", "Make areas and bins from templates");
        IconButton batch = new IconButton("Make Areas/Bins", "batch", "Add as csv");
        IconButton pos = new IconButton("POS Mode", "pos", "Launch Point-of-Sale");
        IconButton label = new IconButton("", "label", "Print labels for properties");
        IconButton refresh = new IconButton("", "refresh", "Reload from store");
        order.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseOrder());
            }
        });
        addArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateArea(desktop, thisCostCenter));
            }
        });
        autoMake.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new AutoMake());
            }
        });
        batch.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                desktop.put(new BatchCreateLocations("Areas", "/AREAS", thisCostCenter.getId()));
            }
        });
        refresh.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            Engine.load();
            Canal rootNode = createRootNode();
            DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
            DefaultTreeModel model = (DefaultTreeModel) dataTree.getModel();
            model.setRoot(rootTreeNode);
            revalidate();
            repaint();
            }
        });
        tb.add(Box.createHorizontalStrut(8));
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
        tb.add(batch);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(pos);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.add(Box.createHorizontalStrut(8));
        return tb;
    }

    private JTable createTable() {
        String[] columns = new String[]{"Property", "Value"};
        String[][] data = {
                {"Id", thisCostCenter.getId()},
                {"Tie", thisCostCenter.getTie()},
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
        Canal rootNode = createRootNode();
        DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootTreeNode);
        JTree tree = new JTree(treeModel);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        return tree;
    }

    private Canal createRootNode() {

        Canal[] customers = new Canal[Engine.getCustomers(thisCostCenter.getTie()).size()];
        for (int i = 0; i < Engine.getCustomers(thisCostCenter.getTie()).size(); i++) {
            Location l = Engine.getCustomers(thisCostCenter.getTie()).get(i);
            customers[i] = new Canal(l.getId() + " - " + l.getName(), false, "/CSTS/" + l.getId(), Color.PINK, null);
        }
        Canal[] vendors = new Canal[Engine.getVendors(thisCostCenter.getTie()).size()];
        for (int i = 0; i < Engine.getVendors(thisCostCenter.getTie()).size(); i++) {
            Location l = Engine.getVendors(thisCostCenter.getTie()).get(i);
            vendors[i] = new Canal(l.getId() + " - " + l.getName(), false, "/VEND/" + l.getId(), Color.CYAN, null);
        }
        Canal[] items = new Canal[Engine.getItems(thisCostCenter.getTie()).size()];
        for (int i = 0; i < Engine.getItems(thisCostCenter.getTie()).size(); i++) {
            Item l = Engine.getItems(thisCostCenter.getTie()).get(i);
            items[i] = new Canal(l.getId() + " - " + l.getName(), false, "/ITS/" + l.getId(), new Color(147, 70, 3), null);
        }
        Canal[] areas = new Canal[Engine.getAreas(thisCostCenter.getId()).size()];
        for (int i = 0; i < Engine.getAreas(thisCostCenter.getId()).size(); i++) {
            Area l = Engine.getAreas(thisCostCenter.getId()).get(i);
            areas[i] = new Canal(l.getId() + " - " + l.getValue("name"), false, "/ITS/" + l.getId(), new Color(147, 70, 3), null);
        }
        return new Canal(thisCostCenter.getId() + " - " + thisCostCenter.getName(), true, "/ORGS", new Canal[]{
                new Canal("Areas", true, "/AREAS", areas),
                new Canal("Bins", true, "/BNS", null),
                new Canal("Items", true, "/ITS", items),
                new Canal("Customers", true, "/CSTS", customers),
                new Canal("Materials", true, "/MTS", null),
                new Canal("Orders", true, "/ORDS", null),
                new Canal("Vendors", true, "/VEND", vendors),
                new Canal("Employees", true, "/EMPS", null),
                new Canal("Reports", true, "/RPTS", new Canal[]{
                    new Canal("Annual Ledger Report", false, "/RPTS/LGS/ANNUM", null),
                    new Canal("Monthly Ledger Report", false, "/RPTS/MONTH", null),
                    new Canal("CC Ledger", false, "/RPTS/CCS/LGS", null),
                    new Canal("Annual Labor", false, "/RPTS/ANUM_LBR", null),
                    new Canal("Monthly Labor", false, "/RPTS/MONTH_LBR", null),
                    new Canal("Daily Labor", false, "/RPTS/DL_LBR", null),
                    new Canal("Current Inventory", false, "/RPTS/CRNT_INV", null),
                    new Canal("Inventory Count", false, "/RPTS/COUNT_INV", null),
                }),
        });
    }

    private DefaultMutableTreeNode createTreeNodes(Canal node) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node);
        if (node.getChildren() != null) {
            for (Canal child : node.getChildren()) {
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
            Canal orgNode = (Canal) treeNode.getUserObject();
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