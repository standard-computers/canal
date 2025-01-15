package org.Canal.UI.Views.Controllers;

import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.Elements;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;

public class Controller extends JPanel implements RefreshListener {

    private JTree dataTree;
    private JTextField cmd;

    public Controller(DesktopState desktop) {

        User me = Engine.getAssignedUser();
        setLayout(new BorderLayout());
        JPanel dataView = new JPanel(new BorderLayout());
        cmd = Elements.input("/");
        cmd.addActionListener(_ -> {
            if(me != null && !me.hasAccess(cmd.getText().replaceAll("\\.", ""))) {
                JOptionPane.showMessageDialog(this, "Not authorized to use this locke!", "Unauthorized", JOptionPane.ERROR_MESSAGE);
            }else{
                try{
                    desktop.put(Engine.router(cmd.getText(), desktop));
                }catch(NullPointerException | ArrayIndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(this, "Locke not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
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
                e.consume();
                TreePath path = dataTree.getPathForLocation(e.getX(), e.getY());
                if (path != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    Locke orgNode = (Locke) node.getUserObject();
                    if(me != null && !me.hasAccess(orgNode.getTransaction())) {
                        JOptionPane.showMessageDialog(Controller.this, "Not authorized to use this locke!", "Unauthorized", JOptionPane.ERROR_MESSAGE);
                    }else{
                        desktop.put(Engine.router(orgNode.getTransaction(), desktop));
                    }
                }
            }
        });
        JScrollPane treeScrollPane = new JScrollPane(dataTree);
        add(cmd, BorderLayout.NORTH);
        add(treeScrollPane, BorderLayout.CENTER);
    }

    public void setBar(String s){

        cmd.setText(s);
        repaint();
        revalidate();
    }

    @Override
    public void refresh() {
        reloadStore();
    }

    private void reloadStore(){

        Locke rootNode = Constants.allModules();
        DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
        DefaultTreeModel model = (DefaultTreeModel) dataTree.getModel();
        model.setRoot(rootTreeNode);
//        expandAllNodes(dataTree);
        revalidate();
        repaint();
    }

    private JTree createTree() {

        Locke rootNode = Constants.allModules();
        DefaultMutableTreeNode rootTreeNode = createTreeNodes(rootNode);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootTreeNode);
        JTree tree = new JTree(treeModel);
        tree.setCellRenderer(new CustomTreeCellRenderer());
//        expandAllNodes(tree);
        return tree;
    }

    private void expandAllNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
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
            setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 14));
            if(isFocusable()){
                setBackground(UIManager.getColor("Panel.background"));
            }else{
                setBackground(orgNode.getColor());
            }
            return component;
        }
    }
}