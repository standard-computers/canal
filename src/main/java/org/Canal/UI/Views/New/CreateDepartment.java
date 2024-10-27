package org.Canal.UI.Views.New;

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
        JTextField empIdField = new JTextField(genId, 18);
        JTextField orgIdField = new JTextField(Engine.getOrganization().getId(), 18);
        JTextField empNameField = new JTextField(18);
        Selectable manager = Selectables.allEmployees();
        manager.editable();
        DatePicker startDatePicker = new DatePicker();
        Selectable locations = Selectables.allLocations();

        f.addInput(new Label("*New Department ID", UIManager.getColor("Label.foreground")), empIdField);
        f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), orgIdField);
        f.addInput(new Label("Location (optional)", Constants.colors[9]), locations);
        f.addInput(new Label("Name", UIManager.getColor("Label.foreground")), empNameField);
        f.addInput(new Label("Manager", UIManager.getColor("Label.foreground")), manager);
        f.addInput(new Label("Start Date", UIManager.getColor("Label.foreground")), startDatePicker);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button cr = new Button("Process");
        add(cr, BorderLayout.SOUTH);
        setIconifiable(true);
        setClosable(true);
        cr.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String orgId = orgIdField.getText().trim();
                dispose();
                JOptionPane.showMessageDialog(null, "Department Created in ORG " + orgId);
            }
        });
    }
}