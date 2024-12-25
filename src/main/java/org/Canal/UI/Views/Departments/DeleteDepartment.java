package org.Canal.UI.Views.Departments;

import org.Canal.Models.HumanResources.Department;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /DPTS/DEL
 */
public class DeleteDepartment extends LockeState {

    public DeleteDepartment() {

        super("Delete Department", "/DPTS/DEL", false, true, false, true);
        setFrameIcon(new ImageIcon(DeleteDepartment.class.getResource("/icons/delete.png")));
        setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        Form f = new Form();
        Selectable organizations = Selectables.organizations();
        JTextField departmentIdField = Elements.input(15);
        f.addInput(new Label("Organization", UIManager.getColor("Label.foreground")), organizations);
        f.addInput(new Label("Department ID", Constants.colors[10]), departmentIdField);
        setLayout(new BorderLayout());
        add(Elements.header("Delete Department", SwingConstants.LEFT), BorderLayout.NORTH);
        add(f, BorderLayout.CENTER);
        JButton confirmDeletion = Elements.button("Confirm");
        add(confirmDeletion, BorderLayout.SOUTH);
        confirmDeletion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Location org = Engine.getLocation(organizations.getSelectedValue(), "ORGS");
                if (org != null) {
                    boolean adjusted = false;

                    // Collect departments to remove in a separate list
                    ArrayList<Department> departmentsToRemove = new ArrayList<>();
                    for (Department d : org.getDepartments()) {
                        if (d.getId().equals(departmentIdField.getText())) {
                            departmentsToRemove.add(d);
                        }
                    }
                    // Remove departments after iteration
                    for (Department d : departmentsToRemove) {
                        org.removeDepartment(d);
                        adjusted = true;
                    }

                    if (adjusted) {
                        org.save();
                        JOptionPane.showMessageDialog(null, "Changes Saved");
                    } else {
                        JOptionPane.showMessageDialog(null, "No adjustment made.");
                    }
                    dispose();
                }
            }
        });
    }
}
