package org.Canal.UI.Views.HR.Employees;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.LockeState;

import javax.swing.*;
import java.awt.*;

/**
 * /EMPS/MOD
 */
public class ModifyEmployee extends LockeState {

    public ModifyEmployee(Employee employee) {
        super(employee.getId() + " - " + employee.getName(), "/EMPS/MOD", false, true, false, true);
        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.YELLOW));
        setFrameIcon(new ImageIcon(ModifyEmployee.class.getResource("/icons/modify.png")));
    }
}