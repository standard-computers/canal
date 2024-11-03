package org.Canal.UI.Views.Transportation.Carriers;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;

import javax.swing.*;
import java.awt.*;

/**
 * /TRANS/CRRS/NEW
 */
public class CreateCarrier extends JInternalFrame {

    public CreateCarrier(DesktopState desktop) {
        super("Create Carrier", true, true, true, true);
        Form f = new Form();

        String genId = "CAR" + 1;
        Copiable carrierId = new Copiable(genId);
        JTextField carrierName = new JTextField("FedEx", 10);
        JTextField street = new JTextField("", 10);
        JTextField city = new JTextField("", 10);
        JTextField state = new JTextField("", 10);
        JTextField postal = new JTextField("", 10);
        Selectable countries = Selectables.countries();

        f.addInput(new Label("*New Carrier ID", UIManager.getColor("Label.background")), carrierId);
        f.addInput(new Label("Carrier Name", Constants.colors[0]), carrierName);
        f.addInput(new Label("Street", Constants.colors[1]), street);
        f.addInput(new Label("City", Constants.colors[2]), city);
        f.addInput(new Label("State", Constants.colors[3]), state);
        f.addInput(new Label("Postal Code", Constants.colors[4]), postal);
        f.addInput(new Label("Country", Constants.colors[5]), countries);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        Button make = new Button("Make Carrier");
        make.color(Constants.colors[9]);
        add(make, BorderLayout.SOUTH);
    }
}