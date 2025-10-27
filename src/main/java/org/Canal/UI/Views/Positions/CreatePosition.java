package org.Canal.UI.Views.Positions;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /HR/POS/NEW
 * Create a new Position in an Organization.
 * Positions MUST be attached to a Department <em>/DPTS</em>
 */
public class CreatePosition extends LockeState {

    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JTextField positionIdField;
    private Selectable organizations;
    private JTextField positionNameField;
    private JTextField descriptionField;
    private Selectable departments;
    private JTextField compensationField;
    private JTextField availabilityField;
    private Selectable countries;
    private JCheckBox isHourly;
    private JCheckBox isBonusable;
    private JCheckBox isCommissionable;
    private JCheckBox autoPost;
    private ArrayList<Employee> employees = new ArrayList<>();
    private CustomTable employeesTable;

    public CreatePosition(DesktopState desktop, RefreshListener refreshListener) {

        super("Create a Position", "/HR/POS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreatePosition.class.getResource("/icons/create.png")));
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Assign Employees", employees());

        setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("New Position", SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(toolbar(), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Position data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Create Position");
        create.addActionListener(_ -> {

            Position position = new Position();
            position.setId(positionIdField.getText());
            position.setOrganization(organizations.getSelectedValue());
            position.setDepartment(departments.getSelectedValue());
            position.setName(positionNameField.getText());
            position.setDescription(descriptionField.getText());
            position.setCompensation(Double.parseDouble(compensationField.getText()));
            position.setAvailability(Integer.parseInt(availabilityField.getText()));
            position.setHourly(isHourly.isSelected());
            position.setBonus(isBonusable.isSelected());
            position.setCommission(isCommissionable.isSelected());
            position.setStatus(LockeStatus.NEW);
            Pipe.save("/HR/POS", position);

            if ((boolean) Engine.codex.getValue("HR/POS", "item_created_alert")) {
                JOptionPane.showMessageDialog(null, "Position succesfully created!");
            }
            dispose();

            if (refreshListener != null) refreshListener.refresh();

            if ((boolean) Engine.codex.getValue("HR/POS", "auto_open_new")) {
                desktop.put(new ViewPosition(position, desktop));
            }
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));

        return tb;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        positionIdField = Elements.input("R-DEPT-" + (Engine.getPositions().size() + 1));
        organizations = Selectables.organizations();
        positionNameField = Elements.input();
        descriptionField = Elements.input();
        departments = Selectables.departments();
        compensationField = Elements.input();
        availabilityField = Elements.input("1");
        countries = Selectables.countries();
        isHourly = new JCheckBox("Employee must clock in and out");
        isBonusable = new JCheckBox("Employee can earn bonuses");
        isCommissionable = new JCheckBox("Position is or can earn commission");
        autoPost = new JCheckBox("Post position as open?");

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Position ID"), positionIdField);
        form.addInput(Elements.inputLabel("*Organization"), organizations);
        form.addInput(Elements.inputLabel("Position Name"), positionNameField);
        form.addInput(Elements.inputLabel("Short Description"), descriptionField);
        form.addInput(Elements.inputLabel("Department"), departments);
        form.addInput(Elements.inputLabel("Compensation"), compensationField);
        form.addInput(Elements.inputLabel("Availability"), availabilityField);
        form.addInput(Elements.inputLabel("Comp. Class"), countries);
        form.addInput(Elements.inputLabel("Hourly?"), isHourly);
        form.addInput(Elements.inputLabel("Earns Bonuses"), isBonusable);
        form.addInput(Elements.inputLabel("Earns Commission"), isCommissionable);
        form.addInput(Elements.inputLabel("Auto Post"), autoPost);
        general.add(form);

        return general;
    }

    private CustomTable employeesTable() {

        String[] columns = new String[]{"ID", "Name"};
        ArrayList<Object[]> data = new ArrayList<>();
        for (Employee employee : employees) {
            data.add(new Object[]{employee.getId(), employee.getName(),});
        }
        return new CustomTable(columns, data);
    }

    private JPanel employees() {

        JPanel employeesPanel = new JPanel(new BorderLayout());

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        IconButton add = new IconButton("Add Employee", "add_rows", "Add Product");
        IconButton remove = new IconButton("Remove Selected", "delete_rows", "Remove Selected Product");
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(add);
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(remove);

        add.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String employeeId = JOptionPane.showInputDialog("Enter Employee ID");
                Employee em = Engine.getEmployee(employeeId);
                employees.add(em);
                CustomTable newTable = employeesTable();
                JScrollPane scrollPane = (JScrollPane) employeesTable.getParent().getParent();
                scrollPane.setViewportView(newTable);
                employeesTable = newTable;
                scrollPane.revalidate();
                scrollPane.repaint();
            }
        });

        remove.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = employeesTable.getSelectedRow();
                if (selectedRow != -1) {
                    String employeeId = employeesTable.getValueAt(selectedRow, 1).toString();
                    Employee em = Engine.getEmployee(employeeId);
                    if (em != null) {
                        employees.remove(em);
                        CustomTable newTable = employeesTable();
                        JScrollPane scrollPane = (JScrollPane) employeesTable.getParent().getParent();
                        scrollPane.setViewportView(newTable);
                        employeesTable = newTable;
                        scrollPane.revalidate();
                        scrollPane.repaint();
                    } else {
                        System.out.println("No employee selected");
                    }
                } else {
                    System.out.println("No employee selected");
                }
            }
        });

        employeesTable = employeesTable();
        employeesPanel.add(buttons, BorderLayout.NORTH);
        employeesPanel.add(new JScrollPane(employeesTable), BorderLayout.CENTER);
        return employeesPanel;
    }

    private void performReview() {

    }
}