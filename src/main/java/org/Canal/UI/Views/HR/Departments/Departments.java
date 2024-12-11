package org.Canal.UI.Views.HR.Departments;

import org.Canal.Models.HumanResources.Department;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
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
 * /DPTS
 */
public class Departments extends LockeState {

    private DefaultListModel<Department> listModel;

    public Departments(DesktopState desktop) {
        super("Departments", "/DPTS", false, true, false, true);
        setFrameIcon(new ImageIcon(Departments.class.getResource("/icons/departments.png")));
        listModel = new DefaultListModel<>();
        JList<Department> list = new JList<>(listModel);
        list.setCellRenderer(new DepartmentRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(280, 350));
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
                            JOptionPane.showMessageDialog(null, "Department Not Found");
                        }
                    }
                }
            }
        });
        JTextField direct = Elements.input("DEPTARTMENT ID");
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    if (!inputText.isEmpty()) {
                        desktop.put(Engine.router("/DPTS/" + inputText, desktop));
                    }
                }
            }
        });
        setLayout(new BorderLayout());
        JPanel controller = new JPanel(new GridLayout(1, 2));
        Selectable orgs = Selectables.organizations();
        controller.add(direct);
        controller.add(orgs);
        add(controller, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        loadLocations(orgs.getSelectedValue());
        orgs.addActionListener(_ -> loadLocations(orgs.getSelectedValue()));
    }

    private void loadLocations(String orgId){
        listModel.removeAllElements();
        ArrayList<Department> found = Engine.getLocation(orgId, "ORGS").getDepartments();
        for (Department dept : found) {
            listModel.addElement(dept);
        }
    }

    static class DepartmentRenderer extends JPanel implements ListCellRenderer<Department> {

        private JLabel departmentName;
        private JLabel departmentId;
        private JLabel employeeCount;

        public DepartmentRenderer() {
            setLayout(new GridLayout(3, 1));
            departmentName = new JLabel();
            departmentId = new JLabel();
            employeeCount = new JLabel();
            departmentName.setFont(new Font("Arial", Font.BOLD, 16));
            departmentId.setFont(new Font("Arial", Font.PLAIN, 12));
            employeeCount.setFont(new Font("Arial", Font.PLAIN, 12));
            add(departmentName);
            add(departmentId);
            add(employeeCount);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Department> list, Department value, int index, boolean isSelected, boolean cellHasFocus) {
            departmentName.setText(value.getName());
            departmentId.setText(value.getId());
            employeeCount.setText("0");
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