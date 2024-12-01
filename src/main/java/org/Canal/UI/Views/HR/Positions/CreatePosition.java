package org.Canal.UI.Views.HR.Positions;

import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /HR/POS/NEW
 */
public class CreatePosition extends JInternalFrame {

    public CreatePosition(DesktopState desktop) {
        super("Create Position", false, true, false, true);
        setFrameIcon(new ImageIcon(CreatePosition.class.getResource("/icons/create.png")));
        JTextField positionNameField = Elements.input();
        JTextField positionIdField = Elements.input();
        Selectable departments = Selectables.departments();
        JTextField stateField = Elements.input();
        JTextField compensationField = Elements.input();
        Selectable countries = Selectables.countries();
        JCheckBox autoPost = new JCheckBox("Post position as open?");
        Form f = new Form();
        f.addInput(new Label("Position Name", UIManager.getColor("Label.foreground")), positionNameField);
        f.addInput(new Label("*New Position ID", UIManager.getColor("Label.foreground")), positionIdField);
        f.addInput(new Label("Department", UIManager.getColor("Label.foreground")), departments);
        f.addInput(new Label("State", UIManager.getColor("Label.foreground")), stateField);
        f.addInput(new Label("Compensation", UIManager.getColor("Label.foreground")), compensationField);
        f.addInput(new Label("Comp. Class", UIManager.getColor("Label.foreground")), countries);
        f.addInput(new Label("Auto Post", UIManager.getColor("Label.foreground")), autoPost);
        JButton make = Elements.button("Create Position");
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
    }
}