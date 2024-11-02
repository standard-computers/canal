package org.Canal.UI.Views.Finance.CostCenters;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateCostCenter extends JInternalFrame {

    public CreateCostCenter(){
        super();
        setTitle("Create Cost Center");
        setFrameIcon(new ImageIcon(CreateCostCenter.class.getResource("/icons/create.png")));
        Constants.checkLocke(this, true, true);
        String genId = "CC" + 100000 + (Engine.getItems().size() + 1);
        JTextField ccIdField = new JTextField(genId);
        JTextField orgIdField = new JTextField(Engine.getOrganization().getId());
        JTextField ccNameField = new JTextField(20);
        JTextField streetNameField = new JTextField(20);
        JTextField ccCityField = new JTextField(20);
        JTextField ccStateField = new JTextField(20);
        JTextField ccPostalField = new JTextField(20);
        JTextField ccCountryField = new JTextField(20);
        Form f = new Form();
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), ccIdField);
        f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), orgIdField);
        f.addInput(new Label("Name", Constants.colors[1]), ccNameField);
        f.addInput(new Label("Street", Constants.colors[2]), streetNameField);
        f.addInput(new Label("City", Constants.colors[3]), ccCityField);
        f.addInput(new Label("State", Constants.colors[4]), ccStateField);
        f.addInput(new Label("Postal Code", Constants.colors[5]), ccPostalField);
        f.addInput(new Label("Country", Constants.colors[6]), ccCountryField);
        JCheckBox taxExempt = new JCheckBox();
        f.addInput(new Label("Tax Exempt?", Constants.colors[7]), taxExempt);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button make = new Button("Make");
        getRootPane().setDefaultButton(make);
        add(make, BorderLayout.SOUTH);
        setIconifiable(true);
        setClosable(true);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String costCenterId = ccIdField.getText().trim();
                String orgTieId = orgIdField.getText().trim();
                String name = ccNameField.getText().trim();
                String line1 = streetNameField.getText().trim();
                String city = ccCityField.getText().trim();
                String state = ccStateField.getText().trim();
                String postal = ccPostalField.getText().trim();
                String country = ccCountryField.getText();
                Location location = new Location(costCenterId, orgTieId, name, line1, city, state, postal, country, taxExempt.isSelected());
                Pipe.save("/CCS", location);
                dispose();
            }
        });
    }
}