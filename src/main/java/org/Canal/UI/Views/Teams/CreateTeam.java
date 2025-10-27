package org.Canal.UI.Views.Teams;

import org.Canal.Models.HumanResources.Team;
import org.Canal.UI.Elements.*;

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

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Team ID"), teamIdField);
        form.addInput(Elements.inputLabel("Team Name"), teamNameField);
        form.addInput(Elements.inputLabel("Department"), departments);
        form.addInput(Elements.inputLabel("Leader"), leaders);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
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