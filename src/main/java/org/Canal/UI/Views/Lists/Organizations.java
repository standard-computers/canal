package org.Canal.UI.Views.Lists;

import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Views.New.*;
import org.Canal.UI.Views.Singleton.OrgView;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Organizations extends JInternalFrame {

    private DefaultListModel<Organization> listModel;
    private DesktopState desktop;

    public Organizations(DesktopState desktop) {
        setTitle("Organizations");
        setFrameIcon(new ImageIcon(Organizations.class.getResource("/icons/organizations.png")));
        listModel = new DefaultListModel<>();
        this.desktop = desktop;
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
                        openOrgView(selectedOrg);
                    }
                }
            }
        });
        JTextField direct = new JTextField();
        direct.addActionListener(e -> openOrgViewById(direct.getText()));
        Button nla = new Button("Create");
        nla.addActionListener(e -> desktop.put(new CreateOrganization(desktop)));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(nla, BorderLayout.SOUTH);
        add(mainPanel);
        for (Organization org : Engine.getOrganizations()) {
            listModel.addElement(org);
        }
        setIconifiable(true);
        setClosable(true);
    }

    private void openOrgView(Organization selectedOrg) {
        Engine.setOrganization(selectedOrg);
        String orgCode = selectedOrg.getId();
        Organization loc = match(orgCode);
        if (loc != null) {
            desktop.put(new OrgView(loc, desktop));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Not Found");
        }
    }

    private void openOrgViewById(String id) {
        Organization loc = match(id);
        if (loc != null) {
            desktop.put(new OrgView(loc, desktop));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Not Found");
        }
    }

    public static Organization match(String id) {
        for (Organization org : Engine.getOrganizations()) {
            if (org != null && org.getId().equals(id)) {
                return org;
            }
        }
        return null;
    }

    class OrgListRenderer extends JPanel implements ListCellRenderer<Organization> {

        private JLabel orgName;
        private JLabel orgId;

        public OrgListRenderer() {
            setLayout(new BorderLayout(2, 2));
            orgName = new JLabel();
            orgId = new JLabel();
            orgName.setFont(new Font("Arial", Font.BOLD, 16));
            orgId.setFont(new Font("Arial", Font.PLAIN, 12));
            add(orgName, BorderLayout.NORTH);
            add(orgId, BorderLayout.SOUTH);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Organization> list, Organization value, int index, boolean isSelected, boolean cellHasFocus) {
            orgName.setText(value.getName());
            orgId.setText(value.getId());
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