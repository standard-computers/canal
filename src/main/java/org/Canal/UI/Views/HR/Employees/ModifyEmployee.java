package org.Canal.UI.Views.HR.Employees;

import org.Canal.Models.HumanResources.Employee;
import javax.swing.*;

/**
 * /EMPS/MOD
 */
public class ModifyEmployee extends JInternalFrame {

    public ModifyEmployee(Employee employee) {
        setTitle(employee.getId() + " - " + employee.getName());

        setIconifiable(true);
        setClosable(true);
    }
}