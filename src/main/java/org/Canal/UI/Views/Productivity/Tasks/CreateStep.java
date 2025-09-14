package org.Canal.UI.Views.Productivity.Tasks;

import org.Canal.Models.SupplyChainUnits.Task;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Includer;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * />$/MVMT/TSKS/NEW
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
        setFrameIcon(new ImageIcon(CreateStep.class.getResource("/icons/windows/locke.png")));

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
            task.setArea(areaField.getText());
            task.setBin(binField.getText());
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
        form.addInput(Elements.coloredLabel("Description", Constants.colors[0]), stepDescriptionField);
        form.addInput(Elements.coloredLabel("Location (ID)", Constants.colors[1]), locationField);
        form.addInput(Elements.coloredLabel("Area (ID)", Constants.colors[2]), areaField);
        form.addInput(Elements.coloredLabel("Bin (ID)", Constants.colors[3]), binField);
        form.addInput(Elements.coloredLabel("Locke Code", Constants.colors[4]), lockeField);
        form.addInput(Elements.coloredLabel("Item (ID)", Constants.colors[5]), itemField);
        form.addInput(Elements.coloredLabel("Quantity", Constants.colors[6]), quantityField);
        form.addInput(Elements.coloredLabel("Employees (Qty)", Constants.colors[7]), employeesField);
        general.add(form);

        return general;
    }

    private JPanel controls() {

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        goodsIssue = new JCheckBox("Creates Goods Issue & Removes Inventory");
        goodsReceipt = new JCheckBox("Creates Goods Receipt & Adds Inventory");

        Form form = new Form();
        form.addInput(Elements.coloredLabel("Goods Issue", Constants.colors[10]), goodsIssue);
        form.addInput(Elements.coloredLabel("Goods Receipt", Constants.colors[9]), goodsReceipt);
        controls.add(form);

        return controls;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }
}