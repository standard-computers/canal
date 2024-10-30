package org.Canal.UI.Views.HR.Employees;

import org.Canal.Models.HumanResources.Employee;
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
 * /EMPS
 */
public class Employees extends JInternalFrame {

    private DefaultListModel<Employee> listModel;

    public Employees(DesktopState desktop) {
        setTitle("Employees");
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
        JTextField direct = new JTextField();
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    System.out.println(inputText);
                    if (!inputText.isEmpty()) {
                        desktop.put(Engine.router("/EMPS/" + inputText, desktop));
                    }
                }
            }
        });
        Button nla = new Button("Add an Employee");
        nla.addActionListener(_ -> desktop.put(new CreateEmployee(desktop)));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(nla, BorderLayout.SOUTH);
        add(mainPanel);
        loadFlexes();
        setIconifiable(true);
        setClosable(true);
    }

    private void loadFlexes(){
        Engine.load();
        ArrayList<Employee> found = Engine.getEmployees();
        listModel.removeAllElements();
        for (Employee f : found) {
            listModel.addElement(f);
        }
    }

    class EmployeeRenderer extends JPanel implements ListCellRenderer<Employee> {

        private JLabel employeeName;
        private JLabel employeeId;

        public EmployeeRenderer() {
            setLayout(new GridLayout(4, 1));
            employeeName = new JLabel();
            employeeId = new JLabel();
            employeeName.setFont(new Font("Arial", Font.BOLD, 16));
            employeeId.setFont(new Font("Arial", Font.PLAIN, 12));
            add(employeeName);
            add(employeeId);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Employee> list, Employee value, int index, boolean isSelected, boolean cellHasFocus) {
            employeeName.setText(value.getName());
            employeeId.setText(value.getId());
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