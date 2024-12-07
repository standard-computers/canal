package org.Canal.UI.Views.HR.Organizations;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.UI.Views.Finance.Catalogs.CreateCatalog;
import org.Canal.UI.Views.Finance.CostCenters.CostCenters;
import org.Canal.UI.Views.Items.CreateItem;
import org.Canal.UI.Views.Finance.Customers.FindCustomer;
import org.Canal.UI.Views.Materials.CreateMaterial;
import org.Canal.UI.Views.Orders.PurchaseOrders.CreatePurchaseOrder;
import org.Canal.UI.Views.Controllers.CheckboxBarcodeFrame;
import org.Canal.Utils.*;
import org.Canal.Utils.DesktopState;

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

/**
 * /ORGS/$[ORGANIZATION_ID]
 */
public class OrgView extends LockeState {

    private Organization location;
    private JTree dataTree;
    private ArrayList<String> printables = new ArrayList<>();
    private DesktopState desktop;

    public OrgView(Organization loc, DesktopState desktop) {
        super(loc.getId() + " - " + loc.getName(), "/ORGS/NEW", true, true, true, true);
        this.location = loc;
        this.desktop = desktop;
        setFrameIcon(new ImageIcon(OrgView.class.getResource("/icons/organizations.png")));
        setLayout(new BorderLayout());
        JPanel tb = createToolBar();
        add(tb, BorderLayout.NORTH);
        JScrollPane tableScrollPane = new JScrollPane(new JTabbedPane());
        JPanel dataView = new JPanel(new BorderLayout());
        JTextField cmd = new JTextField("/ORGS/" + location.getId());
        cmd.addActionListener(_ -> desktop.put(Engine.router(cmd.getText(), desktop)));
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
                        Locke orgNode = (Locke) node.getUserObject();
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
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton findCustomer = new IconButton("Find Customer", "find", "Pull customer with ID");
        IconButton createOrder = new IconButton("Order", "order", "Order from vendor");
        IconButton addItem = new IconButton("+ Item", "items", "Build an item");
        IconButton addMaterial = new IconButton("+ Material", "materials", "Add a material");
        IconButton addCatalog = new IconButton("+ Catalog", "catalogs", "Add a catalog");
        IconButton label = new IconButton("", "label", "Print labels for org properties");
        IconButton costCenters = new IconButton("Find Cost Center", "cost_centers", "Pull cost center with ID");
        createOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreatePurchaseOrder());
            }
        });
        addItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateItem(desktop));
            }
        });
        addMaterial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateMaterial());
            }
        });
        addCatalog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CreateCatalog(desktop));
            }
        });
        findCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new FindCustomer(desktop));
            }
        });
        costCenters.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                desktop.put(new CostCenters(desktop));
            }
        });
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] spool = new String[printables.size()];
                for (int i = 0; i < printables.size(); i++) {
                    spool[i] = printables.get(i);
                }
                desktop.put(new CheckboxBarcodeFrame(spool));
            }
        });
        tb.add(findCustomer);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createOrder);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addItem);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addMaterial);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(addCatalog);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(costCenters);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    private void reloadStore(){
        Locke rootNode = createRootNode();
        DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
        DefaultTreeModel model = (DefaultTreeModel) dataTree.getModel();
        model.setRoot(rootTreeNode);
        expandAllNodes(dataTree);
        revalidate();
        repaint();
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
        Locke[] ccs = new Locke[Engine.getCostCenters(location.getId()).size()];
        for (int i = 0; i < Engine.getCostCenters(location.getId()).size(); i++) {
            Location l = Engine.getCostCenters(location.getId()).get(i);
            ccs[i] = new Locke(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", UIManager.getIcon("FileView.fileIcon"), "/CCS/" + l.getId(), Constants.colors[0], null);
            printables.add(l.getId());
        }
        Locke[] dcss = new Locke[Engine.getDistributionCenters(location.getId()).size()];
        for (int i = 0; i < Engine.getDistributionCenters(location.getId()).size(); i++) {
            Location l = Engine.getDistributionCenters(location.getId()).get(i);
            dcss[i] = new Locke(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", UIManager.getIcon("FileView.fileIcon"), "/DCSS/" + l.getId(), Constants.colors[1], null);
            printables.add(l.getId());
        }
        Locke[] warehouses = new Locke[Engine.getWarehouses(location.getId()).size()];
        for (int i = 0; i < Engine.getWarehouses(location.getId()).size(); i++) {
            Warehouse l = Engine.getWarehouses(location.getId()).get(i);
            warehouses[i] = new Locke(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", UIManager.getIcon("FileView.fileIcon"), "/WHS/" + l.getId(), Constants.colors[2], null);
            printables.add(l.getId());
        }
        Locke[] customers = new Locke[Engine.getCustomers(location.getId()).size()];
        for (int i = 0; i < Engine.getCustomers(location.getId()).size(); i++) {
            Location l = Engine.getCustomers(location.getId()).get(i);
            customers[i] = new Locke(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", UIManager.getIcon("FileView.fileIcon"), "/CSTS/" + l.getId(), Constants.colors[3], null);
            printables.add(l.getId());
        }
        Locke[] vendors = new Locke[Engine.getVendors(location.getId()).size()];
        for (int i = 0; i < Engine.getVendors(location.getId()).size(); i++) {
            Vendor l = Engine.getVendors(location.getId()).get(i);
            vendors[i] = new Locke(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", UIManager.getIcon("FileView.fileIcon"), "/VEND/" + l.getId(), Constants.colors[4], null);
            printables.add(l.getId());
        }
        Locke[] items = new Locke[Engine.getItems(location.getId()).size()];
        for (int i = 0; i < Engine.getItems(location.getId()).size(); i++) {
            Item l = Engine.getItems(location.getId()).get(i);
            items[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/ITS/" + l.getId(), Constants.colors[5], null);
            printables.add(l.getId());
        }
        Locke[] materials = new Locke[Engine.getMaterials(location.getId()).size()];
        for (int i = 0; i < Engine.getMaterials(location.getId()).size(); i++) {
            Material l = Engine.getMaterials(location.getId()).get(i);
            materials[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/MTS/" + l.getId(), Constants.colors[6], null);
            printables.add(l.getId());
        }
        Locke[] employees = new Locke[Engine.getEmployees(location.getId()).size()];
        for (int i = 0; i < Engine.getEmployees(location.getId()).size(); i++) {
            Employee l = Engine.getEmployees(location.getId()).get(i);
            employees[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/EMPS/" + l.getId(), Constants.colors[7], null);
            printables.add(l.getId());
        }
        Locke[] catalogs = new Locke[Engine.getCatalogs().size()];
        for (int i = 0; i < Engine.getCatalogs().size(); i++) {
            Catalog l = Engine.getCatalogs().get(i);
            catalogs[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/CATS/" + l.getId(), Constants.colors[8], null);
            printables.add(l.getId());
        }
        Locke[] orders = new Locke[Engine.getOrders().size()];
        for (int i = 0; i < Engine.getOrders().size(); i++) {
            PurchaseOrder l = Engine.getOrders().get(i);
            orders[i] = new Locke(String.valueOf(l.getOrderId()), UIManager.getIcon("FileView.fileIcon"), "/ORDS/" + l.getOrderId(), Constants.colors[9], null);
        }
        Locke[] ledgers = new Locke[Engine.getLedgers(location.getId()).size()];
        for (int i = 0; i < Engine.getLedgers(location.getId()).size(); i++) {
            Ledger l = Engine.getLedgers(location.getId()).get(i);
            ledgers[i] = new Locke(l.getId() + " - " + l.getName(), UIManager.getIcon("FileView.fileIcon"), "/LGS/" + l.getId(), Constants.colors[10], null);
        }
        return new Locke(location.getId() + " - " + location.getName(), UIManager.getIcon("FileView.fileIcon"), "/ORGS", new Locke[]{
                new Locke("Cost Centers", UIManager.getIcon("FileView.fileIcon"), "/CCS", ccs),
                new Locke("Distribution Centers", UIManager.getIcon("FileView.fileIcon"), "/DCSS", dcss),
                new Locke("Warehouses", UIManager.getIcon("FileView.fileIcon"), "/WHS", warehouses),
                new Locke("Customers", UIManager.getIcon("FileView.fileIcon"), "/CSTS", customers),
                new Locke("Vendors", UIManager.getIcon("FileView.fileIcon"), "/VEND", vendors),
                new Locke("Items", UIManager.getIcon("FileView.fileIcon"), "/ITS", items),
                new Locke("Materials", UIManager.getIcon("FileView.fileIcon"), "/MTS", materials),
                new Locke("Employees", UIManager.getIcon("FileView.fileIcon"), "/EMPS", employees),
                new Locke("Catalogs", UIManager.getIcon("FileView.fileIcon"), "/CATS", catalogs),
                new Locke("Orders", UIManager.getIcon("FileView.fileIcon"), "/ORDS", orders),
                new Locke("Ledgers", UIManager.getIcon("FileView.fileIcon"), "/LGS", ledgers)
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