package org.Canal.UI.Views.Batching;

import org.Canal.UI.Elements.Input;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class BatchCreateLocations extends JInternalFrame {

    private JTable table;
    private DefaultTableModel model;
    private String tcode;

    public BatchCreateLocations(String type, String tcode, String tieValue) {
        super("New " + type);
        this.tcode = tcode;
        setSize(400, 300);
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name"});
        table = new JTable(model);
        Input tcodeInput = new Input("Transaction");
        tcodeInput.setValue(tcode);
        Input tieVal = new Input("Tie Value (Blongs To)");
        tieVal.setValue(tieValue);
        JPanel mo = new JPanel(new GridLayout(1, 2));
        mo.add(tcodeInput);
        mo.add(tieVal);
        add(mo, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                    pasteCsvData();
                }
            }
        });
        loadDefaultProperties();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> saveJson());
        add(saveButton, BorderLayout.SOUTH);
        setResizable(false);
    }

    private void loadDefaultProperties() {
        model.addRow(new Object[]{"", "", "", "", "", "", "", ""});
        model.addRow(new Object[]{"", "", "", "", "", "", "", ""});
        model.addRow(new Object[]{"", "", "", "", "", "", "", ""});
    }

    private void pasteCsvData() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            String data = (String) clipboard.getData(DataFlavor.stringFlavor);
            String[] rows = data.split("\n");
            for (String row : rows) {
                String[] values = row.split(",");
                model.addRow(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveJson() {
        Map<String, String> updatedProperties = new HashMap<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String property = (String) model.getValueAt(i, 0);
            String value = (String) model.getValueAt(i, 1);
            updatedProperties.put(property, value);
        }
        Pipe.save(tcode, null);
        dispose();
    }
}