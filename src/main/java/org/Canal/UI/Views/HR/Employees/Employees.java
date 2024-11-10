package org.Canal.UI.Views.HR.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
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
 * /EMPS
 */
public class Employees extends JInternalFrame {

    private DefaultListModel<Employee> listModel;

    public Employees(DesktopState desktop) {
        super("Employees", false, true, false, true);
        setFrameIcon(new ImageIcon(Employees.class.getResource("/icons/employees.png")));
        listModel = new DefaultListModel<>();
        JList<Employee> list = new JList<>(listModel);
        list.setCellRenderer(new EmployeeRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Employee item = listModel.getElementAt(selectedIndex);
                        desktop.put(Engine.router("/EMPS/" + item.getId(), desktop));
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
                        desktop.put(Engine.router("/EMPS/" + inputText, desktop));
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
        ArrayList<Employee> found = Engine.getEmployees();
        listModel.removeAllElements();
        for (Employee f : found) {
            listModel.addElement(f);
        }
    }

    static class EmployeeRenderer extends JPanel implements ListCellRenderer<Employee> {

        private JLabel employeeName;
        private JLabel employeeId;

        public EmployeeRenderer() {
            setLayout(new GridLayout(2, 1));
            employeeName = new Label("", new Color(83, 83, 83));
            employeeId = new JLabel();
            add(employeeName);
            add(employeeId);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Employee> list, Employee value, int index, boolean isSelected, boolean cellHasFocus) {
            employeeName.setText(value.getName());
            employeeId.setText(value.getId());
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