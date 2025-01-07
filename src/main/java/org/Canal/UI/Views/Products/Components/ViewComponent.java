package org.Canal.UI.Views.Products.Components;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Locke;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

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
 * /CMPS/$[COMPONENT_ID]
 */
public class ViewComponent extends LockeState {

    private Item item;
    private JTree dataTree;
    private Copiable idField;
    private Copiable orgField;
    private Copiable nameField;
    private Copiable vendorField;
    private Copiable colorField;
    private Copiable batchedField;
    private Copiable rentableField;
    private Copiable skudField;
    private Copiable consumableField;
    private Copiable priceField;
    private Copiable widthField;
    private Copiable lengthField;
    private Copiable heightField;
    private Copiable weightField;
    private Copiable taxField;
    private Copiable exciseTaxfield;
    private Copiable vendorIdField;
    private Copiable vendorNameField;
    private Copiable vendorStreetField;
    private Copiable vendorCityField;
    private Copiable vendorStateField;
    private Copiable vendorPostalField;
    private Copiable vendorCountryField;
    private Copiable vendorTaxExemptField;
    private Copiable vendorStatusField;

    public ViewComponent(Item item) {

        super("Component / " + item.getId() + " - " + item.getName(), "/CMPS/$", true, true, true, true);
        setFrameIcon(new ImageIcon(ViewComponent.class.getResource("/icons/components.png")));
        this.item = item;

        setLayout(new BorderLayout());
        JPanel tb = createToolBar();
        add(tb, BorderLayout.NORTH);

        JPanel iic = new JPanel(new BorderLayout());
        iic.add(Elements.h2("Item Information"), BorderLayout.NORTH);
        iic.add(itemInfo(), BorderLayout.CENTER);

        JScrollPane itemScrollPane = new JScrollPane(iic);
        JScrollPane vendorScrollPane = new JScrollPane(vendorInfo(Engine.getLocation(item.getVendor(), "VEND")));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, itemScrollPane, vendorScrollPane);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        JPanel dataView = new JPanel(new BorderLayout());

        JTextField cmd = new JTextField("/ITS/" + item.getId());
        dataView.add(cmd, BorderLayout.NORTH);
        dataView.add(splitPane, BorderLayout.CENTER);
        dataTree = createTree();
        dataTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath path = dataTree.getPathForLocation(e.getX(), e.getY());
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                Locke orgNode = (Locke) node.getUserObject();
                Item selectedItem  = Engine.getItem(orgNode.getTransaction());
                idField.setText(selectedItem.getId());
                orgField.setText(selectedItem.getOrg());
                nameField.setText(selectedItem.getName());
                vendorField.setText(selectedItem.getVendor());
                colorField.setText(selectedItem.getColor());
                batchedField.setText(String.valueOf(selectedItem.isBatched()));
                rentableField.setText(String.valueOf(selectedItem.isRentable()));
                skudField.setText(String.valueOf(selectedItem.isSkud()));
                consumableField.setText(String.valueOf(selectedItem.isConsumable()));
                priceField.setText(String.valueOf(selectedItem.getPrice()));
                widthField.setText(String.valueOf(selectedItem.getWidth()));
                lengthField.setText(String.valueOf(selectedItem.getLength()));
                heightField.setText(String.valueOf(selectedItem.getHeight()));
                weightField.setText(String.valueOf(selectedItem.getWidth()));
                taxField.setText(String.valueOf(selectedItem.getTax()));
                exciseTaxfield.setText(String.valueOf(selectedItem.getExciseTax()));

