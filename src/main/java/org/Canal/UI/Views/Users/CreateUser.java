package org.Canal.UI.Views.Users;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

/**
 * /USRS/NEW
 */
public class CreateUser extends LockeState {

    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JPanel canalAccess;
    private Selectable employees;
    private ArrayList<JCheckBox> checkboxes;
    private RSyntaxTextArea textArea;
    private JLabel accessCount;

    public CreateUser(DesktopState desktop, RefreshListener refreshListener) {

        super("Create User", "/USRS/NEW", false, true, false, true);
        this.desktop = desktop;
        this.refreshListener = refreshListener;
        if(Engine.getEmployees().isEmpty()){
            JOptionPane.showMessageDialog(null, "No employees to attach to!");
            try {
                setClosed(true);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }

        employees = Selectables.employees();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Employee"), employees);

        JPanel again = new JPanel(new BorderLayout());
        again.add(Elements.header("Create New User", SwingConstants.LEFT), BorderLayout.NORTH);
        again.add(form, BorderLayout.CENTER);
        again.add(toolbar(), BorderLayout.SOUTH);
        add(again, BorderLayout.NORTH);

        CustomTabbedPane accessControl = new CustomTabbedPane();
        accessControl.addTab("Checklist", accessList());
        accessControl.addTab("Paste Access", pasteAccess());
        add(accessControl, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton copy = new IconButton("Copy From", "open", "Export as CSV", "");
        copy.addActionListener(_ -> {

            String userId = JOptionPane.showInputDialog("Enter User ID");
            User u = Engine.getUser(userId);
            employees.setSelectedValue(u.getEmployee());
        });
        tb.add(copy);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review User");
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create User", "execute", "Create User");
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        accessCount = Elements.link("0 Accesses", "Total number of Locked Codes user has access to");

        create.addActionListener(_ -> {
            User newUser = new User();
            String userId = Engine.generateId("USRS");
            newUser.setId(userId);
            newUser.setEmployee(employees.getSelectedValue());
            ArrayList<String> accesses = new ArrayList<>();
            for(JCheckBox c : checkboxes){
                if(c.isSelected()){
                    accesses.add(c.getText());
                }
            }
            newUser.setAccesses(accesses);
            Employee emp = Engine.getEmployee(employees.getSelectedValue());
            try {
                String genHpv = Crypter.md5(emp.getName().split(" ")[emp.getName().split(" ").length - 1].toLowerCase() + "1234");
                newUser.setHpv(genHpv);
                Pipe.save("/USRS", newUser);
                dispose();
                JOptionPane.showMessageDialog(null, "User create for ORG " + Engine.getOrganization().getId());

                if(refreshListener != null) refreshListener.refresh();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "User creation failed because password encryption was not implemented.");
            }
        });

        return tb;
    }

    private JPanel accessList(){

        JPanel p = new JPanel(new BorderLayout());
        canalAccess = prepareAccesses();
        JScrollPane scrollPane = new JScrollPane(canalAccess);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel ctrls = new JPanel(new GridLayout(1, 2));
        JButton selectAll = Elements.button("Select All");
        selectAll.addActionListener(_ -> {
            checkboxes.forEach(cb -> cb.setSelected(true));
            repaint();
        });
        ctrls.add(selectAll);

        JButton deselectAll = Elements.button("Deselect All");
        deselectAll.addActionListener(_ -> {
            checkboxes.forEach(cb -> cb.setSelected(false));
            repaint();
        });
        ctrls.add(deselectAll);

        p.add(ctrls, BorderLayout.SOUTH);
        p.add(scrollPane, BorderLayout.CENTER);

        return p;
    }

    private RTextScrollPane pasteAccess(){

        textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        textArea.setCodeFoldingEnabled(true);
        textArea.setLineWrap(false);
        return new RTextScrollPane(textArea);
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