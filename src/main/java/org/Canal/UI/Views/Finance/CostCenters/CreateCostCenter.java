package org.Canal.UI.Views.Finance.CostCenters;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /CCS/NEW
 */
public class CreateCostCenter extends JInternalFrame {

    public CreateCostCenter(){
        super();
        setTitle("Create Cost Center");
        setFrameIcon(new ImageIcon(CreateCostCenter.class.getResource("/icons/cost_centers.png")));
        String genId = "CC" + 1000 + (Engine.getCostCenters().size() + 1);
        JTextField ccIdField = Elements.input(genId);
        Selectable organizations = Selectables.organizations();
        JTextField ccNameField = Elements.input(20);
        JTextField streetNameField = Elements.input(20);
        JTextField ccCityField = Elements.input(20);
        JTextField ccStateField = Elements.input(20);
        JTextField ccPostalField = Elements.input(20);
        Selectable countries = Selectables.countries();
        Form f = new Form();
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), ccIdField);
        f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), organizations);
        f.addInput(new Label("Name", Constants.colors[1]), ccNameField);
        f.addInput(new Label("Street", Constants.colors[2]), streetNameField);
        f.addInput(new Label("City", Constants.colors[3]), ccCityField);
        f.addInput(new Label("State", Constants.colors[4]), ccStateField);
        f.addInput(new Label("Postal Code", Constants.colors[5]), ccPostalField);
        f.addInput(new Label("Country", Constants.colors[6]), countries);
        JCheckBox taxExempt = new JCheckBox();
        f.addInput(new Label("Tax Exempt?", Constants.colors[7]), taxExempt);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button make = new Button("Make");
        add(make, BorderLayout.SOUTH);
        setIconifiable(true);
        setClosable(true);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String costCenterId = ccIdField.getText().trim();
                String orgTieId = organizations.getSelectedValue();
                String name = ccNameField.getText().trim();
                String line1 = streetNameField.getText().trim();
                String city = ccCityField.getText().trim();
                String state = ccStateField.getText().trim();
                String postal = ccPostalField.getText().trim();
                String country = countries.getSelectedValue();
                Location location = new Location(costCenterId, orgTieId, name, line1, city, state, postal, country, taxExempt.isSelected());
                Pipe.save("/CCS", location);
                dispose();
            }
        });
    }
}