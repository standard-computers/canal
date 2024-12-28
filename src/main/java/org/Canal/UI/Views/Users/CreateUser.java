package org.Canal.UI.Views.Users;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Crypter;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * /USRS/NEW
 */
public class CreateUser extends LockeState {

    private JPanel canalAccess;
    private ArrayList<JCheckBox> checkboxes;

    public CreateUser(){

        super("Create User", "/USRS/NEW", false, true, false, true);
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
        JTextField userIdField = Elements.input(puid);
        f.addInput(new Label("New User ID", new Color(178, 255, 102)), userIdField);
        Selectable empsOpts = Selectables.employees();
        f.addInput(new Label("Employee", new Color(102, 255, 178)), empsOpts);
        JTextArea pastAccess = new JTextArea();
        pastAccess.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handlePaste();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}

            private void handlePaste() {
                String clipboardText = getClipboardText();
                if (clipboardText != null && pastAccess.getText().contains(clipboardText)) {
                    canalAccess.removeAll();
                    checkboxes.clear();
                    String[] lines = clipboardText.split("\n");
                    for (String line : lines) {
                        JCheckBox checkBox = new JCheckBox(line.trim());
                        checkboxes.add(checkBox);
                        canalAccess.add(checkBox);
                    }
                    canalAccess.revalidate();
                    canalAccess.repaint();
                }
            }
        });
        f.addInput(Elements.label("Paste Access"), pastAccess);
        JButton make = Elements.button("Create User");
        canalAccess = prepareAccesses();
        JPanel again = new JPanel(new BorderLayout());
        again.add(f, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(canalAccess);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        again.add(scrollPane, BorderLayout.CENTER);
        JPanel ctrls = new JPanel(new GridLayout(1, 2));
        JButton sa = Elements.button("Select All");
        JButton dsa = Elements.button("Deselect All");
        ctrls.add(sa);
        ctrls.add(dsa);
        again.add(ctrls, BorderLayout.SOUTH);
        l.add(again, BorderLayout.CENTER);
        l.add(make, BorderLayout.SOUTH);
        add(l);
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
        make.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                User newUser = new User();
                newUser.setId(userIdField.getText().trim());
                newUser.setEmployee(empsOpts.getSelectedValue());
                ArrayList<String> accesses = new ArrayList<>();
                for(JCheckBox c : checkboxes){
                    if(c.isSelected()){
                        accesses.add(c.getText());
                    }
                }
                newUser.setAccesses(accesses);
                Employee emp = Engine.getEmployee(empsOpts.getSelectedValue());
                try {
                    String genHpv = Crypter.md5(emp.getName().split(" ")[emp.getName().split(" ").length - 1].toLowerCase() + "1234");
                    newUser.setHpv(genHpv);
                    Pipe.save("/USRS", newUser);
                    dispose();
                    JOptionPane.showMessageDialog(null, "User create for ORG " + Engine.getOrganization().getId());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "User creation failed because password encryption was not implemented.");
                }
            }
        });
    }

    private String getClipboardText() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return null;
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