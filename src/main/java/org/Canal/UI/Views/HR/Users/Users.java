package org.Canal.UI.Views.HR.Users;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Label;
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
 * /USRS
 * List of Users registerd in Canal attached to selected Organization.
 */
public class Users extends LockeState {

    private DefaultListModel<User> listModel;

    public Users(DesktopState desktop) {
        super("Users", "/USRS", false, true, false, true);
        setFrameIcon(new ImageIcon(Users.class.getResource("/icons/employees.png")));
        listModel = new DefaultListModel<>();
        JList<User> list = new JList<>(listModel);
        list.setCellRenderer(new UserRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        User item = listModel.getElementAt(selectedIndex);
                        desktop.put(Engine.router("/USRS/" + item.getId(), desktop));
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
                        desktop.put(Engine.router("/USRS/" + inputText, desktop));
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
        ArrayList<User> found = Engine.getUsers();
        listModel.removeAllElements();
        for (User f : found) {
            listModel.addElement(f);
        }
    }

    static class UserRenderer extends JPanel implements ListCellRenderer<User> {

        private JLabel employeeName;
        private JLabel userId;
        private JLabel employeeId;

        public UserRenderer() {
            setLayout(new GridLayout(3, 1));
            employeeName = new Label("", new Color(83, 83, 83));
            userId = new JLabel();
            employeeId = new JLabel();
            add(employeeName);
            add(userId);
            add(employeeId);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends User> list, User value, int index, boolean isSelected, boolean cellHasFocus) {
            Employee thisEmployee = Engine.getEmployee(value.getEmployee());
            employeeName.setText(thisEmployee.getName());
            userId.setText(value.getId());
            employeeId.setText(value.getEmployee());
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