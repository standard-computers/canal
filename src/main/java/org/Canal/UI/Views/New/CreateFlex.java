package org.Canal.UI.Views.New;

import org.Canal.Models.SupplyChainUnits.Flex;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CreateFlex extends JInternalFrame {

    private JTable table;
    private DefaultTableModel model;
    private Flex item;
    private String tcode;

    public CreateFlex(Flex item, String type, String tcode) {
        setTitle("Create " + type);
        this.item = item;
        this.tcode = tcode;
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Properties", "Values"});
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadDefaultProperties();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> saveJson());
        add(saveButton, BorderLayout.SOUTH);
    }

    private void loadDefaultProperties() {
        Map<String, String> defaultProperties = item.getProperties();
        for (Map.Entry<String, String> entry : defaultProperties.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private void saveJson() {
        Map<String, String> updatedProperties = new HashMap<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String property = (String) model.getValueAt(i, 0);
            String value = (String) model.getValueAt(i, 1);
            updatedProperties.put(property, value);
        }
        item.updateProperties(updatedProperties);
        Pipe.save(tcode, item);
        dispose();
    }
}