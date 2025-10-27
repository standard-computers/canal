package org.Canal.UI.Views.People;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Employees.ViewEmployee;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * /PPL/NEW
 * This solely evolves around an employee.
 * Not a user of this system. All system actions
 */
public class CreatePerson extends LockeState {

    private JTextField empIdField;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField empAddressL1;
    private JTextField empAddressL2;
    private JTextField empAddressCity;
    private JTextField empAddressState;
    private JTextField empAddressPostal;
    private JTextField empPhone;
    private JTextField empEmail;
    private Selectable orgIdField;
    private Selectable locations;
    private Selectable supervisor;
    private Selectable countries;
    private Selectable ethnicities;
    private Selectable genders;
    private DatePicker startDatePicker = new DatePicker();
    private JCheckBox disabled;
    private JCheckBox veteren;

    public CreatePerson(DesktopState desktop, boolean autoMakeUser) {
        super("Create Person", "/PPL/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreatePerson.class.getResource("/icons/create.png")));

        CustomTabbedPane tabbedPane = new CustomTabbedPane();
        tabbedPane.addTab("General", general());
        tabbedPane.addTab("Address", locationInfo());
        tabbedPane.addTab("Personal", personalInfo());
        setLayout(new BorderLayout());
        add(Elements.header("New Person", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        JButton createEmployee = Elements.button("Process");
        add(createEmployee, BorderLayout.SOUTH);
        createEmployee.addActionListener(e -> {

            String empId = empIdField.getText().trim();
            String orgId = orgIdField.getSelectedValue().trim();
            String location = locations.getSelectedValue();
            String firstname = firstNameField.getText().trim();
            String middlename = middleNameField.getText().trim();
            String lastname = lastNameField.getText().trim();
            String empSupervisor = supervisor.getSelectedValue();
            Employee employee = new Employee();
            employee.setId(empId);
            employee.setOrg(orgId);
            employee.setLocation(location);
            employee.setFirstName(firstname);
            employee.setMiddleName(middlename);
            employee.setLastName(lastname);
            employee.setSupervisor(empSupervisor);
            employee.setStartDate(startDatePicker.getSelectedDateString());
            employee.setCreateDate(Constants.now());
            employee.setStatus(LockeStatus.NEW);

            employee.setLine1(empAddressL1.getText());
            employee.setLine2(empAddressL2.getText());
            employee.setCity(empAddressCity.getText());
            employee.setState(empAddressState.getText());
            employee.setPostal(empAddressPostal.getText());
            employee.setCountry(countries.getSelectedValue());
            employee.setPhone(empPhone.getText());
            employee.setEmail(empEmail.getText());
            employee.setDisability(disabled.isSelected());
            employee.setVeteran(veteren.isSelected());

            Pipe.save("/PPL", employee);
            dispose();
            JOptionPane.showMessageDialog(null, "Person Created");
            desktop.put(new ViewEmployee(employee, desktop, null));
        });
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String genId = "1-" + (10000 + (Engine.getPeople().size() + 1));
        empIdField = Elements.input(genId, 15);
        orgIdField = Selectables.organizations();
        firstNameField = Elements.input(15);
        middleNameField = Elements.input(15);
        lastNameField = Elements.input(15);
        HashMap<String, String> availablePositions = new HashMap<>();
        Selectable position = new Selectable(availablePositions);
        position.editable();
        HashMap<String, String> availableSupervisors = new HashMap<>();
        supervisor = new Selectable(availableSupervisors);
        supervisor.editable();
        startDatePicker = new DatePicker();
        locations = Selectables.allLocations();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New ID"), empIdField);
        form.addInput(Elements.inputLabel("Organization"), orgIdField);
        form.addInput(Elements.inputLabel("Location (optional)"), locations);
        form.addInput(Elements.inputLabel("First Name"), firstNameField);
        form.addInput(Elements.inputLabel("Middle Name"), middleNameField);
        form.addInput(Elements.inputLabel("Last Name"), lastNameField);
        form.addInput(Elements.inputLabel("Position"), position);
        form.addInput(Elements.inputLabel("Supervisor"), supervisor);
        form.addInput(Elements.inputLabel("Start Date"), startDatePicker);
        general.add(form);

        return general;
    }

    private JPanel locationInfo() {

        JPanel locationInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        empAddressL1 = Elements.input(15);
        empAddressL2 = Elements.input(15);
        empAddressCity = Elements.input(15);
        empAddressState = Elements.input(15);
        empAddressPostal = Elements.input(15);
        countries = Selectables.countries();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Street Line 1"), empAddressL1);
        form.addInput(Elements.inputLabel("Street Line 2"), empAddressL2);
        form.addInput(Elements.inputLabel("City"), empAddressCity);
        form.addInput(Elements.inputLabel("State"), empAddressState);
        form.addInput(Elements.inputLabel("Postal"), empAddressPostal);
        form.addInput(Elements.inputLabel("Country"), Selectables.countries());
        locationInfo.add(form);

        return locationInfo;
    }

    private JPanel personalInfo() {

        JPanel personalInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        empPhone = Elements.input(15);
        empEmail = Elements.input(15);
        ethnicities = Selectables.ethnicities();
        genders = Selectables.genders();
        disabled = new JCheckBox();
        veteren = new JCheckBox();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Phone"), empPhone);
        form.addInput(Elements.inputLabel("Email"), empEmail);
        form.addInput(Elements.inputLabel("Ethnicity"), ethnicities);
        form.addInput(Elements.inputLabel("Gender"), genders);
        form.addInput(Elements.inputLabel("Disabled?"), veteren);
        form.addInput(Elements.inputLabel("Veteran?"), veteren);
        personalInfo.add(form);

        return personalInfo;
    }
}