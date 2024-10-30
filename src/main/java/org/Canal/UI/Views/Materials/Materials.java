package org.Canal.UI.Views.Materials;

import org.Canal.Models.SupplyChainUnits.Material;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /MTS
 */
public class Materials extends JInternalFrame {

    private DefaultListModel<Material> listModel;

    public Materials(DesktopState desktop) {
        setTitle("Materials");
        setFrameIcon(new ImageIcon(Materials.class.getResource("/icons/materials.png")));
        listModel = new DefaultListModel<>();
        JList<Material> list = new JList<>(listModel);
        list.setCellRenderer(new MaterialRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Material item = listModel.getElementAt(selectedIndex);
                        Engine.router("/MTS/" + item.getId(), desktop);
                    }
                }
            }
        });
        JTextField direct = new JTextField();
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    System.out.println(inputText);
                    if (!inputText.isEmpty()) {
                        Engine.router("/MTS/" + inputText, desktop);
                    }
                }
            }
        });
        JButton nla = new JButton("Create a Material");
        nla.addActionListener(_ -> desktop.put(new CreateMaterial()));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(nla, BorderLayout.SOUTH);
        add(mainPanel);
        setResizable(false);
        pack();
        setVisible(true);
        loadFlexes();
    }

    private void loadFlexes(){
        Engine.load();
        ArrayList<Material> found = Engine.getMaterials();
        listModel.removeAllElements();
        for (Material f : found) {
            listModel.addElement(f);
        }
    }

    class MaterialRenderer extends JPanel implements ListCellRenderer<Material> {

        private JLabel materialName;
        private JLabel materialId;
        private JLabel materialPrice;

        public MaterialRenderer() {
            setLayout(new GridLayout(4, 1));
            materialName = new JLabel();
            materialId = new JLabel();
            materialPrice = new JLabel();
            materialName.setFont(new Font("Arial", Font.BOLD, 16));
            materialId.setFont(new Font("Arial", Font.PLAIN, 12));
            materialPrice.setFont(new Font("Arial", Font.PLAIN, 12));
            add(materialName);
            add(materialId);
            add(materialPrice);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Material> list, Material value, int index, boolean isSelected, boolean cellHasFocus) {
            materialName.setText(value.getName());
            materialId.setText(value.getId());
            materialPrice.setText("$" + value.getPrice());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
}