package org.Canal.UI.Views.Managers;

import org.Canal.Utils.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;

public class Controller extends JInternalFrame implements RefreshListener {

    private JTree dataTree;

    public Controller(DesktopState desktop) {
        setTitle("Controller");
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/controller.png")));
        setLayout(new BorderLayout());
        JPanel dataView = new JPanel(new BorderLayout());
        JTextField cmd = new JTextField("/ORGS/" + Engine.getOrganization().getId());
        cmd.addActionListener(e -> desktop.put(Engine.router(cmd.getText(), desktop)));
        InputMap inputMap = cmd.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = cmd.getActionMap();
        KeyStroke keyStroke = KeyStroke.getKeyStroke("ctrl shift C");
        inputMap.put(keyStroke, "focusTextField");
        actionMap.put("focusTextField", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmd.requestFocusInWindow();
                cmd.selectAll();
            }
        });
        dataView.add(cmd, BorderLayout.NORTH);
        dataTree = createTree();
        dataTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    e.consume();
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
        add(cmd, BorderLayout.NORTH);
        add(treeScrollPane, BorderLayout.CENTER);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
    }

    @Override
    public void onRefresh() {
        reloadStore();
    }

    private void reloadStore(){
        Engine.load();
        Canal rootNode = Constants.allModules();
        DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
        DefaultTreeModel model = (DefaultTreeModel) dataTree.getModel();
        model.setRoot(rootTreeNode);
        expandAllNodes(dataTree);
        revalidate();
        repaint();
    }

    private JTree createTree() {
        Canal rootNode = Constants.allModules();
        DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootTreeNode);
        JTree tree = new JTree(treeModel);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        expandAllNodes(tree);
        return tree;
    }

    private void expandAllNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
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
}