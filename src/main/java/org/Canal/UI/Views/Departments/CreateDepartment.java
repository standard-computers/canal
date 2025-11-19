package org.Canal.UI.Views.Departments;

import org.Canal.Models.HumanResources.Department;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * /DPTS/NEW
 */
public class CreateDepartment extends LockeState {

    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JTextField deptIdField;
    private JTextField deptNameField;
    private Selectable manager;
    private DatePicker openedDatePicker;
    private JTextField locations;
    private Selectable orgs;

    public CreateDepartment(DesktopState desktop, RefreshListener refreshListener) {

        super("Create a Department", "/DPTS/NEW", false, true, false, true);
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Positions", positions());

        setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("New Department", SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(toolbar(), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create Department");
        create.addActionListener(_ -> {

            Department newDepartment = new Department();
            newDepartment.setId(deptIdField.getText());
            newDepartment.setOrganization(orgs.getSelectedValue());
            newDepartment.setLocation(locations.getText());
            newDepartment.setName(deptNameField.getText());
            newDepartment.setSupervisor(manager.getSelectedValue());

            Location selectedOrg = Engine.getLocation(orgs.getSelectedValue(), "ORGS");
            selectedOrg.addDepartment(newDepartment);
            selectedOrg.save();
            Engine.setOrganization(selectedOrg);

            if ((boolean) Engine.codex.getValue("DPTS", "item_created_alert")) {
                JOptionPane.showMessageDialog(null, "Department Created in ORG " + orgs.getSelectedValue());
            }
            dispose();

            if (refreshListener != null) refreshListener.refresh();

            if ((boolean) Engine.codex.getValue("DPTS", "auto_open_new")) {
                Engine.router("/DPTS/" + deptIdField.getText(), desktop);
            }
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-modify");
        rp.getActionMap().put("do-modify", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create.doClick();
            }
        });

        return tb;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String genId = "D0" + (Engine.getOrganization().getDepartments().size() + 1);

        deptIdField = Elements.input(genId, 18);
        deptNameField = Elements.input(18);
        manager = Selectables.employees();
        manager.editable();
        openedDatePicker = new DatePicker();
        locations = Elements.input();
        orgs = Selectables.organizations();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Department ID"), deptIdField);
        form.addInput(Elements.inputLabel("*Organization"), orgs);
        form.addInput(Elements.inputLabel("Location (optional)"), locations);
        form.addInput(Elements.inputLabel("Name"), deptNameField);
        form.addInput(Elements.inputLabel("Manager"), manager);
        form.addInput(Elements.inputLabel("Open Date"), openedDatePicker);
        general.add(form);

        return general;
    }

    private JPanel positions() {

        JPanel positions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalStrut(5));

        IconButton add = new IconButton("Assign Position", "add_rows", "Add Position");
        buttons.add(add);
        buttons.add(Box.createHorizontalStrut(5));

        IconButton remove = new IconButton("Remove Selected", "delete_rows", "Remove Selected Position");
        buttons.add(remove);

        positions.add(buttons, BorderLayout.SOUTH);

        return positions;
    }
}