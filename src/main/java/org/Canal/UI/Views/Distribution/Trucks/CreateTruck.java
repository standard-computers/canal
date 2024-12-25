package org.Canal.UI.Views.Distribution.Trucks;

import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
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
 * /TRANS/TRCKS/NEW
 */
public class CreateTruck extends LockeState {

    private DesktopState desktop;

    public CreateTruck(DesktopState desktop) {

        super("Create a Truck", "/TRANS/TRCKS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateTruck.class.getResource("/icons/trucks.png")));
        this.desktop = desktop;

        Form f = new Form();
        JTextField truckIdField = new JTextField((String) Engine.codex("TRANS/TRCKS", "prefix") + 1000 + (Engine.getTrucks().size() + 1));
        JTextField truckNumberField = Elements.input(15);
        Selectable carriers = Selectables.carriers();
        JTextField driverNameField = Elements.input(15);
        JTextField yearNameField = Elements.input(15);
        JTextField makeNameField = Elements.input(15);
        JTextField modelNameField = Elements.input(15);
        f.addInput(new Label("*New Truck ID", UIManager.getColor("Label.foreground")), truckIdField);
        f.addInput(new Label("Truck Number", Constants.colors[0]), truckNumberField);
        f.addInput(new Label("Carrier", Constants.colors[1]), carriers);
        f.addInput(new Label("Driver (Name)", Constants.colors[2]), driverNameField);
        f.addInput(new Label("Vehicle Year", Constants.colors[3]), yearNameField);
        f.addInput(new Label("Vehicle Make", Constants.colors[4]), makeNameField);
        f.addInput(new Label("Vehicle Model", Constants.colors[5]), modelNameField);

        setLayout(new BorderLayout());
        add(Elements.header("Create a Truck", SwingConstants.LEFT), BorderLayout.NORTH);
        add(f, BorderLayout.CENTER);
        JButton create = Elements.button("Create Truck");
        add(create, BorderLayout.SOUTH);
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Truck truck = new Truck();
                truck.setId(truckIdField.getText());
                truck.setNumber(truckNumberField.getText());
                truck.setCarrier(carriers.getSelectedValue());
                truck.setDriver(driverNameField.getText());
                truck.setYear(yearNameField.getText());
                truck.setMake(makeNameField.getText());
                truck.setModel(modelNameField.getText());
                Pipe.save("/TRANS/TRCKS/NEW", truck);
                dispose();
                JOptionPane.showMessageDialog(null, "Truck created");
                Engine.router("/TRANS/TRCKS/" + truckIdField.getText(), desktop);
            }
        });
    }
}
