package org.Canal.UI.Views.Users;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.User;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

/**
 * /USRS/NEW
 */
public class CreateUser extends LockeState {

    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JPanel canalAccess;
    private JTextField userIdField;
    private Selectable employees;
    private ArrayList<JCheckBox> checkboxes;
    private RSyntaxTextArea textArea;
    private JLabel accessCount;

    public CreateUser(DesktopState desktop, RefreshListener refreshListener) {

        super("Create User", "/USRS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateUser.class.getResource("/icons/create.png")));
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
        Form f = new Form();
        String puid = "U" + (10000 + (Engine.getUsers().size() + 1));
        userIdField = Elements.input(puid);
        employees = Selectables.employees();
        f.addInput(Elements.coloredLabel("New User ID", Constants.colors[10]), userIdField);
        f.addInput(Elements.coloredLabel("Employee", Constants.colors[9]), employees);

        JPanel again = new JPanel(new BorderLayout());
        again.add(Elements.header("Create New User", SwingConstants.LEFT), BorderLayout.NORTH);
        again.add(f, BorderLayout.CENTER);
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
        IconButton copy = new IconButton("Copy From", "open", "Export as CSV", "");
        IconButton review = new IconButton("Review", "review", "Review User");
        IconButton create = new IconButton("Create User", "execute", "Create User");
        accessCount = Elements.link("0 Accesses", "Total number of Locked Codes user has access to");
        tb.add(copy);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(create);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        create.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                User newUser = new User();
                newUser.setId(userIdField.getText().trim());
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

                    if(refreshListener != null){
                        refreshListener.refresh();
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "User creation failed because password encryption was not implemented.");
                }
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
        JButton sa = Elements.button("Select All");
        JButton dsa = Elements.button("Deselect All");
        ctrls.add(sa);
        ctrls.add(dsa);
        p.add(ctrls, BorderLayout.SOUTH);
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