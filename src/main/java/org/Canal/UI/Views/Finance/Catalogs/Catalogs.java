package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CATS
 */
public class Catalogs extends JInternalFrame {

    private DefaultListModel<Catalog> listModel;

    public Catalogs(DesktopState desktop) {
        setTitle("Catalogs");
        listModel = new DefaultListModel<>();
        JList<Catalog> list = new JList<>(listModel);
        list.setCellRenderer(new CatalogRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Catalog l = listModel.getElementAt(selectedIndex);
                        if (l != null) {
                            Engine.router("/CATS/" + l.getId(), desktop);
                        } else {
                            JOptionPane.showMessageDialog(null, "Ledger Not Found");
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
                        Engine.router("/CATS/" + inputText, desktop);
                    }
                }
            }
        });
        JButton nla = new JButton("Build a Catalog");
        nla.addActionListener(e -> new CreateCatalog(desktop));
        JPanel options = new JPanel();
        options.add(nla);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(options, BorderLayout.SOUTH);
        add(mainPanel);
        setResizable(false);
        loadCatalogs();
    }

    private void loadCatalogs(){
        listModel.removeAllElements();
        Engine.load();
        for (Catalog loc : Engine.getCatalogs()) {
            listModel.addElement(loc);
        }
    }

    class CatalogRenderer extends JPanel implements ListCellRenderer<Catalog> {

        private JLabel catalogName;
        private JLabel catalogId;
        private JLabel itemcount;

        public CatalogRenderer() {
            setLayout(new GridLayout(4, 1));
            catalogName = new JLabel();
            catalogId = new JLabel();
            itemcount = new JLabel();
            catalogName.setFont(new Font("Arial", Font.BOLD, 16));
            catalogId.setFont(new Font("Arial", Font.PLAIN, 12));
            itemcount.setFont(new Font("Arial", Font.PLAIN, 12));
            add(catalogName);
            add(catalogId);
            add(itemcount);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Catalog> list, Catalog catalog, int index, boolean isSelected, boolean cellHasFocus) {
            catalogName.setText(catalog.getName());
            catalogId.setText(catalog.getId());
            itemcount.setText(catalog.getItems().size() + " Available Items");
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