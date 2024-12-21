package org.Canal.UI.Views.Finance.SalesOrders;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.DatePicker;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * /ORDS/SO/AUTO_MK
 */
public class AutoMakeSalesOrders extends LockeState {

    private JPanel checkboxPanel;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes;
    private Selectable vendor;
    private JTextField maxSpendField;
    private DatePicker prStartDateField, prEndDateField;
    private JTextArea prNotesField;

    public AutoMakeSalesOrders() {
        super("AutoMake Sales Orders", "/ORDS/SO/AUTO_MK", false, true, false, true);
        setFrameIcon(new ImageIcon(AutoMakeSalesOrders.class.getResource("/icons/automake.png")));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(buyers(), "Buyers");
        tabbedPane.add(salesOrderData(), "SO Data");
        tabbedPane.add(itemData(), "Items");
        JLabel description = Elements.h3("Creates a Sales Order for each customer with info.");
        description.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(description, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        JButton createPrs = Elements.button("AutoMake");
        createPrs.addActionListener(_ -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (JCheckBox checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    String genId = "PR" + (10000000 + (Engine.orderProcessing.getPurchaseRequisitions().size() + 1));
                    PurchaseRequisition newPr = new PurchaseRequisition(genId, genId, "U10001", vendor.getSelectedValue(), checkbox.getActionCommand(), genId, Double.valueOf(maxSpendField.getText()), dateFormat.format(prStartDateField.getSelectedDate()), dateFormat.format(prEndDateField.getSelectedDate()), prNotesField.getText());
                    Pipe.save("/PR", newPr);
                }
            }
            dispose();
            JOptionPane.showMessageDialog(null, "AutoMake Complete");
        });
        add(createPrs, BorderLayout.SOUTH);
    }

    private JPanel buyers() {
        locations = Engine.getLocations("CCS");
        locations.addAll(Engine.getLocations("DCS"));
        this.checkboxes = new ArrayList<>();
        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        addCheckboxes();
//        /ORDS/SO/AUTO_MK
        return checkboxPanel;
    }

    private JPanel salesOrderData() {
        Form addtlInfo = new Form();
        addtlInfo.setBorder(new TitledBorder("Additional Info"));
        JTextField maxSpendField = Elements.input(10);
        JCheckBox isSingleOrder = new JCheckBox("");
        Selectable supplier = Selectables.allLocations();
        supplier.editable();
        DatePicker prStartDateField = new DatePicker();
        DatePicker prEndDateField = new DatePicker();
        JTextArea prNotesField = new JTextArea();
        addtlInfo.addInput(new Label("*Created", UIManager.getColor("Label.foreground")), new Copiable(Constants.now()));
        addtlInfo.addInput(new Label("Max Spend", Constants.colors[9]), maxSpendField);
        addtlInfo.addInput(new Label("[or] Single Order", UIManager.getColor("Label.foreground")), isSingleOrder);
        addtlInfo.addInput(new Label("Supplier/Vendor", Constants.colors[7]), supplier);
        addtlInfo.addInput(new Label("Valid From", Constants.colors[6]), prStartDateField);
        addtlInfo.addInput(new Label("To", Constants.colors[5]), prEndDateField);
        addtlInfo.addInput(new Label("Notes", Constants.colors[4]), prNotesField);
        return addtlInfo;
    }

    private JPanel itemData() {
        JPanel p = new JPanel();

        return p;
    }

    private void addCheckboxes() {
        for (Location location : locations) {
            String displayText = location.getId() + " - " + location.getName();
            JCheckBox checkbox = new JCheckBox(displayText);
            checkbox.setActionCommand(String.valueOf(location.getId()));
            checkboxes.add(checkbox);
            checkboxPanel.add(checkbox);
        }
    }
}