package org.Canal.UI.Views.New;

import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.Models.HumanResources.Department;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /DPTS/NEW
 */
public class CreateDepartment extends JInternalFrame {

    public CreateDepartment(DesktopState desktop){
        setTitle("Create Department");
        setFrameIcon(new ImageIcon(CreateDepartment.class.getResource("/icons/create.png")));
        Form f = new Form();

        String genId = "D0" + Engine.getEmployees().size() + 1;
        JTextField deptIdField = new JTextField(genId, 18);
        JTextField deptNameField = new JTextField(18);
        Selectable manager = Selectables.allEmployees();
        manager.editable();
        DatePicker openedDatePicker = new DatePicker();
        Selectable locations = Selectables.allLocations();
        Selectable orgs = Selectables.allOrgs();
        f.addInput(new Label("*New Department ID", UIManager.getColor("Label.foreground")), deptIdField);
        f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), orgs);
        f.addInput(new Label("Location (optional)", Constants.colors[9]), locations);
        f.addInput(new Label("Name", UIManager.getColor("Label.foreground")), deptNameField);
        f.addInput(new Label("Manager", UIManager.getColor("Label.foreground")), manager);
        f.addInput(new Label("Open Date", UIManager.getColor("Label.foreground")), openedDatePicker);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button cr = new Button("Process");
        add(cr, BorderLayout.SOUTH);
        setIconifiable(true);
        setClosable(true);
        cr.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                Department newDepartment = new Department();
                newDepartment.setId(deptIdField.getText());
                newDepartment.setOrg(orgs.getSelectedValue());
                newDepartment.setLocation(locations.getSelectedValue());
                //Parent Department
                newDepartment.setName(deptNameField.getText());
                Organization selectedOrg = Engine.getOrganization(orgs.getSelectedValue());
                selectedOrg.addDepartment(newDepartment);
                selectedOrg.save();
                dispose();
                JOptionPane.showMessageDialog(null, "Department Created in ORG " + orgs.getSelectedValue());
            }
        });
    }
}