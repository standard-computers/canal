package org.Canal.UI.Views.Controllers;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.ViewLocation;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import java.awt.*;

/**
 * /ME
 */
public class MyProfile extends LockeState {

    private Employee me;

    public MyProfile(DesktopState desktop) {

        super("My Profile", "/ME", true, true, true, true);
        //TODO Check that user is assigned
        me = Engine.getEmployee(Engine.getAssignedUser().getEmployee());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Summary", summary());
        tabs.addTab("Pay", new ImageIcon(ViewLocation.class.getResource("/icons/payment.png")), pay());
        tabs.addTab("Time", new ImageIcon(ViewLocation.class.getResource("/icons/timeclock.png")), time());
        tabs.addTab("Information", information());
        tabs.addTab("Positions", new ImageIcon(ViewLocation.class.getResource("/icons/positions.png")), positions());
        tabs.addTab("Learning", learning());
        setLayout(new BorderLayout());
        add(tabs,BorderLayout.NORTH);
    }

    private JPanel summary(){
        JPanel p = new JPanel();

        return p;
    }

    private JPanel pay(){
        JPanel p = new JPanel();

        return p;
    }

    private JPanel time(){
        JPanel p = new JPanel();

        return p;
    }

    private JPanel information(){
        JPanel p = new JPanel();

        return p;
    }

    private JPanel positions(){
        JPanel p = new JPanel();

        return p;
    }

    private JPanel learning(){
        JPanel p = new JPanel();

        return p;
    }
}