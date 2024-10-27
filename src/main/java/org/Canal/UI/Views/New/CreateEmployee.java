package org.Canal.UI.Views.New;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Views.Singleton.EmployeeView;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class CreateEmployee extends JInternalFrame {

    public CreateEmployee(DesktopState desktop){
        setTitle("Create Employee");
        JPanel l = new JPanel(new BorderLayout());
        Form f = new Form();
        String puid = "E" + (10000 + (Engine.getEmployees().size() + 1));
        JTextField empIdField = new JTextField(puid, 18);
        JTextField orgIdField = new JTextField(Engine.getOrganization().getId(), 18);
        JTextField empNameField = new JTextField(18);
        f.addInput(new Label("New Employee ID", UIManager.getColor("Label.foreground")), empIdField);
        f.addInput(new Label("Organization", Constants.colors[10]), orgIdField);
        HashMap<String, String> accs = new HashMap<>();
        accs.put("", "");
        for(Location cs : Engine.getCostCenters()){
            accs.put(cs.getId() + " – " + cs.getName(), cs.getId());
        }
        Selectable ats = new Selectable(accs);
        ats.setSelectedValue("");
        f.addInput(new Label("Location (optional)", Constants.colors[9]), ats);
        f.addInput(new Label("Name", UIManager.getColor("Label.foreground")), empNameField);
        l.add(f, BorderLayout.CENTER);
        Button cr = new Button("Process");
        l.add(cr, BorderLayout.SOUTH);
        add(l);
        setResizable(false);
        cr.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String empId = empIdField.getText().trim();
                String orgId = orgIdField.getText().trim();
                String empName = empNameField.getText().trim();
                Employee newEmployee = new Employee(empId, orgId, ats.getSelectedValue(), empName);
                Pipe.save("/EMPS", newEmployee);
                dispose();
                JOptionPane.showMessageDialog(null, "Employee Created");
                desktop.put(new EmployeeView(newEmployee));
            }
        });
    }
}