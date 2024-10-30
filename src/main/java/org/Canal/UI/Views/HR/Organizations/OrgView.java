package org.Canal.UI.Views.HR.Organizations;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.Models.BusinessUnits.PurchaseOrder;
import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.SupplyChainUnits.*;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Views.Finance.Catalogs.CreateCatalog;
import org.Canal.UI.Views.Finance.CostCenters.CostCenters;
import org.Canal.UI.Views.Items.CreateItem;
import org.Canal.UI.Views.Finance.Customers.FindCustomer;
import org.Canal.UI.Views.Materials.CreateMaterial;
import org.Canal.UI.Views.Orders.CreatePurchaseOrder;
import org.Canal.UI.Views.Managers.CheckboxBarcodeFrame;
import org.Canal.Utils.*;
import org.Canal.Utils.DesktopState;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class OrgView extends JInternalFrame {

    private Organization location;
    private JTree dataTree;
    private ArrayList<String> printables = new ArrayList<>();
    private DesktopState desktop;

    public OrgView(Organization loc, DesktopState desktop) {
        this.location = loc;
        this.desktop = desktop;
        setTitle(loc.getId() + " - " + loc.getName());
        setFrameIcon(new ImageIcon(OrgView.class.getResource("/icons/organizations.png")));
        setLayout(new BorderLayout());
        JPanel tb = createToolBar();
        JScrollPane scrollPane = new JScrollPane(tb);
        scrollPane.setBackground(UIManager.getColor("secondary1"));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setViewPosition(new Point(0, 0));
        add(scrollPane, BorderLayout.NORTH);
        JTable table = createTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        JPanel dataView = new JPanel(new BorderLayout());
        JTextField cmd = new JTextField("/ORGS/" + location.getId());
        cmd.addActionListener(e -> desktop.put(Engine.router(cmd.getText(), desktop)));
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
        setResizable(true);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton findCustomer = new IconButton("Find Customer", "find", "Pull customer with ID");
        IconButton createOrder = new IconButton("Order", "order", "Order from vendor");
        IconButton addItem = new IconButton("+ Item", "items", "Build an item");
        IconButton addMaterial = new IconButton("+ Material", "materials", "Add a material");
        IconButton addCatalog = new IconButton("+ Catalog", "catalog", "Add a catalog");
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
                desktop.put(new CreateItem());
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
        tb.add(Box.createHorizontalStrut(5));
        return tb;
    }

    private void reloadStore(){
        Engine.load();
        Canal rootNode = createRootNode();
        DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
        DefaultTreeModel model = (DefaultTreeModel) dataTree.getModel();
        model.setRoot(rootTreeNode);
        expandAllNodes(dataTree);
        revalidate();
        repaint();
    }

    private JTable createTable() {
        String[] columns = new String[]{"Property", "Value"};
        String[][] data = {
                {"Id", location.getId()},
                {"Name", location.getName()},
                {"Street", location.getLine1()},
                {"City", location.getCity()},
                {"State", location.getState()},
                {"Postal", location.getPostal()},
                {"Country", location.getCountry()},
                {"Tax Exempt Status", String.valueOf(location.isTaxExempt())}
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
        expandAllNodes(tree);
        return tree;
    }

    private Canal createRootNode() {
        Canal[] ccs = new Canal[Engine.getCostCenters(location.getId()).size()];
        for (int i = 0; i < Engine.getCostCenters(location.getId()).size(); i++) {
            Location l = Engine.getCostCenters(location.getId()).get(i);
            ccs[i] = new Canal(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", false, "/CCS/" + l.getId(), Constants.colors[0], null);
            printables.add(l.getId());
        }

        Canal[] dcss = new Canal[Engine.getDistributionCenters(location.getId()).size()];
        for (int i = 0; i < Engine.getDistributionCenters(location.getId()).size(); i++) {
            Location l = Engine.getDistributionCenters(location.getId()).get(i);
            dcss[i] = new Canal(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", false, "/DCSS/" + l.getId(), Constants.colors[1], null);
            printables.add(l.getId());
        }

        Canal[] warehouses = new Canal[Engine.getWarehouses(location.getId()).size()];
        for (int i = 0; i < Engine.getWarehouses(location.getId()).size(); i++) {
            Warehouse l = Engine.getWarehouses(location.getId()).get(i);
            warehouses[i] = new Canal(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", false, "/WHS/" + l.getId(), Constants.colors[2], null);
            printables.add(l.getId());
        }

        Canal[] customers = new Canal[Engine.getCustomers(location.getId()).size()];
        for (int i = 0; i < Engine.getCustomers(location.getId()).size(); i++) {
            Location l = Engine.getCustomers(location.getId()).get(i);
            customers[i] = new Canal(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", false, "/CSTS/" + l.getId(), Constants.colors[3], null);
            printables.add(l.getId());
        }

        Canal[] vendors = new Canal[Engine.getVendors(location.getId()).size()];
        for (int i = 0; i < Engine.getVendors(location.getId()).size(); i++) {
            Location l = Engine.getVendors(location.getId()).get(i);
            vendors[i] = new Canal(l.getId() + " - " + l.getName() + " (" + l.getCity() + ")", false, "/VEND/" + l.getId(), Constants.colors[4], null);
            printables.add(l.getId());
        }

        Canal[] items = new Canal[Engine.getItems(location.getId()).size()];
        for (int i = 0; i < Engine.getItems(location.getId()).size(); i++) {
            Item l = Engine.getItems(location.getId()).get(i);
            items[i] = new Canal(l.getId() + " - " + l.getName(), false, "/ITS/" + l.getId(), Constants.colors[5], null);
            printables.add(l.getId());
        }

        Canal[] materials = new Canal[Engine.getMaterials(location.getId()).size()];
        for (int i = 0; i < Engine.getMaterials(location.getId()).size(); i++) {
            Material l = Engine.getMaterials(location.getId()).get(i);
            materials[i] = new Canal(l.getId() + " - " + l.getName(), false, "/MTS/" + l.getId(), Constants.colors[6], null);
            printables.add(l.getId());
        }

        Canal[] employees = new Canal[Engine.getEmployees(location.getId()).size()];
        for (int i = 0; i < Engine.getEmployees(location.getId()).size(); i++) {
            Employee l = Engine.getEmployees(location.getId()).get(i);
            employees[i] = new Canal(l.getId() + " - " + l.getName(), false, "/EMPS/" + l.getId(), Constants.colors[7], null);
            printables.add(l.getId());
        }

        Canal[] catalogs = new Canal[Engine.getCatalogs().size()];
        for (int i = 0; i < Engine.getCatalogs().size(); i++) {
            Catalog l = Engine.getCatalogs().get(i);
            catalogs[i] = new Canal(l.getId() + " - " + l.getName(), false, "/CATS/" + l.getId(), Constants.colors[8], null);
            printables.add(l.getId());
        }

        Canal[] orders = new Canal[Engine.getOrders().size()];
        for (int i = 0; i < Engine.getOrders().size(); i++) {
            PurchaseOrder l = Engine.getOrders().get(i);
            orders[i] = new Canal(String.valueOf(l.getOrderId()), false, "/ORDS/" + l.getOrderId(), Constants.colors[9], null);
        }

        Canal[] ledgers = new Canal[Engine.getLedgers(location.getId()).size()];
        for (int i = 0; i < Engine.getLedgers(location.getId()).size(); i++) {
            Ledger l = Engine.getLedgers(location.getId()).get(i);
            ledgers[i] = new Canal(l.getId() + " - " + l.getName(), false, "/LGS/" + l.getId(), Constants.colors[10], null);
        }

        return new Canal(location.getId() + " - " + location.getName(), true, "/ORGS", new Canal[]{
                new Canal("Cost Centers", true, "/CCS", ccs),
                new Canal("Distribution Centers", true, "/DCSS", dcss),
                new Canal("Warehouses", true, "/WHS", warehouses),
                new Canal("Customers", true, "/CSTS", customers),
                new Canal("Vendors", true, "/VEND", vendors),
                new Canal("Items", true, "/ITS", items),
                new Canal("Materials", true, "/MTS", materials),
                new Canal("Employees", true, "/EMPS", employees),
                new Canal("Catalogs", true, "/CATS", catalogs),
                new Canal("Orders", true, "/ORDS", orders),
                new Canal("Ledgers", true, "/LGS", ledgers)
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

    private void expandAllNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}