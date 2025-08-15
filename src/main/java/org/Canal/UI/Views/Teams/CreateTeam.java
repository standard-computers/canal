package org.Canal.UI.Views.Teams;

import org.Canal.Models.HumanResources.Team;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /TMS/NEW
 */
public class CreateTeam extends LockeState {

    public CreateTeam() {

        super("Create Team", "/TMS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateTeam.class.getResource("/icons/create.png")));

        JTextField teamIdField = Elements.input(10);
        JTextField teamNameField = Elements.input();
        Selectable departments = Selectables.departments();
        Selectable leaders = Selectables.employees();

        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New Team ID", UIManager.getColor("Label.foreground")), teamIdField);
        f.addInput(Elements.coloredLabel("Team Name", Constants.colors[10]), teamNameField);
        f.addInput(Elements.coloredLabel("Department", Constants.colors[9]), departments);
        f.addInput(Elements.coloredLabel("Leader", Constants.colors[8]), leaders);

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton make = Elements.button("Make Team");
        add(make, BorderLayout.SOUTH);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Team team = new Team();
                team.setName(teamNameField.getText());

            }
        });
    }
}