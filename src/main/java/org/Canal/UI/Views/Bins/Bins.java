package org.Canal.UI.Views.Bins;

import org.Canal.Models.SupplyChainUnits.Area;
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
import java.util.ArrayList;

/**
 * /BNS
 */
public class Bins extends LockeState {

    private DefaultListModel<Area> listModel;

    public Bins(DesktopState desktop) {
        super("Bins", "/BNS", false, true, false, true);
        setFrameIcon(new ImageIcon(Bins.class.getResource("/icons/areas.png")));
        listModel = new DefaultListModel<>();
        JList<Area> list = new JList<>(listModel);
        list.setCellRenderer(new BinRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Area item = listModel.getElementAt(selectedIndex);
                        Engine.router("/BNS/" + item.getId(), desktop);
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
                        Engine.router("/BNS/" + inputText, desktop);
                    }
                }
            }
        });
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
        loadFlexes();
    }

    private void loadFlexes(){
        ArrayList<Area> found = Engine.getAreas();
        listModel.removeAllElements();
        for (Area f : found) {
            listModel.addElement(f);
        }
    }

    static class BinRenderer extends JPanel implements ListCellRenderer<Area> {

        private JLabel binName;
        private JLabel binId;

        public BinRenderer() {
            setLayout(new GridLayout(2, 1));
            binName = new JLabel();
            binId = new JLabel();
            binName.setFont(new Font("Arial", Font.BOLD, 16));
            binId.setFont(new Font("Arial", Font.PLAIN, 12));
            add(binName);
            add(binId);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Area> list, Area value, int index, boolean isSelected, boolean cellHasFocus) {
            binName.setText(value.getName());
            binId.setText(value.getId());
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