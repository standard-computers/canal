package org.Canal.UI.Views.Finance.Catalogs;

import org.Canal.Models.SupplyChainUnits.Catalog;
import org.Canal.UI.Elements.Windows.LockeState;
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
public class Catalogs extends LockeState {

    private DefaultListModel<Catalog> listModel;

    public Catalogs(DesktopState desktop) {
        super("Catalogs", "/CATS", false, true, false, true);
        setFrameIcon(new ImageIcon(Catalogs.class.getResource("/icons/catalogs.png")));
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
        setLayout(new BorderLayout());
        add(direct, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        loadCatalogs();
    }

    private void loadCatalogs(){
        listModel.removeAllElements();
        for (Catalog loc : Engine.getCatalogs()) {
            listModel.addElement(loc);
        }
    }

    static class CatalogRenderer extends JPanel implements ListCellRenderer<Catalog> {

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