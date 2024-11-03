package org.Canal.UI.Views.HR.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.DatePicker;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * /EMPS/NEW
 * This solely evolves around an employee.
 * Not a user of this system. All system actions
 */
public class CreateEmployee extends JInternalFrame {

    public CreateEmployee(DesktopState desktop){
        setTitle("Create Employee");
        setFrameIcon(new ImageIcon(CreateEmployee.class.getResource("/icons/create.png")));
        Form f = new Form();

        String genId = "E" + (10000 + (Engine.getEmployees().size() + 1));
        JTextField empIdField = Elements.input(genId, 15);
        Selectable orgIdField = Selectables.allOrgs();
        JTextField empNameField = Elements.input(15);
        HashMap<String, String> availablePositions = new HashMap<>();
        Selectable position = new Selectable(availablePositions);
        position.editable();
        HashMap<String, String> availableSupervisors = new HashMap<>();
        Selectable supervisor = new Selectable(availableSupervisors);
        supervisor.editable();
        DatePicker startDatePicker = new DatePicker();

        f.addInput(new Label("New Employee ID", UIManager.getColor("Label.foreground")), empIdField);
        f.addInput(new Label("Organization", Constants.colors[10]), orgIdField);
        Selectable locations = Selectables.allLocations();
        f.addInput(new Label("Location (optional)", Constants.colors[9]), locations);
        f.addInput(new Label("Full Name", Constants.colors[9]), empNameField);
        f.addInput(new Label("Position", Constants.colors[8]), position);
        f.addInput(new Label("Supervisor", Constants.colors[7]), supervisor);
        f.addInput(new Label("Start Date", Constants.colors[6]), startDatePicker);
        setLayout(new BorderLayout());

        Form f2 = new Form();
        JTextField empAddressL1 = Elements.input(15);
        JTextField empAddressL2 = Elements.input(15);
        JTextField empAddressCity = Elements.input(15);
        JTextField empAddressState = Elements.input(15);
        JTextField empAddressPostal = Elements.input(15);
        f2.addInput(new Label("Street Line 1", UIManager.getColor("Label.foreground")), empAddressL1);
        f2.addInput(new Label("Street Line 2", UIManager.getColor("Label.foreground")), empAddressL2);
        f2.addInput(new Label("City", UIManager.getColor("Label.foreground")), empAddressCity);
        f2.addInput(new Label("State", UIManager.getColor("Label.foreground")), empAddressState);
        f2.addInput(new Label("Postal", UIManager.getColor("Label.foreground")), empAddressPostal);
        f2.addInput(new Label("Country", UIManager.getColor("Label.foreground")), Selectables.countries());
        f2.setBorder(new TitledBorder("Employee Residential Address"));
        add(f2, BorderLayout.EAST);
        add(f, BorderLayout.CENTER);
        Button cr = new Button("Process");
        add(cr, BorderLayout.SOUTH);
        setIconifiable(true);
        setClosable(true);
        cr.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String empId = empIdField.getText().trim();
                String orgId = orgIdField.getSelectedValue().trim();
                String location = locations.getSelectedValue();
                String empName = empNameField.getText().trim();
                String empSupervisor = supervisor.getSelectedValue();
                Employee newEmployee = new Employee();
                newEmployee.setId(empId);
                newEmployee.setOrg(orgId);
                newEmployee.setLocation(location);
                newEmployee.setName(empName);
                newEmployee.setSupervisor(empSupervisor);
                newEmployee.setStartDate(startDatePicker.getSelectedDateString());
                newEmployee.setCreateDate(Constants.now());
                newEmployee.setStatus(LockeStatus.NEW);
                Pipe.save("/EMPS", newEmployee);
                dispose();
                JOptionPane.showMessageDialog(null, "Employee Created");
                desktop.put(new EmployeeView(newEmployee));
            }
        });
    }
}