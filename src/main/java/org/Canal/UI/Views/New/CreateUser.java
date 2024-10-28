package org.Canal.UI.Views.New;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * /USRS/NEW
 */
public class CreateUser extends JInternalFrame {

    private JPanel tCodeAccesses;
    private ArrayList<JCheckBox> checkboxes;

    public CreateUser(){
        setTitle("Create User");
        setFrameIcon(new ImageIcon(CreateUser.class.getResource("/icons/create.png")));
        if(Engine.getEmployees().isEmpty()){
            JOptionPane.showMessageDialog(null, "No employees to attach to!");
            try {
                setClosed(true);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }
        JPanel l = new JPanel(new BorderLayout());
        Form f = new Form();
        String puid = "U" + (10000 + (Engine.getUsers().size() + 1));
        f.addInput(new Label("New User ID", new Color(178, 255, 102)), new Copiable(puid));
        HashMap<String, String> emps = new HashMap();
        for(Employee emp : Engine.getEmployees()){
            emps.put(emp.getId() + " – " + emp.getName(), emp.getId());
        }
        Selectable empsOpts = new Selectable(emps);
        empsOpts.editable();
        f.addInput(new Label("Employee", new Color(102, 255, 178)), empsOpts);
        JTextArea pastAccess = new JTextArea();
        f.addInput(Labels.label("Paste Access"), pastAccess);
        Button cr = new Button("Process");
        cr.color(new Color(102, 204, 255));
        tCodeAccesses = prepareAccesses();
        JPanel again = new JPanel(new BorderLayout());
        again.add(f, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(tCodeAccesses);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        again.add(scrollPane, BorderLayout.CENTER);
        JPanel ctrls = new JPanel(new GridLayout(1, 2));
        JButton sa = new JButton("Select All");
        JButton dsa = new JButton("Deselect All");
        ctrls.add(sa);
        ctrls.add(dsa);
        again.add(ctrls, BorderLayout.SOUTH);
        l.add(again, BorderLayout.CENTER);
        l.add(cr, BorderLayout.SOUTH);
        add(l);
        setResizable(false);
        setIconifiable(true);
        setClosable(true);
        sa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(true));
                repaint();
            }
        });
        dsa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                checkboxes.forEach(cb -> cb.setSelected(false));
                repaint();
            }
        });
        cr.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                User newUser = new User();
                newUser.setId(puid);
                newUser.setEmployee(empsOpts.getSelectedValue());
                ArrayList<String> accesses = new ArrayList<>();
                for(JCheckBox c : checkboxes){
                    if(c.isSelected()){
                        accesses.add(c.getText());
                    }
                }
                newUser.setAccesses(accesses);
                Pipe.save("/USRS", newUser);
                dispose();
                JOptionPane.showMessageDialog(null, "User create for ORG " + Engine.getOrganization().getId());
            }
        });
    }

    private JPanel prepareAccesses() {
        checkboxes = new ArrayList<>();
        ArrayList<String> allTransactions = Constants.getAllTransactions();
        for (String transaction : allTransactions) {
            JCheckBox checkbox = new JCheckBox(transaction);
            checkboxes.add(checkbox);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (JCheckBox checkbox : checkboxes) {
            panel.add(checkbox);
        }
        return panel;
    }
}