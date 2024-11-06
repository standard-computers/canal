package org.Canal.UI.Views.Components;

import org.Canal.Models.SupplyChainUnits.Vendor;
import org.Canal.UI.Elements.Elements;
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
 * /CMPS
 */
public class Components extends JInternalFrame {

    private DefaultListModel<Vendor> listModel;

    public Components(DesktopState desktop) {
        super("Components", false, true, false, true);
        setFrameIcon(new ImageIcon(Components.class.getResource("/icons/vendors.png")));
        listModel = new DefaultListModel<>();
        JList<Vendor> list = new JList<>(listModel);
        list.setCellRenderer(new VendorRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Vendor l = listModel.getElementAt(selectedIndex);
                        if (l != null) {
                            desktop.put(Engine.router("/CMPS/" + l.getId(), desktop));
                        } else {
                            JOptionPane.showMessageDialog(null, "Vendor Not Found");
                        }
                    }
                }
            }
        });
        JTextField direct = Elements.input();
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    if (!inputText.isEmpty()) {
                        desktop.put(Engine.router("/CMPS/" + inputText, desktop));
                    }
                }
            }
        });
        setLayout(new BorderLayout());
        add(direct, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        loadLocations();
    }

    private void loadLocations(){
        ArrayList<Vendor> found = Engine.getVendors();
        listModel.removeAllElements();
        for (Vendor loc : found) {
            listModel.addElement(loc);
        }
    }

    static class VendorRenderer extends JPanel implements ListCellRenderer<Vendor> {

        private JLabel vendorName;
        private JLabel vendorId;
        private JLabel line1;
        private JLabel line2;

        public VendorRenderer() {
            setLayout(new GridLayout(4, 1));
            vendorName = new JLabel();
            vendorId = new JLabel();
            line1 = new JLabel();
            line2 = new JLabel();
            vendorName.setFont(new Font("Arial", Font.BOLD, 16));
            vendorId.setFont(new Font("Arial", Font.PLAIN, 12));
            line1.setFont(new Font("Arial", Font.PLAIN, 12));
            line2.setFont(new Font("Arial", Font.PLAIN, 12));
            add(vendorName);
            add(vendorId);
            add(line1);
            add(line2);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Vendor> list, Vendor vendor, int index, boolean isSelected, boolean cellHasFocus) {
            vendorName.setText(vendor.getName());
            vendorId.setText(vendor.getId());
            line1.setText(vendor.getLine1());
            line2.setText(vendor.getCity() + ", " + vendor.getState() + " " + vendor.getPostal() + " " + vendor.getCountry());
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