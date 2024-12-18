package org.Canal.UI.Views.ValueAddedServices;

import org.Canal.UI.Elements.*;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.UI.Views.Controllers.Controller;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * /VAS/NEW
 */
public class CreateVAS extends LockeState {

    public CreateVAS() {
        super("Create VAS", "/VAS/NEW", false, true, false, true);
        Constants.checkLocke(this, true, true);
        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));

        JTextField vasIdField = Elements.input();
        Selectable organizations = Selectables.organizations();
        JTextField vasNameField = Elements.input(15);
        JCheckBox isRecurring = new JCheckBox();
        JCheckBox isTaxable = new JCheckBox();
        JTextField vasAmount = Elements.input("10.00");
        Form f = new Form();
        f.addInput(new Label("*New VAS ID", UIManager.getColor("Label.foreground")), vasIdField);
        f.addInput(new Label("Organization", UIManager.getColor("Label.foreground")), organizations);
        f.addInput(new Label("Name", Constants.colors[10]), vasNameField);
        f.addInput(new Label("Amount", Constants.colors[9]), vasAmount);
        f.addInput(new Label("Recurring", Constants.colors[8]), isRecurring);
        f.addInput(new Label("Taxable", Constants.colors[7]), isTaxable);
        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton save = Elements.button("Create VAS");
        add(save, BorderLayout.SOUTH);
    }
}