package org.Canal.UI.Views.Positions;

import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Pipe;

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
        JTextField positionNameField = Elements.input();
        JTextField positionIdField = Elements.input();
        Selectable departments = Selectables.departments();
        JTextField compensationField = Elements.input();
        Selectable countries = Selectables.countries();
        JCheckBox isHourly = new JCheckBox();
        JCheckBox autoPost = new JCheckBox("Post position as open?");
        Form f = new Form();
        f.addInput(new Label("*New Position ID", UIManager.getColor("Label.foreground")), positionIdField);
        f.addInput(new Label("Position Name", Constants.colors[0]), positionNameField);
        f.addInput(new Label("Department", Constants.colors[1]), departments);
        f.addInput(new Label("Compensation", Constants.colors[2]), compensationField);
        f.addInput(new Label("Comp. Class", Constants.colors[3]), countries);
        f.addInput(new Label("Hourly?", Constants.colors[4]), isHourly);
        f.addInput(new Label("Auto Post", Constants.colors[5]), autoPost);
        JButton make = Elements.button("Create Position");
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Position position = new Position();
                position.setId(positionIdField.getText());
                position.setName(positionNameField.getText());
                position.setDepartment(departments.getSelectedValue());
                position.setCompensation(Double.parseDouble(compensationField.getText()));
                position.setHourly(isHourly.isSelected());
                Pipe.save("/HR/POS", position);
                dispose();
            }
        });
    }
}