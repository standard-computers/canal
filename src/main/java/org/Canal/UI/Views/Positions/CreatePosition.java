package org.Canal.UI.Views.Positions;

import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /HR/POS/NEW
 *  Create a new Position in an Organization.
 * Positions MUST be attached to a Department <em>/DPTS</em>
 */
public class CreatePosition extends LockeState {

    public CreatePosition(DesktopState desktop) {

        super("Create Position", "/HR/POS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreatePosition.class.getResource("/icons/create.png")));

        JTextField positionIdField = Elements.input("R-DEPT-" + (Engine.getPositions().size() + 1));
        Selectable organizations = Selectables.organizations();
        JTextField positionNameField = Elements.input();
        JTextField descriptionField = Elements.input();
        Selectable departments = Selectables.departments();
        JTextField compensationField = Elements.input();
        Selectable countries = Selectables.countries();
        JCheckBox isHourly = new JCheckBox("Employee must clock in and out");
        JCheckBox isBonusable = new JCheckBox("Employee can earn bonuses");
        JCheckBox isCommissionable = new JCheckBox("Position is or can earn commission");
        JCheckBox autoPost = new JCheckBox("Post position as open?");

        Form f = new Form();
        f.addInput(new Label("*New Position ID", UIManager.getColor("Label.foreground")), positionIdField);
        f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), organizations);
        f.addInput(new Label("Position Name", Constants.colors[0]), positionNameField);
        f.addInput(new Label("Short Description", Constants.colors[1]), descriptionField);
        f.addInput(new Label("Department", Constants.colors[2]), departments);
        f.addInput(new Label("Compensation", Constants.colors[3]), compensationField);
        f.addInput(new Label("Comp. Class", Constants.colors[4]), countries);
        f.addInput(new Label("Hourly?", Constants.colors[5]), isHourly);
        f.addInput(new Label("Earns Bonuses", Constants.colors[6]), isBonusable);
        f.addInput(new Label("Earns Commission", Constants.colors[7]), isCommissionable);
        f.addInput(new Label("Auto Post", UIManager.getColor("Label.foreground")), autoPost);

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton make = Elements.button("Create Position");
        add(make, BorderLayout.SOUTH);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Position position = new Position();
                position.setId(positionIdField.getText());
                position.setOrganization(organizations.getSelectedValue());
                position.setDepartment(departments.getSelectedValue());
                position.setName(positionNameField.getText());
                position.setDescription(descriptionField.getText());
                position.setCompensation(Double.parseDouble(compensationField.getText()));
                position.setHourly(isHourly.isSelected());
                position.setBonus(isBonusable.isSelected());
                position.setCommission(isCommissionable.isSelected());
                position.setStatus(LockeStatus.NEW);
                Pipe.save("/HR/POS", position);
                dispose();
                JOptionPane.showMessageDialog(null, "Position succesfully created!");
                desktop.put(new PositionView(position));
            }
        });
    }
}