package org.Canal.UI.Views.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * /EMPS/MOD/$
 */
public class ModifyEmployee extends LockeState {

    private Employee employee;
    private DesktopState desktop;
    private RefreshListener refreshListener;

    //General Info Tab
    private Selectable orgIdField;
    private JTextField locationField;
    private JTextField nameField;
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField position;
    private JTextField supervisor;
    private DatePicker startDatePicker = new DatePicker();

    //Address Tab
    private JTextField addressL1Field;
    private JTextField addressL2Field;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField postalField;
    private Selectable countryField;

    //Personal Tab
    private JTextField phoneField;
    private JTextField emailField;
    private Selectable ethnicities;
    private Selectable genders;
    private JCheckBox disabled;
    private JCheckBox veteren;

    public ModifyEmployee(Employee employee, DesktopState desktop, RefreshListener refreshListener) {

        super("Modify " + employee.getName(), "/EMPS/MOD/" + employee.getId(), false, true, false, true);
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

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));

        IconButton save = new IconButton("Save", "save", "Save changes");
        save.addActionListener(_ -> {

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
            employee.setPosition(position.getText());
            employee.setSupervisor(supervisor.getText());
            employee.setEmail(emailField.getText());
            employee.setPhone(phoneField.getText());
            employee.setEthnicity(ethnicities.getSelectedValue());
            employee.setGender(genders.getSelectedValue());
            employee.setDisability(disabled.isSelected());
            employee.setVeteran(veteren.isSelected());

            employee.save();

            if (refreshListener != null) refreshListener.refresh();

            if ((boolean) Engine.codex.getValue("EMPS", "dispose_on_save")) {
                dispose();
            }
        });
        toolbar.add(save);
        toolbar.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-save");
        rp.getActionMap().put("do-save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save.doClick();
            }
        });

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        review.addActionListener(_ -> performReview());
        toolbar.add(review);
        toolbar.add(Box.createHorizontalStrut(5));

        if ((boolean) Engine.codex.getValue("EMPS", "allow_archival")) {
            IconButton archive = new IconButton("Archive", "archive", "Archive item");
            archive.addActionListener(_ -> {
                employee.setStatus(LockeStatus.ARCHIVED);
                employee.save();
                dispose();
            });
            toolbar.add(archive);
            toolbar.add(Box.createHorizontalStrut(5));
        }

        if ((boolean) Engine.codex.getValue("EMPS", "allow_deletion")) {
            IconButton delete = new IconButton("Delete", "delete", "Delete item");
            delete.addActionListener(_ -> {

            });
            toolbar.add(delete);
            toolbar.add(Box.createHorizontalStrut(5));
        }

        return toolbar;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        orgIdField = Selectables.organizations();
        orgIdField.setSelectedValue(employee.getOrg());
        nameField = Elements.input(employee.getName());
        firstNameField = Elements.input(employee.getFirstName());
        middleNameField = Elements.input(employee.getMiddleName());
        lastNameField = Elements.input(employee.getLastName());
        position = Elements.input(employee.getPosition());
        supervisor = Elements.input(employee.getSupervisor());

        startDatePicker = new DatePicker();
//     ToDO   startDatePicker.setSelectedDate(employee.getStartDate());

        Form form = new Form();
        form.addInput(Elements.inputLabel("Organization"), orgIdField);
        locationField = Elements.input(employee.getLocation(), 15);

        form.addInput(Elements.inputLabel("Location"), locationField);
        form.addInput(Elements.inputLabel("Name (Nickname)"), nameField);
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

        addressL1Field = Elements.input(employee.getLine1());
        addressL2Field = Elements.input(employee.getLine2());
        cityField = Elements.input(employee.getCity());
        stateField = Elements.input(employee.getState());
        postalField = Elements.input(employee.getPostal());
        countryField = Selectables.countries();
        countryField.setSelectedValue(employee.getCountry());

        Form form = new Form();
        form.addInput(Elements.inputLabel("Street Line 1"), addressL1Field);
        form.addInput(Elements.inputLabel("Line 2"), addressL2Field);
        form.addInput(Elements.inputLabel("City"), cityField);
        form.addInput(Elements.inputLabel("State"), stateField);
        form.addInput(Elements.inputLabel("Postal"), postalField);
        form.addInput(Elements.inputLabel("Country"), countryField);
        locationInfo.add(form);

        return locationInfo;
    }

    private JPanel personalInfo() {

        JPanel personalInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        emailField = Elements.input(employee.getEmail(), 15);
        phoneField = Elements.input(employee.getPhone());
        ethnicities = Selectables.ethnicities();
        ethnicities.setSelectedValue(employee.getEthnicity());
        genders = Selectables.genders();
        genders.setSelectedValue(employee.getGender());
        disabled = new JCheckBox("", employee.isDisability());
        veteren = new JCheckBox("", employee.isVeteran());

        Form form = new Form();
        form.addInput(Elements.inputLabel("Email"), emailField);
        form.addInput(Elements.inputLabel("Phone"), phoneField);
        form.addInput(Elements.inputLabel("Ethnicity"), ethnicities);
        form.addInput(Elements.inputLabel("Gender"), genders);
        form.addInput(Elements.inputLabel("Disabled?"), disabled);
        form.addInput(Elements.inputLabel("Veteran?"), veteren);
        personalInfo.add(form);

        return personalInfo;
    }

    private void performReview() {

    }
}