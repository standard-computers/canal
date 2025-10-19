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
 * /HR/POS/MOD/$
 * Modify a Position
 */
public class ModifyPosition extends LockeState {

    private Position position;
    private DesktopState desktop;
    private RefreshListener refreshListener;
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
    private ArrayList<Employee> employees = new ArrayList<>();
    private CustomTable employeesTable;

    public ModifyPosition(Position position, DesktopState desktop, RefreshListener refreshListener) {

        super("Modify a Position", "/HR/POS/MOD/" + position.getId());
        setFrameIcon(new ImageIcon(ModifyPosition.class.getResource("/icons/modify.png")));
        this.position = position;
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Assign Employees", employees());

        setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("Modify " + position.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(toolbar(), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Position data");
        review.addActionListener(_ -> performReview());
        toolbar.add(review);
        toolbar.add(Box.createHorizontalStrut(5));

        IconButton save = new IconButton("Save", "save", "Save changes");
        save.addActionListener(_ -> {

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
            position.save();

            if ((boolean) Engine.codex.getValue("HR/POS", "item_created_alert")) {
                JOptionPane.showMessageDialog(null, "Position succesfully created!");
            }
            dispose();

            if (refreshListener != null) refreshListener.refresh();

            if ((boolean) Engine.codex.getValue("HR/POS", "auto_open_new")) {
                desktop.put(new ViewPosition(position, desktop));
            }
        });
        toolbar.add(save);
        toolbar.add(Box.createHorizontalStrut(5));

        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        organizations = Selectables.organizations();
        positionNameField = Elements.input(position.getName());
        descriptionField = Elements.input(position.getDescription());
        departments = Selectables.departments();
        departments.setSelectedValue(position.getDepartment());
        compensationField = Elements.input(String.valueOf(position.getCompensation()));
        availabilityField = Elements.input(String.valueOf(position.getAvailability()));
        countries = Selectables.countries();
        isHourly = new JCheckBox("Employee must clock in and out", position.isHourly());
        isBonusable = new JCheckBox("Employee can earn bonuses", position.isBonus());
        isCommissionable = new JCheckBox("Position is or can earn commission", position.isCommission());

        Form form = new Form();
        form.addInput(Elements.coloredLabel("*Organization", UIManager.getColor("Label.foreground")), organizations);
        form.addInput(Elements.coloredLabel("Position Name", Constants.colors[0]), positionNameField);
        form.addInput(Elements.coloredLabel("Short Description", Constants.colors[1]), descriptionField);
        form.addInput(Elements.coloredLabel("Department", Constants.colors[2]), departments);
        form.addInput(Elements.coloredLabel("Compensation", Constants.colors[3]), compensationField);
        form.addInput(Elements.coloredLabel("Availability", Constants.colors[4]), availabilityField);
        form.addInput(Elements.coloredLabel("Comp. Class", Constants.colors[5]), countries);
        form.addInput(Elements.coloredLabel("Hourly?", Constants.colors[6]), isHourly);
        form.addInput(Elements.coloredLabel("Earns Bonuses", Constants.colors[7]), isBonusable);
        form.addInput(Elements.coloredLabel("Earns Commission", Constants.colors[8]), isCommissionable);
        general.add(form);

        return general;
    }

    private CustomTable employeesTable() {
        String[] columns = new String[]{
                "ID",
                "Name"
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Employee employee : employees) {
            data.add(new Object[]{
                    employee.getId(),
                    employee.getName(),
            });
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