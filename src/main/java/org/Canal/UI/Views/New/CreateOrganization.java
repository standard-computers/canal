package org.Canal.UI.Views.New;

import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Input;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateOrganization extends JInternalFrame {

    public CreateOrganization() {
        setTitle("Create Organization");
        setFrameIcon(new ImageIcon(CreateOrganization.class.getResource("/icons/create.png")));
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBorder(new EmptyBorder(6, 6, 3, 6));
        JPanel h = new JPanel(new GridLayout(4, 2));
        h.setBorder(new EmptyBorder(4, 4, 4, 4));
        Input cn = new Input("Name");
        Input id = new Input("Organization ID");
        id.setValue(String.valueOf(1000 + (Engine.getOrganizations().size() + 1)));
        Input l1 = new Input("Street");
        Input ct = new Input("City");
        Input st = new Input("State");
        Input ps = new Input("Postal");
        Input cy = new Input("Country");
        JCheckBox ex = new JCheckBox("Tax Exempt?");
        ex.setBorder(new EmptyBorder(20, 20, 0, 20));
        Button make = new Button("Create");
        h.add(cn);
        h.add(id);
        h.add(l1);
        h.add(ct);
        h.add(st);
        h.add(ps);
        h.add(cy);
        h.add(ex);
        pp.add(h, BorderLayout.CENTER);
        pp.add(make, BorderLayout.SOUTH);
        add(pp);
        pack();
        getRootPane().setDefaultButton(make);
        setResizable(false);
        make.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = cn.value().trim();
                String line1 = l1.value().trim();
                String city = ct.value().trim();
                String state = st.value().trim();
                String postal = ps.value().trim();
                String country = cy.value().trim();
                Location norg = new Location(id.value(), "", name, line1, city, state, postal, country, ex.isSelected());
                Pipe.save("/ORGS", norg);
                dispose();
            }
        });
        setClosable(true);
        setIconifiable(true);
    }
}