package org.Canal.UI.Views.HR.Employees;

import org.Canal.Models.HumanResources.Employee;

import javax.swing.*;
import java.awt.*;

/**
 * /EMPS/MOD
 */
public class ModifyEmployee extends JInternalFrame {

    public ModifyEmployee(Employee employee) {
        super(employee.getId() + " - " + employee.getName(), false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
        setFrameIcon(new ImageIcon(ModifyEmployee.class.getResource("/icons/modify.png")));
    }
}