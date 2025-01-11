package org.Canal.UI.Views.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.DatePicker;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
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
public class CreateEmployee extends LockeState {

    private JTextField empIdField, empNameField, empAddressL1, empAddressL2,
            empAddressCity, empAddressState, empAddressPostal, empPhone, empEmail;
    private Selectable orgIdField, locations, supervisor, countries, ethnicities, genders;
    private DatePicker startDatePicker = new DatePicker();
    private JCheckBox isVeteran;

    public CreateEmployee(DesktopState desktop, boolean autoMakeUser){
        super("Create Employee", "/EMPS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateEmployee.class.getResource("/icons/create.png")));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(basicInfo(), "Basic Info");
        tabbedPane.add(locationInfo(), "Address");
        tabbedPane.add(personalInfo(), "Personal");
        setLayout(new BorderLayout());
        add(Elements.header("New Employee", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        JButton createEmployee = Elements.button("Process");
        add(createEmployee, BorderLayout.SOUTH);
        createEmployee.addMouseListener(new MouseAdapter() {
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

                newEmployee.setLine1(empAddressL1.getText());
                newEmployee.setLine2(empAddressL2.getText());
                newEmployee.setCity(empAddressCity.getText());
                newEmployee.setState(empAddressState.getText());
                newEmployee.setPostal(empAddressPostal.getText());
                newEmployee.setCountry(countries.getSelectedValue());
                newEmployee.setPhone(empPhone.getText());
                newEmployee.setEmail(empEmail.getText());

                Pipe.save("/EMPS", newEmployee);
                dispose();
                JOptionPane.showMessageDialog(null, "Employee Created");
                desktop.put(new ViewEmployee(newEmployee));
            }
        });
    }

    private JPanel basicInfo(){
        Form f = new Form();
        String genId = "E" + (10000 + (Engine.getEmployees().size() + 1));
        empIdField = Elements.input(genId, 15);
        orgIdField = Selectables.organizations();
        empNameField = Elements.input(15);
        HashMap<String, String> availablePositions = new HashMap<>();
        Selectable position = new Selectable(availablePositions);
        position.editable();
        HashMap<String, String> availableSupervisors = new HashMap<>();
        supervisor = new Selectable(availableSupervisors);
        supervisor.editable();
        startDatePicker = new DatePicker();
        f.addInput(Elements.coloredLabel("New Employee ID", UIManager.getColor("Label.foreground")), empIdField);
        f.addInput(Elements.coloredLabel("Organization", Constants.colors[10]), orgIdField);
        locations = Selectables.allLocations();
        f.addInput(Elements.coloredLabel("Location (optional)", Constants.colors[9]), locations);
        f.addInput(Elements.coloredLabel("Full Name", Constants.colors[9]), empNameField);
        f.addInput(Elements.coloredLabel("Position", Constants.colors[8]), position);
        f.addInput(Elements.coloredLabel("Supervisor", Constants.colors[7]), supervisor);
        f.addInput(Elements.coloredLabel("Start Date", Constants.colors[6]), startDatePicker);
        return f;
    }

    private JPanel locationInfo(){
        Form f2 = new Form();
        empAddressL1 = Elements.input(15);
        empAddressL2 = Elements.input(15);
        empAddressCity = Elements.input(15);
        empAddressState = Elements.input(15);
        empAddressPostal = Elements.input(15);
        countries = Selectables.countries();
        f2.addInput(Elements.coloredLabel("Street Line 1", UIManager.getColor("Label.foreground")), empAddressL1);
        f2.addInput(Elements.coloredLabel("Street Line 2", UIManager.getColor("Label.foreground")), empAddressL2);
        f2.addInput(Elements.coloredLabel("City", UIManager.getColor("Label.foreground")), empAddressCity);
        f2.addInput(Elements.coloredLabel("State", UIManager.getColor("Label.foreground")), empAddressState);
        f2.addInput(Elements.coloredLabel("Postal", UIManager.getColor("Label.foreground")), empAddressPostal);
        f2.addInput(Elements.coloredLabel("Country", UIManager.getColor("Label.foreground")), Selectables.countries());
        return f2;
    }

    private JPanel personalInfo(){
        Form f = new Form();
        empPhone = Elements.input(15);
        empEmail = Elements.input(15);
        ethnicities = Selectables.ethnicities();
        genders = Selectables.genders();
        isVeteran = new JCheckBox();
        f.addInput(Elements.coloredLabel("Phone", UIManager.getColor("Label.foreground")), empPhone);
        f.addInput(Elements.coloredLabel("Email", UIManager.getColor("Label.foreground")), empEmail);
        f.addInput(Elements.coloredLabel("Ethnicity", UIManager.getColor("Label.foreground")), ethnicities);
        f.addInput(Elements.coloredLabel("Gender", UIManager.getColor("coloredLabel.foreground")), genders);
        f.addInput(Elements.coloredLabel("Veteran?", UIManager.getColor("coloredLabel.foreground")), isVeteran);
        return f;
    }
}