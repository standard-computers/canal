package org.Canal.UI.Views.Tasks;

import org.Canal.Models.SupplyChainUnits.Task;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Includer;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * /
 */
public class CreateStep extends LockeState {

    private JTextField stepDescriptionField;
    private JTextField locationField;
    private JTextField areaField;
    private JTextField binField;
    private JTextField lockeField;
    private JTextField itemField;
    private JTextField quantityField;
    private JTextField employeesField;

    //Controls Tab
    private JCheckBox goodsIssue;
    private JCheckBox goodsReceipt;

    //Notes Tab
    private RTextScrollPane notes;

    public CreateStep(Includer includer) {

        super("Create Step", "/MVMT/TSKS/NEW", false, true, false, true);

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Controls", controls());
        tabs.addTab("Notes", notes());

        setLayout(new BorderLayout());
        add(Elements.header("Create a Step", SwingConstants.LEFT), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        JButton create = Elements.button("Create Step");
        add(create, BorderLayout.SOUTH);
        create.addActionListener(_ -> {

            Task task = new Task();
            task.setDescription(stepDescriptionField.getText());
            task.setLocation(locationField.getText());
            task.setSourceArea(areaField.getText());
            task.setSourceBin(binField.getText());
            task.setLocke(lockeField.getText());
            task.setItem(itemField.getText());
            task.setQuantity(Double.parseDouble(quantityField.getText()));
            task.setEmployees(Integer.parseInt(employeesField.getText()));
            task.setNotes(notes.getTextArea().getText());

            includer.commitStep(task);

            dispose();
        });
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        stepDescriptionField = Elements.input(15);
        locationField = Elements.input();
        areaField = Elements.input();
        binField = Elements.input();
        lockeField = Elements.input();
        itemField = Elements.input();
        quantityField = Elements.input();
        employeesField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Description"), stepDescriptionField);
        form.addInput(Elements.inputLabel("Location (ID)"), locationField);
        form.addInput(Elements.inputLabel("Area (ID)"), areaField);
        form.addInput(Elements.inputLabel("Bin (ID)"), binField);
        form.addInput(Elements.inputLabel("Locke Code"), lockeField);
        form.addInput(Elements.inputLabel("Item (ID)"), itemField);
        form.addInput(Elements.inputLabel("Quantity"), quantityField);
        form.addInput(Elements.inputLabel("Employees (Qty)"), employeesField);
        general.add(form);

        return general;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        goodsIssue = new JCheckBox("Creates Goods Issue & Removes Inventory");
        goodsReceipt = new JCheckBox("Creates Goods Receipt & Adds Inventory");

        Form form = new Form();
        form.addInput(Elements.inputLabel("Goods Issue"), goodsIssue);
        form.addInput(Elements.inputLabel("Goods Receipt"), goodsReceipt);
        controls.add(form);

        return controls;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }
}