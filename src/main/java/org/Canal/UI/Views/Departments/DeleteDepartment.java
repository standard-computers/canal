package org.Canal.UI.Views.Departments;

import org.Canal.Models.HumanResources.Department;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * /DPTS/DEL
 * This Locke specifically deletes Departments as they belong to Locations
 */
public class DeleteDepartment extends LockeState {

    public DeleteDepartment() {

        super("Delete Department", "/DPTS/DEL", false, true, false, true);
        setFrameIcon(new ImageIcon(DeleteDepartment.class.getResource("/icons/delete.png")));
        setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        Selectable locations = Selectables.allLocations();
        JTextField departmentId = Elements.input(15);

        Form form = new Form();
        form.addInput(Elements.inputLabel("Location"), locations);
        form.addInput(Elements.inputLabel("Department ID"), departmentId);

        setLayout(new BorderLayout());
        add(Elements.header("Delete Department", SwingConstants.LEFT), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        JButton confirmDeletion = Elements.button("Confirm");
        add(confirmDeletion, BorderLayout.SOUTH);
        confirmDeletion.addActionListener(_ -> {

            Location org = Engine.getLocation(locations.getSelectedValue(), "ORGS");
            if (org != null) {
                boolean adjusted = false;

                // Collect departments to remove in a separate list
                ArrayList<Department> departmentsToRemove = new ArrayList<>();
                for (Department d : org.getDepartments()) {
                    if (d.getId().equals(departmentId.getText())) {
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
        });
    }
}