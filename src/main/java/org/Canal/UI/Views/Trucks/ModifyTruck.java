package org.Canal.UI.Views.Trucks;

import org.Canal.Models.SupplyChainUnits.Truck;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * /TRANS/TRCKS/MOD/$[TRUCK_ID]
 */
public class ModifyTruck extends LockeState {

    private Truck truck;
    private DesktopState desktop;

    //General Info Tab
    private JTextField truckIdField;
    private JTextField truckNumberField;
    private Selectable carriers;
    private JTextField driverNameField;
    private JTextField yearNameField;
    private JTextField makeNameField;
    private JTextField modelNameField;

    //Notes Tab
    private RTextScrollPane notes;

    public ModifyTruck(Truck truck, DesktopState desktop) {

        super("Create a Truck", "/TRANS/TRCKS/NEW", false, true, false, true);
        this.truck = truck;
        this.desktop = desktop;

        CustomTabbedPane tabs = new CustomTabbedPane();
        tabs.addTab("General", general());

        if ((boolean) Engine.codex.getValue("TRANS/TRCKS", "allow_notes")) {
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

        IconButton review = new IconButton("Review", "review", "Review Area Data");
        review.addActionListener(_ -> performReview());
        tb.add(review);
        tb.add(Box.createHorizontalStrut(5));

        IconButton save = new IconButton("Save", "save", "Save changes");
        save.addActionListener(_ -> {

            Truck truck = new Truck();
            truck.setId(truckIdField.getText());
            truck.setNumber(truckNumberField.getText());
            truck.setCarrier(carriers.getSelectedValue());
            truck.setDriver(driverNameField.getText());
            truck.setYear(yearNameField.getText());
            truck.setMake(makeNameField.getText());
            truck.setModel(modelNameField.getText());
            truck.setNotes(notes.getTextArea().getText().trim());
            truck.save();

            dispose();
            JOptionPane.showMessageDialog(null, "Truck created");
            Engine.router("/TRANS/TRCKS/" + truckIdField.getText(), desktop);
        });
        tb.add(save);
        tb.add(Box.createHorizontalStrut(5));
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "do-save");
        rp.getActionMap().put("do-save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save.doClick();
            }
        });

        panel.add(tb, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel general() {

        JPanel general = new JPanel(new FlowLayout(FlowLayout.LEFT));

        truckIdField = new JTextField((String) Engine.codex("TRANS/TRCKS", "prefix") + 1000 + (Engine.getTrucks().size() + 1));
        truckNumberField = Elements.input(15);
        carriers = Selectables.carriers();
        driverNameField = Elements.input();
        yearNameField = Elements.input();
        makeNameField = Elements.input();
        modelNameField = Elements.input();

        Form form = new Form();
        form.addInput(Elements.inputLabel("*New Truck ID"), truckIdField);
        form.addInput(Elements.inputLabel("Truck Number"), truckNumberField);
        form.addInput(Elements.inputLabel("Carrier"), carriers);
        form.addInput(Elements.inputLabel("Driver (Name)"), driverNameField);
        form.addInput(Elements.inputLabel("Vehicle Year"), yearNameField);
        form.addInput(Elements.inputLabel("Vehicle Make"), makeNameField);
        form.addInput(Elements.inputLabel("Vehicle Model"), modelNameField);
        general.add(form);

        return general;
    }

    private RTextScrollPane notes() {

        notes = Elements.simpleEditor();
        notes.getTextArea().setText(truck.getNotes());
        return notes;
    }

    private void performReview() {

    }
}