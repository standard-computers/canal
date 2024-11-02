package org.Canal.UI.Views.Distribution.Warehouses;

import org.Canal.Models.SupplyChainUnits.Warehouse;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Views.Controllers.Controller;
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
 * /WHS
 */
public class Warehouses extends JInternalFrame {

    private DefaultListModel<Warehouse> listModel;

    public Warehouses(DesktopState desktop) {
        setTitle("Warehouses");
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/warehouses.png")));
        listModel = new DefaultListModel<>();
        JList<Warehouse> list = new JList<>(listModel);
        list.setCellRenderer(new WarehouseRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Warehouse l = listModel.getElementAt(selectedIndex);
                        if (l != null) {
                            desktop.put(Engine.router("/WHS/" + l.getId(), desktop));
                        } else {
                            JOptionPane.showMessageDialog(null, "Location Not Found");
                        }
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
                        desktop.put(Engine.router("/WHS/" + inputText, desktop));
                    }
                }
            }
        });
        Button nla = new Button("Add a Warehouse");
        nla.addActionListener(e -> desktop.put(Engine.router("/WHS/NEW", desktop)));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(nla, BorderLayout.SOUTH);
        add(mainPanel);
        setResizable(false);
        setIconifiable(true);
        setClosable(true);
        loadLocations();
    }

    private void loadLocations(){
        listModel.removeAllElements();
        Engine.load();
        ArrayList<Warehouse> found = Engine.getWarehouses();
        for (Warehouse loc : found) {
            listModel.addElement(loc);
        }
    }

    class WarehouseRenderer extends JPanel implements ListCellRenderer<Warehouse> {

        private JLabel ccName;
        private JLabel ccId;
        private JLabel line1;
        private JLabel line2;

        public WarehouseRenderer() {
            setLayout(new GridLayout(4, 1));
            ccName = new JLabel();
            ccId = new JLabel();
            line1 = new JLabel();
            line2 = new JLabel();
            ccName.setFont(new Font("Arial", Font.BOLD, 16));
            ccId.setFont(new Font("Arial", Font.PLAIN, 12));
            line1.setFont(new Font("Arial", Font.PLAIN, 12));
            line2.setFont(new Font("Arial", Font.PLAIN, 12));
            add(ccName);
            add(ccId);
            add(line1);
            add(line2);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Warehouse> list, Warehouse warehouse, int index, boolean isSelected, boolean cellHasFocus) {
            ccName.setText(warehouse.getName());
            ccId.setText(warehouse.getId());
            line1.setText(warehouse.getLine1());
            line2.setText(warehouse.getCity() + ", " + warehouse.getState() + " " + warehouse.getPostal() + " " + warehouse.getCountry());
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