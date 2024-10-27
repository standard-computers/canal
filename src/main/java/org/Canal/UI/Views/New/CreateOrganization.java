package org.Canal.UI.Views.New;

import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Views.Singleton.OrgView;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateOrganization extends JInternalFrame {

    public CreateOrganization(DesktopState desktop) {
        setTitle("Create Organization");
        setFrameIcon(new ImageIcon(CreateOrganization.class.getResource("/icons/create.png")));
        JTextField orgNameField = new JTextField();
        JTextField orgIdField = new JTextField();
        orgIdField.setText(String.valueOf(1000 + (Engine.getOrganizations().size() + 1)));
        JTextField line1Field = new JTextField();
        JTextField line2Field = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField postalCodeField = new JTextField();
        JTextField taxId = new JTextField();
        JCheckBox isTaxExempt = new JCheckBox();
        Selectable countries = Selectables.countries();
        Form f = new Form();
        f.addInput(new Label("Company Name", UIManager.getColor("Label.foreground")), orgNameField);
        f.addInput(new Label("*New Org ID", UIManager.getColor("Label.foreground")), orgIdField);
        f.addInput(new Label("Address Line 1", UIManager.getColor("Label.foreground")), line1Field);
        f.addInput(new Label("Line 2", UIManager.getColor("Label.foreground")), line2Field);
        f.addInput(new Label("City", UIManager.getColor("Label.foreground")), cityField);
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
                String name = orgNameField.getText().trim();
                String line1 = line1Field.getText().trim();
                String line2 = line2Field.getText().trim();
                String city = cityField.getText().trim();
                String state = stateField.getText().trim();
                String postal = postalCodeField.getText().trim();
                String country = countries.getSelectedValue();
                String taxIdTxt = taxId.getText().trim();
                Organization newOrganization = new Organization();
                newOrganization.setId(orgIdField.getText().trim());
                newOrganization.setName(name);
                newOrganization.setLine1(line1);
                newOrganization.setLine2(line2);
                newOrganization.setCity(city);
                newOrganization.setState(state);
                newOrganization.setPostal(postal);
                newOrganization.setCountry(country);
                newOrganization.setEin(taxIdTxt);
                newOrganization.setTaxExempt(isTaxExempt.isSelected());
                Pipe.save("/ORGS", newOrganization);
                dispose();
                desktop.put(new OrgView(newOrganization, desktop));
            }
        });
    }
}