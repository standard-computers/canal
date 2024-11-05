package org.Canal.UI.Views.Distribution.Vendors;

import org.Canal.Models.SupplyChainUnits.Vendor;
import org.Canal.UI.Elements.Button;
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
 * /VEND
 */
public class Vendors extends JInternalFrame {

    private DefaultListModel<Vendor> listModel;

    public Vendors(DesktopState desktop) {
        super("Vendors", false, true, false, true);
        setFrameIcon(new ImageIcon(Vendors.class.getResource("/icons/vendors.png")));
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
                            desktop.put(Engine.router("/VEND/" + l.getId(), desktop));
                        } else {
                            JOptionPane.showMessageDialog(null, "Vendor Not Found");
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
                    if (!inputText.isEmpty()) {
                        desktop.put(Engine.router("/VEND/" + inputText, desktop));
                    }
                }
            }
        });
        Button nla = new Button("Add a Vendor");
        nla.addActionListener(_ -> desktop.put(Engine.router("/VEND//NEW", desktop)));
        JPanel options = new JPanel();
        options.add(nla);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(options, BorderLayout.SOUTH);
        add(mainPanel);
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