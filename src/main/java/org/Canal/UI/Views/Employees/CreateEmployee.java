package org.Canal.UI.Views.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField addressL1Field;
    private JTextField addressL2Field;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField postalField;
    private JTextField phoneField;
    private JTextField emailField;
    private Selectable orgIdField;
    private JTextField locations;
    private Selectable supervisor;
    private Selectable countries;
    private Selectable ethnicities;
    private Selectable genders;
    private DatePicker startDatePicker = new DatePicker();
    private JCheckBox disabled;
    private JCheckBox veteren;

    public CreateEmployee(DesktopState desktop, RefreshListener refreshListener){

        super("Create Employee", "/EMPS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateEmployee.class.getResource("/icons/create.png")));
        this.desktop = desktop;
        this.refreshListener = refreshListener;

        JPanel main = new JPanel(new BorderLayout());
        CustomTabbedPane tabbedPane = new CustomTabbedPane();
        tabbedPane.addTab("General", general());
        tabbedPane.addTab("Address", locationInfo());
        tabbedPane.addTab("Personal", personalInfo());
        main.add(toolbar(), BorderLayout.NORTH);
        main.add(tabbedPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(Elements.header("New Employee", SwingConstants.LEFT), BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Refresh Data");
        create.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                String empId = "E" + (10000 + (Engine.getEmployees().size() + 1));
                String orgId = orgIdField.getSelectedValue().trim();
                String location = locations.getText().trim();
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String empSupervisor = supervisor.getSelectedValue();

                Employee newEmployee = new Employee();
                newEmployee.setId(empId);
                newEmployee.setOrg(orgId);
                newEmployee.setLocation(location);
                newEmployee.setName(firstName + " " + lastName);
                newEmployee.setFirstName(firstName);
                newEmployee.setLastName(lastName);
                newEmployee.setSupervisor(empSupervisor);
                newEmployee.setStartDate(startDatePicker.getSelectedDateString());
                newEmployee.setCreateDate(Constants.now());
                newEmployee.setStatus(LockeStatus.NEW);

                newEmployee.setLine1(addressL1Field.getText());
                newEmployee.setLine2(addressL2Field.getText());
                newEmployee.setCity(cityField.getText());
                newEmployee.setState(stateField.getText());
                newEmployee.setPostal(postalField.getText());
                newEmployee.setCountry(countries.getSelectedValue());
                newEmployee.setPhone(phoneField.getText());
                newEmployee.setEmail(emailField.getText());
                newEmployee.setEthnicity(ethnicities.getSelectedValue());
                newEmployee.setGender(genders.getSelectedValue());
                newEmployee.setDisability(disabled.isSelected());
                newEmployee.setVeteran(veteren.isSelected());

                Pipe.save("/EMPS", newEmployee);

                if((boolean) Engine.codex.getValue("EMPS", "item_created_alert")){
                    JOptionPane.showMessageDialog(null, "Employee Created");
                }
                dispose();

                if(refreshListener != null) refreshListener.refresh();

                if((boolean) Engine.codex.getValue("EMPS", "auto_open_new")){
                    desktop.put(new ViewEmployee(newEmployee, desktop, refreshListener));
                }
            }
        });
        tb.add(create);
        tb.setBorder(new EmptyBorder(5, 5, 5, 5));
        return tb;
    }

    private JPanel general(){

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();

        orgIdField = Selectables.organizations();
        locations = Elements.input();
        firstNameField = Elements.input();
        middleNameField = Elements.input();
        lastNameField = Elements.input();
        HashMap<String, String> availablePositions = new HashMap<>();
        Selectable position = new Selectable(availablePositions);
        position.editable();
        HashMap<String, String> availableSupervisors = new HashMap<>();
        supervisor = new Selectable(availableSupervisors);
        supervisor.editable();
        startDatePicker = new DatePicker();

        f.addInput(Elements.inputLabel("Organization"), orgIdField);
        f.addInput(Elements.inputLabel("Location (optional)"), locations);
        f.addInput(Elements.inputLabel("First Name"), firstNameField);
        f.addInput(Elements.inputLabel("Middle Name"), middleNameField);
        f.addInput(Elements.inputLabel("Last Name"), lastNameField);
        f.addInput(Elements.inputLabel("Position"), position);
        f.addInput(Elements.inputLabel("Supervisor"), supervisor);
        f.addInput(Elements.inputLabel("Start Date"), startDatePicker);
        general.add(f);

        return general;
    }

    private JPanel locationInfo(){

        JPanel locationInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f2 = new Form();

        addressL1Field = Elements.input(15);
        addressL2Field = Elements.input();
        cityField = Elements.input();
        stateField = Elements.input();
        postalField = Elements.input();
        countries = Selectables.countries();

        f2.addInput(Elements.inputLabel("Street Line 1"), addressL1Field);
        f2.addInput(Elements.inputLabel("Line 2"), addressL2Field);
        f2.addInput(Elements.inputLabel("City"), cityField);
        f2.addInput(Elements.inputLabel("State"), stateField);
        f2.addInput(Elements.inputLabel("Postal"), postalField);
        f2.addInput(Elements.inputLabel("Country"), Selectables.countries());
        locationInfo.add(f2);

        return locationInfo;
    }

    private JPanel personalInfo(){

        JPanel personalInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();

        emailField = Elements.input(15);
        phoneField = Elements.input();
        ethnicities = Selectables.ethnicities();
        genders = Selectables.genders();
        disabled = new JCheckBox();
        veteren = new JCheckBox();

        f.addInput(Elements.inputLabel("Email"), emailField);
        f.addInput(Elements.inputLabel("Phone"), phoneField);
        f.addInput(Elements.inputLabel("Ethnicity"), ethnicities);
        f.addInput(Elements.inputLabel("Gender"), genders);
        f.addInput(Elements.inputLabel("Disabled?"), disabled);
        f.addInput(Elements.inputLabel("Veteran?"), veteren);
        personalInfo.add(f);

        return personalInfo;
    }
}