package org.Canal.UI.Views.Controllers;

import org.Canal.Models.HumanResources.Employee;
import org.Canal.UI.Elements.CustomTabbedPane;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.LockeState;
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

        super("My Profile", "/ME");
        //TODO Check that user is assigned
        me = Engine.getEmployee(Engine.getAssignedUser().getEmployee());

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("Summary", summary());
        tabs.addTab("Pay", pay());
        tabs.addTab("Time", time());
        tabs.addTab("Information", information());
        tabs.addTab("Positions", positions());
        tabs.addTab("Learning", learning());
        setLayout(new BorderLayout());
        add(Elements.header("My Profile", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabs,BorderLayout.CENTER);
        setMaximized(true);
    }

    private JPanel summary(){

        JPanel summary = new JPanel();

        return summary;
    }

    private JPanel pay(){

        JPanel pay = new JPanel();

        return pay;
    }

    private JPanel time(){

        JPanel time = new JPanel();

        return time;
    }

    private JPanel information(){

        JPanel information = new JPanel();

        return information;
    }

    private JPanel positions(){

        JPanel positions = new JPanel();

        return positions;
    }

    private JPanel learning(){

        JPanel learning = new JPanel();

        return learning;
    }
}