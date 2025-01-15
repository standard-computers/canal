package org.Canal.UI.Views.Finance.PurchaseRequisitions;

import org.Canal.Models.BusinessUnits.PurchaseRequisition;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import org.apache.commons.compress.utils.Lists;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * /ORDS/PR/AUTO_MK
 */
public class AutoMakePurchaseRequisitions extends LockeState {

    private JPanel checkboxPanel;
    private JTextField descriptionField;
    private JTextField maxSpendField;
    private JCheckBox isSingleOrder;
    private ArrayList<Location> locations;
    private ArrayList<JCheckBox> checkboxes = new ArrayList<>();
    private DatePicker prStartDateField;
    private DatePicker prEndDateField;
    private Selectable suppliers;
    private RTextScrollPane notes;

    public AutoMakePurchaseRequisitions() {

        super("AutoMake Purchase Reqs.", "/ORDS/PR/AUTO_MK", false, true, false, true);
        setFrameIcon(new ImageIcon(AutoMakePurchaseRequisitions.class.getResource("/icons/automake.png")));

        locations = Engine.getLocations();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Information", information());
        tabs.addTab("Buyers", buyers());
        tabs.addTab("Notes", notes());


        JLabel description = Elements.h3("Creates a Purchase Req. for each buyer from selected supplier with info.");
        description.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(description, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        JButton createPrs = Elements.button("AutoMake Purchase Reqs.");
        createPrs.addActionListener(_ -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (JCheckBox checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    String genId = "PR" + (10000000 + (Engine.orders.getPurchaseRequisitions().size() + 1));
                    PurchaseRequisition newPr = new PurchaseRequisition(genId, genId, "U10001", suppliers.getSelectedValue(), checkbox.getActionCommand(), genId, Double.valueOf(maxSpendField.getText()), dateFormat.format(prStartDateField.getSelectedDate()), dateFormat.format(prEndDateField.getSelectedDate()), notes.getTextArea().getText());
                    Pipe.save("/ORDS/PR", newPr);
                }
            }
            dispose();
            JOptionPane.showMessageDialog(null, "AutoMake Purchase Reqs Complete");
        });
        add(createPrs, BorderLayout.SOUTH);
    }

    private JPanel information() {

        JPanel information = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionField = Elements.input(10);
        maxSpendField = Elements.input();
        isSingleOrder = new JCheckBox();
        suppliers = Selectables.vendors();
        prStartDateField = new DatePicker();
        prEndDateField = new DatePicker();
        Form f = new Form();
        f.addInput(Elements.coloredLabel("*Created", UIManager.getColor("Label.foreground")), new Copiable(Constants.now()));
        f.addInput(Elements.coloredLabel("Description", Constants.colors[9]), descriptionField);
        f.addInput(Elements.coloredLabel("Max Spend", Constants.colors[8]), maxSpendField);
        f.addInput(Elements.coloredLabel("[or] Single Order", UIManager.getColor("Label.foreground")), isSingleOrder);
        f.addInput(Elements.coloredLabel("Supplier", Constants.colors[7]), suppliers);
        f.addInput(Elements.coloredLabel("Start Date", Constants.colors[6]), prStartDateField);
        f.addInput(Elements.coloredLabel("End Date", Constants.colors[5]), prEndDateField);
        information.add(f);
        return information;
    }

    private JScrollPane buyers(){

        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        addCheckboxes();
        JScrollPane sp = new JScrollPane(checkboxPanel);
        sp.setPreferredSize(new Dimension(300, 300));
        return sp;
    }

    private void addCheckboxes() {

        for (Location location : locations) {
            String displayText = location.getId() + " - " + location.getName();
            JCheckBox checkbox = new JCheckBox(displayText);
            checkbox.setActionCommand(String.valueOf(location.getId())); // Set the value as ID
            checkboxes.add(checkbox);
            checkboxPanel.add(checkbox);
        }
    }

    private JScrollPane notes(){

        notes = Elements.simpleEditor();
        return notes;
    }
}