                Location selectedVendor = Engine.getLocation(selectedItem.getVendor(), "VEND");
                vendorIdField.setText(selectedVendor.getId());
                vendorNameField.setText(selectedVendor.getName());
                vendorStreetField.setText(selectedVendor.getLine1());
                vendorCityField.setText(selectedVendor.getCity());
                vendorStateField.setText(selectedVendor.getState());
                vendorPostalField.setText(selectedVendor.getPostal());
                vendorCountryField.setText(selectedVendor.getCountry());
                vendorTaxExemptField.setText(String.valueOf(selectedVendor.isTaxExempt()));
                vendorStatusField.setText(String.valueOf(selectedVendor.getStatus()));
            }
        });
        JScrollPane treeScrollPane = new JScrollPane(dataTree);
        JSplitPane infoSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, dataView);
        infoSplitPane.setDividerLocation(250);
        infoSplitPane.setResizeWeight(0.2);
        add(infoSplitPane, BorderLayout.CENTER);
        setIconifiable(true);
        setClosable(true);
        setResizable(true);
    }

    private JPanel vendorInfo(Location vendor) {
        if (vendor == null) {
            JOptionPane.showMessageDialog(null, "A Vendor has not been allocated for this item. Please contact the data team.");
        }
        Form vi = new Form();
        vendorIdField = new Copiable(vendor.getId());
        vendorNameField = new Copiable(vendor.getName());
        vendorStreetField = new Copiable(vendor.getLine1());
        vendorCityField = new Copiable(vendor.getCity());
        vendorStateField = new Copiable(vendor.getState());
        vendorPostalField = new Copiable(vendor.getPostal());
        vendorCountryField = new Copiable(vendor.getCountry());
        vendorTaxExemptField = new Copiable(String.valueOf(vendor.isTaxExempt()));
        vendorStatusField = new Copiable(String.valueOf(vendor.getStatus()));
        vi.addInput(Elements.coloredLabel("Vendor ID", UIManager.getColor("Label.foreground")), vendorIdField);
        vi.addInput(Elements.coloredLabel("Vendor Name", UIManager.getColor("Label.foreground")), vendorNameField);
        vi.addInput(Elements.coloredLabel("Street", UIManager.getColor("Label.foreground")), vendorStreetField);
        vi.addInput(Elements.coloredLabel("City", UIManager.getColor("Label.foreground")), vendorCityField);
        vi.addInput(Elements.coloredLabel("State", UIManager.getColor("Label.foreground")), vendorStateField);
        vi.addInput(Elements.coloredLabel("Postal", UIManager.getColor("Label.foreground")), vendorPostalField);
        vi.addInput(Elements.coloredLabel("Country", UIManager.getColor("Label.foreground")), vendorCountryField);
        vi.addInput(Elements.coloredLabel("Tax Exempt", UIManager.getColor("coloredLabel.foreground")), vendorTaxExemptField);
        vi.addInput(Elements.coloredLabel("Status", UIManager.getColor("coloredLabel.foreground")), vendorStatusField);
        vi.setBorder(new EmptyBorder(10, 10, 10, 10));
        return vi;
    }

    private JPanel itemInfo() {
        Form f = new Form();
        idField = new Copiable(item.getId());
        orgField = new Copiable(item.getOrg());
        nameField = new Copiable(item.getName());
        vendorField = new Copiable(item.getVendor());
        colorField = new Copiable(item.getColor());
        batchedField = new Copiable(String.valueOf(item.isBatched()));
        rentableField = new Copiable(String.valueOf(item.isRentable()));
        skudField = new Copiable(String.valueOf(item.isSkud()));
        consumableField = new Copiable(String.valueOf(item.isConsumable()));
        priceField = new Copiable(String.valueOf(item.getPrice()));
        widthField = new Copiable(String.valueOf(item.getWidth()));
        lengthField = new Copiable(String.valueOf(item.getLength()));
        heightField = new Copiable(String.valueOf(item.getHeight()));
        weightField = new Copiable(String.valueOf(item.getWeight()));
        taxField = new Copiable(String.valueOf(item.getTax()));
        exciseTaxfield = new Copiable(String.valueOf(item.getExciseTax()));
        f.addInput(Elements.coloredLabel("ID", Constants.colors[0]), idField);
        f.addInput(Elements.coloredLabel("Organization", Constants.colors[1]), orgField);
        f.addInput(Elements.coloredLabel("Name", Constants.colors[2]), nameField);
        f.addInput(Elements.coloredLabel("Vendor", Constants.colors[3]), vendorField);
        f.addInput(Elements.coloredLabel("Color", Constants.colors[4]), colorField);
        f.addInput(Elements.coloredLabel("Batched", Constants.colors[5]), batchedField);
        f.addInput(Elements.coloredLabel("Rentable", Constants.colors[6]), rentableField);
        f.addInput(Elements.coloredLabel("SKU'd", Constants.colors[6]), skudField);
        f.addInput(Elements.coloredLabel("Consumable", Constants.colors[7]), consumableField);
        f.addInput(Elements.coloredLabel("Price", Constants.colors[8]), priceField);
        f.addInput(Elements.coloredLabel("Width", Constants.colors[9]), widthField);
        f.addInput(Elements.coloredLabel("Length", Constants.colors[10]), lengthField);
        f.addInput(Elements.coloredLabel("Height", Constants.colors[0]), heightField);
        f.addInput(Elements.coloredLabel("Weight", Constants.colors[1]), weightField);
        f.addInput(Elements.coloredLabel("Tax", Constants.colors[2]), taxField);
        f.addInput(Elements.coloredLabel("Excise Tax", Constants.colors[3]), exciseTaxfield);
        return f;
    }

    private JPanel createToolBar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton inventory = new IconButton("Inventory", "inventory", "Check stock of item");
        IconButton label = new IconButton("", "label", "Print labels for properties");
        IconButton refresh = new IconButton("", "refresh", "Reload from store");
        refresh.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Locke rootNode = createRootNode();
                DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
                DefaultTreeModel model = (DefaultTreeModel) dataTree.getModel();
                model.setRoot(rootTreeNode);
                expandAllNodes(dataTree);
                revalidate();
                repaint();
            }
        });
        tb.add(inventory);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(label);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
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

        Locke[] items = new Locke[Engine.getItems(Engine.getOrganization().getId()).size()];
        for (int i = 0; i < Engine.getItems(Engine.getOrganization().getId()).size(); i++) {
            Item l = Engine.getItems(Engine.getOrganization().getId()).get(i);
            items[i] = new Locke(l.getId() + " - " + l.getName() + " / Items", UIManager.getIcon("FileView.fileIcon"), l.getId(), new Color(147, 70, 3), null);
        }
        return new Locke(Engine.getOrganization().getId() + " - " + Engine.getOrganization().getName(), UIManager.getIcon("FileView.fileIcon"), "/ITS", items);
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