package org.Canal.UI.Views.ValueAddedServices;

import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;

import javax.swing.*;
import java.awt.*;

public class ViewVAS extends LockeState {

    public ViewVAS() {

        super("View Value Added Service", "/VAS/$", false, true, false, true);
//        setFrameIcon(new ImageIcon(Controller.class.getResource("/icons/create.png")));

        JTextField vasIdField = Elements.input();
        Selectable organizations = Selectables.organizations();
        JTextField vasNameField = Elements.input(15);
        JTextField vasDescriptionField = Elements.input(15);
        JCheckBox isRecurring = new JCheckBox();
        JCheckBox isTaxable = new JCheckBox();
        JTextField vasAmount = Elements.input("10.00");

        Form f = new Form();
        f.addInput(Elements.coloredLabel("*New VAS ID", UIManager.getColor("Label.foreground")), vasIdField);
        f.addInput(Elements.coloredLabel("Organization", UIManager.getColor("Label.foreground")), organizations);
        f.addInput(Elements.coloredLabel("Name", Constants.colors[10]), vasNameField);
        f.addInput(Elements.coloredLabel("Description", Constants.colors[9]), vasDescriptionField);
        f.addInput(Elements.coloredLabel("Amount (Price)", Constants.colors[9]), vasAmount);
        f.addInput(Elements.coloredLabel("Recurring", Constants.colors[8]), isRecurring);
        f.addInput(Elements.coloredLabel("Taxable", Constants.colors[7]), isTaxable);

        setLayout(new BorderLayout());
        add(f, BorderLayout.CENTER);
        JButton save = Elements.button("Create VAS");
        add(save, BorderLayout.SOUTH);
    }
}
