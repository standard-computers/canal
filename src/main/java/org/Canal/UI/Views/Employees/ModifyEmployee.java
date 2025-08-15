package org.Canal.UI.Views.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

/**
 * /EMPS/MOD/$
 */
public class ModifyEmployee extends LockeState {

    private Employee employee;
    private DesktopState desktop;
    private RefreshListener refreshListener;
    private JTextField nameField;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField addressL1Field;
    private JTextField addressL2Field;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField postalField;
    private Selectable countryField;
    private JTextField phoneField;
    private JTextField emailField;
    private Selectable orgIdField;
    private JTextField locationField;
    private Selectable supervisor;
    private Selectable ethnicities;
    private Selectable genders;
    private DatePicker startDatePicker = new DatePicker();
    private JCheckBox disabled;
    private JCheckBox veteren;

    public ModifyEmployee(Employee employee, DesktopState desktop, RefreshListener refreshListener){

        super("Modify " + employee.getName(), "/EMPS/MOD/" + employee.getId(), false, true, false, true);
        setFrameIcon(new ImageIcon(ModifyEmployee.class.getResource("/icons/modify.png")));
        this.employee = employee;
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
        add(Elements.header("Modify " + employee.getName(), SwingConstants.LEFT), BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
    }

    private JPanel toolbar() {

        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton save = new IconButton("Save", "save", "Save changes");
        save.addActionListener(e -> {

            employee.setLocation(locationField.getText());
            employee.setName(nameField.getText());
            employee.setFirstName(firstNameField.getText());
            employee.setMiddleName(middleNameField.getText());
            employee.setLastName(lastNameField.getText());
            employee.setLine1(addressL1Field.getText());
            employee.setLine2(addressL2Field.getText());
            employee.setCity(cityField.getText());
            employee.setState(stateField.getText());
            employee.setPostal(postalField.getText());
            employee.setCountry(countryField.getSelectedValue());
            employee.setEmail(emailField.getText());
            employee.setPhone(phoneField.getText());
            employee.setEthnicity(ethnicities.getSelectedValue());
            employee.setGender(genders.getSelectedValue());
            employee.setDisability(disabled.isSelected());
            employee.setVeteran(veteren.isSelected());

            employee.save();

            if(refreshListener != null){
                refreshListener.refresh();
            }

           if((boolean) Engine.codex.getValue("EMPS", "dispose_on_save")){
               dispose();
           }
        });
        tb.add(save);
        tb.add(Box.createHorizontalStrut(5));

        if((boolean) Engine.codex.getValue("EMPS", "allow_archival")){
            IconButton archive = new IconButton("Archive", "archive", "Archive item");
            archive.addActionListener(e -> {

            });
            tb.add(archive);
            tb.add(Box.createHorizontalStrut(5));
        }

        if((boolean) Engine.codex.getValue("EMPS", "allow_deletion")){
            IconButton delete = new IconButton("Delete", "delete", "Delete item");
            delete.addActionListener(e -> {

            });
            tb.add(delete);
            tb.add(Box.createHorizontalStrut(5));
        }

        tb.setBorder(new EmptyBorder(5, 5, 5, 5));

        return tb;
    }

    private JPanel general(){

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();

        orgIdField = Selectables.organizations();
        orgIdField.setSelectedValue(employee.getOrg());

        nameField = Elements.input(employee.getName());

        firstNameField = Elements.input(employee.getFirstName());

        middleNameField = Elements.input(employee.getMiddleName());

        lastNameField = Elements.input(employee.getLastName());

        HashMap<String, String> availablePositions = new HashMap<>();
        Selectable position = new Selectable(availablePositions);
        position.editable();
        HashMap<String, String> availableSupervisors = new HashMap<>();
        supervisor = new Selectable(availableSupervisors);
        supervisor.editable();
        startDatePicker = new DatePicker();
//     ToDO   startDatePicker.setSelectedDate(employee.getStartDate());

        f.addInput(Elements.coloredLabel("Organization", Constants.colors[10]), orgIdField);
        locationField = Elements.input(employee.getLocation(), 15);

        f.addInput(Elements.coloredLabel("Location", Constants.colors[9]), locationField);
        f.addInput(Elements.coloredLabel("Name (Nickname)", Constants.colors[8]), nameField);
        f.addInput(Elements.coloredLabel("First Name", Constants.colors[8]), firstNameField);
        f.addInput(Elements.coloredLabel("Middle Name", Constants.colors[7]), middleNameField);
        f.addInput(Elements.coloredLabel("Last Name", Constants.colors[7]), lastNameField);
        f.addInput(Elements.coloredLabel("Position", Constants.colors[6]), position);
        f.addInput(Elements.coloredLabel("Supervisor", Constants.colors[5]), supervisor);
        f.addInput(Elements.coloredLabel("Start Date", Constants.colors[4]), startDatePicker);
        general.add(f);
        return general;
    }

    private JPanel locationInfo(){

        JPanel locationInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f2 = new Form();

        addressL1Field = Elements.input(employee.getLine1(), 15);

        addressL2Field = Elements.input(employee.getLine2());

        cityField = Elements.input(employee.getCity());

        stateField = Elements.input(employee.getState());

        postalField = Elements.input(employee.getPostal());

        countryField = Selectables.countries();
        countryField.setSelectedValue(employee.getCountry());

        f2.addInput(Elements.coloredLabel("Street Line 1", UIManager.getColor("Label.foreground")), addressL1Field);
        f2.addInput(Elements.coloredLabel("Line 2", UIManager.getColor("Label.foreground")), addressL2Field);
        f2.addInput(Elements.coloredLabel("City", UIManager.getColor("Label.foreground")), cityField);
        f2.addInput(Elements.coloredLabel("State", UIManager.getColor("Label.foreground")), stateField);
        f2.addInput(Elements.coloredLabel("Postal", UIManager.getColor("Label.foreground")), postalField);
        f2.addInput(Elements.coloredLabel("Country", UIManager.getColor("Label.foreground")), countryField);
        locationInfo.add(f2);

        return locationInfo;
    }

    private JPanel personalInfo(){

        JPanel personalInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Form f = new Form();

        emailField = Elements.input(employee.getEmail(), 15);

        phoneField = Elements.input(employee.getPhone());

        ethnicities = Selectables.ethnicities();
        ethnicities.setSelectedValue(employee.getEthnicity());

        genders = Selectables.genders();
        genders.setSelectedValue(employee.getGender());

        disabled = new JCheckBox("", employee.isDisability());

        veteren = new JCheckBox("", employee.isVeteran());

        f.addInput(Elements.coloredLabel("Email", UIManager.getColor("Label.foreground")), emailField);
        f.addInput(Elements.coloredLabel("Phone", UIManager.getColor("Label.foreground")), phoneField);
        f.addInput(Elements.coloredLabel("Ethnicity", UIManager.getColor("Label.foreground")), ethnicities);
        f.addInput(Elements.coloredLabel("Gender", UIManager.getColor("Label.foreground")), genders);
        f.addInput(Elements.coloredLabel("Disabled?", UIManager.getColor("Label.foreground")), disabled);
        f.addInput(Elements.coloredLabel("Veteran?", UIManager.getColor("Label.foreground")), veteren);
        personalInfo.add(f);

        return personalInfo;
    }
}