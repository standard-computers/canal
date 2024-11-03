package org.Canal.UI.Views.HR;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /HR/POS/NEW
 */
public class CreatePosition extends JInternalFrame {

    public CreatePosition(DesktopState desktop) {
        setTitle("Create Position");
        setFrameIcon(new ImageIcon(CreatePosition.class.getResource("/icons/create.png")));
        JTextField orgNameField = new JTextField();
        JTextField orgIdField = new JTextField();
        orgIdField.setText(String.valueOf(1000 + (Engine.getOrganizations().size() + 1)));
        JTextField line1Field = new JTextField();
        JTextField stateField = new JTextField();
        JTextField postalCodeField = new JTextField();
        JTextField taxId = new JTextField();
        JCheckBox isTaxExempt = new JCheckBox();
        Selectable countries = Selectables.countries();
        Form f = new Form();
        f.addInput(new Label("Position Name", UIManager.getColor("Label.foreground")), orgNameField);
        f.addInput(new Label("*New Position ID", UIManager.getColor("Label.foreground")), orgIdField);
        f.addInput(new Label("Address Line 1", UIManager.getColor("Label.foreground")), line1Field);
        f.addInput(new Label("State", UIManager.getColor("Label.foreground")), stateField);
        f.addInput(new Label("Postal Code", UIManager.getColor("Label.foreground")), postalCodeField);
        f.addInput(new Label("Country", UIManager.getColor("Label.foreground")), countries);
        f.addInput(new Label("Tax ID", UIManager.getColor("Label.foreground")), taxId);
        f.addInput(new Label("Tax Exempt?", UIManager.getColor("Label.foreground")), isTaxExempt);
        Button make = new Button("Make Organization");
        getRootPane().setDefaultButton(make);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
        setClosable(true);
        setIconifiable(true);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
    }
}