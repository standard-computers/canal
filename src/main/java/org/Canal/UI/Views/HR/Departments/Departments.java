package org.Canal.UI.Views.HR.Departments;

import org.Canal.Models.HumanResources.Department;
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
 * /DPTS
 */
public class Departments extends JInternalFrame {

    private DefaultListModel<Department> listModel;

    public Departments(DesktopState desktop) {
        super("Departments", false, true, false, true);
        setFrameIcon(new ImageIcon(Departments.class.getResource("/icons/departments.png")));
        listModel = new DefaultListModel<>();
        JList<Department> list = new JList<>(listModel);
        list.setCellRenderer(new DepartmentRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Department l = listModel.getElementAt(selectedIndex);
                        if (l != null) {
                            Department selected = Engine.getOrganization().getDepartment(l.getId());
                            desktop.put(new DepartmentView(selected));
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
                        desktop.put(Engine.router("/DPTS/" + inputText, desktop));
                    }
                }
            }
        });
        Button nla = new Button("Create Department");
        nla.addActionListener(_ -> desktop.put(Engine.router("/DPTS/NEW", desktop)));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(nla, BorderLayout.SOUTH);
        add(mainPanel);
        loadLocations();
    }

    private void loadLocations(){
        listModel.removeAllElements();
        ArrayList<Department> found = Engine.getOrganization().getDepartments();
        for (Department dept : found) {
            listModel.addElement(dept);
        }
    }

    static class DepartmentRenderer extends JPanel implements ListCellRenderer<Department> {

        private JLabel deptName;
        private JLabel deptId;
        private JLabel line1;

        public DepartmentRenderer() {
            setLayout(new GridLayout(3, 1));
            deptName = new JLabel();
            deptId = new JLabel();
            line1 = new JLabel();
            deptName.setFont(new Font("Arial", Font.BOLD, 16));
            deptId.setFont(new Font("Arial", Font.PLAIN, 12));
            line1.setFont(new Font("Arial", Font.PLAIN, 12));
            add(deptName);
            add(deptId);
            add(line1);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Department> list, Department value, int index, boolean isSelected, boolean cellHasFocus) {
            deptName.setText(value.getName());
            deptId.setText(value.getId());
            line1.setText("0");
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