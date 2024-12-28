package org.Canal.UI.Views.Controllers;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;

/**
 * /CNL/ME
 */
public class MyProfile extends LockeState {

    private Employee me;

    public MyProfile(DesktopState desktop) {

        super("My Profile", "/CNL/ME", true, true, true, true);
        //TODO Check that user is assigned
        me = Engine.getEmployee(Engine.getAssignedUser().getEmployee());

        setLayout(new BorderLayout());


    }

    private JPanel tools(){
        JPanel tools = new JPanel();

        return tools;
    }
}
