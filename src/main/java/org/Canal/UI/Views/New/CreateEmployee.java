package org.Canal.UI.Views.New;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Singleton.EmployeeView;
import org.Canal.Utils.*;
import javax.swing.*;
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
        JTextField empIdField = new JTextField(genId, 18);
        JTextField orgIdField = new JTextField(Engine.getOrganization().getId(), 18);
        JTextField empNameField = new JTextField(18);
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
        f.addInput(new Label("Full Name", UIManager.getColor("Label.foreground")), empNameField);
        f.addInput(new Label("Position", UIManager.getColor("Label.foreground")), position);
        f.addInput(new Label("Supervisor", UIManager.getColor("Label.foreground")), supervisor);
        f.addInput(new Label("Start Date", UIManager.getColor("Label.foreground")), startDatePicker);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button cr = new Button("Process");
        add(cr, BorderLayout.SOUTH);
        setIconifiable(true);
        setClosable(true);
        cr.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String empId = empIdField.getText().trim();
                String orgId = orgIdField.getText().trim();
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
                newEmployee.setStatus(LockeType.NEW);
                Pipe.save("/EMPS", newEmployee);
                dispose();
                JOptionPane.showMessageDialog(null, "Employee Created");
                desktop.put(new EmployeeView(newEmployee));
            }
        });
    }
}