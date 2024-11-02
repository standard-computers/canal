package org.Canal.UI.Views.HR.Organizations;

import org.Canal.Models.BusinessUnits.Organization;
import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /ORGS/NEW
 */
public class CreateOrganization extends JInternalFrame {

    public CreateOrganization(DesktopState desktop) {
        super("Create Organization", false, true, false, true);
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
        f.addInput(new Label("*New Organization ID", UIManager.getColor("Label.foreground")), orgIdField);
        f.addInput(new Label("Company Name", Constants.colors[0]), orgNameField);
        f.addInput(new Label("Address Line 1", Constants.colors[1]), line1Field);
        f.addInput(new Label("Line 2", Constants.colors[2]), line2Field);
        f.addInput(new Label("City", Constants.colors[3]), cityField);
        f.addInput(new Label("State", Constants.colors[4]), stateField);
        f.addInput(new Label("Postal Code", Constants.colors[5]), postalCodeField);
        f.addInput(new Label("Country", Constants.colors[6]), countries);
        f.addInput(new Label("Tax ID", Constants.colors[7]), taxId);
        f.addInput(new Label("Tax Exempt?", Constants.colors[8]), isTaxExempt);
        Button make = new Button("Make Organization");
        setLayout(new BorderLayout());
        add(Panels.header("New Organization"), BorderLayout.NORTH);
        add(f, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
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