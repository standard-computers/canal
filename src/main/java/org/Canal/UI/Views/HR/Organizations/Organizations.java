package org.Canal.UI.Views.HR.Organizations;

import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /ORGS
 * View a list of Organizations in this instance of Canal
 */
public class Organizations extends LockeState {

    private DefaultListModel<Organization> listModel;

    public Organizations(DesktopState desktop) {
        super("Organizations", "/ORGS", false, true, false, true);
        setFrameIcon(new ImageIcon(Organizations.class.getResource("/icons/organizations.png")));
        listModel = new DefaultListModel<>();
        JList<Organization> list = new JList<>(listModel);
        list.setCellRenderer(new OrgListRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(250, 200));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Organization selectedOrg = listModel.getElementAt(selectedIndex);
                        desktop.put(new OrgView(selectedOrg, desktop));
                    }
                }
            }
        });
        JTextField direct = Elements.input();
        direct.addActionListener(_ -> {
            desktop.put(new OrgView(Engine.getOrganization(direct.getText()), desktop));
            dispose();
        });
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
        for (Organization org : Engine.getOrganizations()) {
            listModel.addElement(org);
        }
    }

    static class OrgListRenderer extends JPanel implements ListCellRenderer<Organization> {

        private JLabel orgName;
        private JLabel orgId;

        public OrgListRenderer() {
            setLayout(new GridLayout(2, 1));
            orgName = new Label("", UIManager.getColor("Panel.background").darker());
            orgId = new JLabel();
            add(orgName, BorderLayout.NORTH);
            add(orgId, BorderLayout.SOUTH);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Organization> list, Organization value, int index, boolean isSelected, boolean cellHasFocus) {
            orgName.setText(value.getName());
            orgId.setText(value.getId());
            if (isSelected) {
                setBackground(UIManager.getColor("Panel.background").darker());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
}