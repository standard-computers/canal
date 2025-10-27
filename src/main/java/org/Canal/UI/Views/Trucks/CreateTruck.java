package org.Canal.UI.Views.Trucks;

import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * /TRANS/TRCKS/NEW
 */
public class CreateTruck extends LockeState {

    private DesktopState desktop;

    //General Info Tab
    private JTextField truckIdField;
    private JTextField truckNumberField;
    private Selectable carriers;
    private JTextField driverNameField;
    private JTextField licensePlateField;
    private JTextField vinField;
    private JTextField yearNameField;
    private JTextField makeNameField;
    private JTextField modelNameField;

    //Dimensional Tab
    private UOMField weightField;
    private UOMField maxWeightField;

    //Notes Tab
    private RTextScrollPane notes;

    public CreateTruck(DesktopState desktop) {

        super("Create a Truck", "/TRANS/TRCKS/NEW", false, true, false, true);
        setFrameIcon(new ImageIcon(CreateTruck.class.getResource("/icons/create.png")));
        this.desktop = desktop;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());
        tabs.addTab("Dimensional", dimensional());

        if((boolean) Engine.codex.getValue("TRANS/TRCKS", "allow_notes")) {
            tabs.addTab("Notes", notes());
        }

        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.add(Elements.header("New Truck", SwingConstants.LEFT), BorderLayout.NORTH);
        header.add(toolbar(), BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    public JPanel toolbar() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        tb.add(Box.createHorizontalStrut(5));

        IconButton copyFrom = new IconButton("Copy From", "open", "Copy Area");
        copyFrom.addActionListener(_ -> {

            String truckId = JOptionPane.showInputDialog(CreateTruck.this, "Enter Truck ID", "Copy Truck", JOptionPane.QUESTION_MESSAGE);
            if (!truckId.isEmpty()) {
                Truck foundTruck = Engine.getTruck(truckId);
                if (foundTruck != null) {
                    truckNumberField.setText(foundTruck.getNumber());
                    carriers.setSelectedValue(foundTruck.getCarrier());
                    driverNameField.setText(foundTruck.getDriver());
                    yearNameField.setText(foundTruck.getYear());
                    makeNameField.setText(foundTruck.getMake());
                    modelNameField.setText(foundTruck.getModel());
                    notes.getTextArea().setText(foundTruck.getNotes());
                }
            }
        });
        tb.add(copyFrom);
        tb.add(Box.createHorizontalStrut(5));

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton create = new IconButton("Create", "create", "Refresh Data");
        create.addActionListener(_ -> {

            Truck truck = new Truck();
            truck.setId(truckIdField.getText());
            truck.setNumber(truckNumberField.getText());
            truck.setCarrier(carriers.getSelectedValue());
            truck.setDriver(driverNameField.getText());
            truck.setLicensePlate(licensePlateField.getText());
            truck.setYear(yearNameField.getText());
            truck.setYear(yearNameField.getText());
            truck.setMake(makeNameField.getText());
            truck.setModel(modelNameField.getText());
            truck.setWeight(Double.parseDouble(weightField.getValue()));
            truck.setWeightUOM(weightField.getUOM());
            truck.setMaxWeight(Double.parseDouble(maxWeightField.getValue()));
            truck.setMaxWeightUOM(maxWeightField.getUOM());
            truck.setNotes(notes.getTextArea().getText().trim());
            Pipe.save("/TRANS/TRCKS/NEW", truck);
            dispose();
            JOptionPane.showMessageDialog(null, "Truck created");
            Engine.router("/TRANS/TRCKS/" + truckIdField.getText(), desktop);
        });
        tb.add(create);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-create");
        rp.getActionMap().put("do-create", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create.doClick();
            }
        });

        panel.add(tb, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        truckIdField = Elements.input((String) Engine.codex("TRANS/TRCKS", "prefix") + 1000 + (Engine.getTrucks().size() + 1));
        truckNumberField = Elements.input(15);
        carriers = Selectables.carriers();
        driverNameField = Elements.input();
        licensePlateField = Elements.input();
        vinField = Elements.input();
        yearNameField = Elements.input();
        makeNameField = Elements.input();
        modelNameField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Truck ID"), truckIdField);
        form.addInput(Elements.inputLabel("Truck Number"), truckNumberField);
        form.addInput(Elements.inputLabel("Carrier"), carriers);
        form.addInput(Elements.inputLabel("Driver (Name)"), driverNameField);
        form.addInput(Elements.inputLabel("License Plate"), licensePlateField);
        form.addInput(Elements.inputLabel("VIN"), vinField);
        form.addInput(Elements.inputLabel("Vehicle Year"), yearNameField);
        form.addInput(Elements.inputLabel("Vehicle Make"), makeNameField);
        form.addInput(Elements.inputLabel("Vehicle Model"), modelNameField);
        general.add(form);

        return general;
    }

    private JPanel dimensional() {

        JPanel dimensional = new JPanel(new FlowLayout(FlowLayout.LEFT));

        weightField = new UOMField();
        maxWeightField = new UOMField();

        Form form = new Form();
        form.addInput(Elements.inputLabel("Vehicle Weight"), weightField);
        form.addInput(Elements.inputLabel("Max Weight (Load, Carrying)"), maxWeightField);
        dimensional.add(form);

        return dimensional;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        return notes;
    }

    private void performReview() {

    }
}