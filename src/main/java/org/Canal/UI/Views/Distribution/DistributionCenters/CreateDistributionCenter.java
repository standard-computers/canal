package org.Canal.UI.Views.Distribution.DistributionCenters;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.Label;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /DCSS/NEW
 */
public class CreateDistributionCenter extends JInternalFrame {

    public CreateDistributionCenter(DesktopState desktop) {
        super("New Distribution Center", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateDistributionCenter.class.getResource("/icons/create.png")));
        ArrayList<Location> ls = Engine.getDistributionCenters();
        String generatedId = "DC" + (100000 + (ls.size() + 1));
        JTextField dcIdField = new JTextField(generatedId);
        JTextField orgIdField = new JTextField(Engine.getOrganization().getId());
        JTextField cn = new JTextField(20);
        JTextField l1 = new JTextField(20);
        JTextField ct = new JTextField(20);
        JTextField st = new JTextField(20);
        JTextField ps = new JTextField(20);
        JTextField cy = new JTextField(20);
        cy.setText("US");
        Button make = new Button("Make");
        Form f = new Form();
        f.addInput(new Label("*New ID", UIManager.getColor("Label.foreground")), dcIdField);
        f.addInput(new Label("*Organization", UIManager.getColor("Label.foreground")), orgIdField);
        f.addInput(new Label("Name", Constants.colors[0]), cn);
        f.addInput(new Label("Street", Constants.colors[1]), l1);
        f.addInput(new Label("City", Constants.colors[2]), ct);
        f.addInput(new Label("State", Constants.colors[3]), st);
        f.addInput(new Label("Postal", Constants.colors[4]), ps);
        f.addInput(new Label("Country", Constants.colors[5]), cy);
        add(f, BorderLayout.CENTER);
        add(make, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(make);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String dcId = dcIdField.getText().trim();
                String orgTieId = orgIdField.getText().trim();
                String name = cn.getText().trim();
                String line1 = l1.getText().trim();
                String city = ct.getText().trim();
                String state = st.getText().trim();
                String postal = ps.getText().trim();
                String country = cy.getText();
                Location location = new Location(dcId, orgTieId, name, line1, city, state, postal, country, false);
                Pipe.save("/DCSS", location);
                dispose();
                desktop.put(new DCView(location, desktop));
            }
        });
    }
}