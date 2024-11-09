package org.Canal.UI.Views.Areas;

import org.Canal.Models.SupplyChainUnits.Area;
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
 * /AREAS
 */
public class Areas extends JInternalFrame {

    private DefaultListModel<Area> listModel;

    public Areas(DesktopState desktop) {
        super("All Areas", false, true, false, true);
        setFrameIcon(new ImageIcon(Areas.class.getResource("/icons/areas.png")));
        listModel = new DefaultListModel<>();
        JList<Area> list = new JList<>(listModel);
        list.setCellRenderer(new AreaRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Area item = listModel.getElementAt(selectedIndex);
                        Engine.router("/AREAS/" + item.getId(), desktop);
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
                        Engine.router("/AREAS/" + inputText, desktop);
                    }
                }
            }
        });
        setLayout(new BorderLayout());
        add(direct, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        loadFlexes();
    }

    private void loadFlexes(){
        ArrayList<Area> found = Engine.getAreas();
        listModel.removeAllElements();
        for (Area f : found) {
            listModel.addElement(f);
        }
    }

    static class AreaRenderer extends JPanel implements ListCellRenderer<Area> {

        private JLabel areaName;
        private JLabel areaId;

        public AreaRenderer() {
            setLayout(new GridLayout(2, 1));
            areaName = new JLabel();
            areaId = new JLabel();
            areaName.setFont(new Font("Arial", Font.BOLD, 16));
            areaId.setFont(new Font("Arial", Font.PLAIN, 12));
            add(areaName);
            add(areaId);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Area> list, Area value, int index, boolean isSelected, boolean cellHasFocus) {
            areaName.setText(value.getName());
            areaId.setText(value.getId());
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