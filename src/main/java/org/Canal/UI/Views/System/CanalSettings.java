package org.Canal.UI.Views.System;

import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * /CNL
 */
public class CanalSettings extends LockeState {

    private Selectable themeOptions;
    private JTextField fontSizeField;
    private JCheckBox saveLockeState;
    private JCheckBox showLockeCodes;
    private JCheckBox showButtonLabels;

    public CanalSettings(){

        super("Canal Settings", "/CNL", false, true, false, false);
        setFrameIcon(new ImageIcon(CanalSettings.class.getResource("/icons/settings.png")));

        JButton cr = Elements.button("Save");
        cr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedTheme = themeOptions.getSelectedValue();
                Engine.getConfiguration().setFontSize(Integer.parseInt(fontSizeField.getText()));
                Engine.getConfiguration().setTheme(selectedTheme);
                Engine.getConfiguration().setShowCanalCodes(showLockeCodes.isSelected());
                Pipe.saveConfiguration();
                dispose();
            }
        });

        setLayout(new BorderLayout());
        JTabbedPane settings = new JTabbedPane();
        settings.add(generalSettings(), "General");
        settings.add(instanceVars(), "Instace Variables");
        settings.add(databaseConnection(), "Database");

        add(Elements.header("Canal Settings", SwingConstants.LEFT), BorderLayout.NORTH);
        add(settings, BorderLayout.CENTER);
        add(cr, BorderLayout.SOUTH);
    }

    public JPanel generalSettings(){

        Form f = new Form();
        fontSizeField = Elements.input(String.valueOf(Engine.getConfiguration().getFontSize()), 5);
        themeOptions = Selectables.themes();
        if(Engine.getConfiguration().getTheme() != null){
            themeOptions.setSelectedValue(Engine.getConfiguration().getTheme());
        }
        JButton backgroundChooser = Elements.button("Choose File");
        backgroundChooser.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File file = chooser.getSelectedFile();

                }
            }
        });
        JButton uploadTheme = Elements.button("Upload Theme");
        saveLockeState = new JCheckBox("Reopen windows");
        showLockeCodes = new JCheckBox("Show locke codes in menus");
        showButtonLabels = new JCheckBox("Effects icon buttons");
        if(Engine.getConfiguration().showCanalCodes()){
            showLockeCodes.setSelected(true);
        }
        String assignedUser = "NOT_ASSIGNED/SIGN_IN";
        if(Engine.getAssignedUser() != null){
            assignedUser = Engine.getAssignedUser().getId();
        }
        f.addInput(Elements.coloredLabel("Instance Name", UIManager.getColor("Label.foreground")), new Copiable(Engine.getConfiguration().getInstance_name()));
        f.addInput(Elements.coloredLabel("Endpoint", UIManager.getColor("Label.foreground")), new Copiable(Engine.getConfiguration().getEndpoint()));
        f.addInput(Elements.coloredLabel("Assigned User", UIManager.getColor("Label.foreground")), new Copiable(assignedUser));
        f.addInput(Elements.coloredLabel("Font Size", Constants.colors[10]), fontSizeField);
        f.addInput(Elements.coloredLabel("Theme", Constants.colors[9]), themeOptions);
        f.addInput(Elements.coloredLabel("Upload Theme", Constants.colors[8]), uploadTheme);
        f.addInput(Elements.coloredLabel("Background", Constants.colors[7]), backgroundChooser);
        f.addInput(Elements.coloredLabel("Save Locke State", Constants.colors[6]), saveLockeState);
        f.addInput(Elements.coloredLabel("Show Locke Codes", Constants.colors[5]), showLockeCodes);
        f.addInput(Elements.coloredLabel("Show Button Labels", Constants.colors[4]), showButtonLabels);
        return f;
    }

    private JPanel instanceVars(){

        JPanel panel = new JPanel(new BorderLayout());
        ArrayList<Object[]> data = new ArrayList<>();
        for(Map.Entry<String, HashMap<String, Object>> outerEntry : Engine.codex.getVariables().entrySet()){
            String outerKey = outerEntry.getKey(); // Key of the outer HashMap (e.g., "/", "ORGS")
            HashMap<String, Object> innerMap = outerEntry.getValue(); // Inner HashMap

            // Iterate through the inner HashMap
            for (Map.Entry<String, Object> innerEntry : innerMap.entrySet()) {
                String innerKey = innerEntry.getKey(); // Key of the inner HashMap (e.g., "minumim_build")
                Object value = innerEntry.getValue(); // Value of the inner HashMap (e.g., 1, "USD")

                // Add a row to the data
                data.add(new Object[]{outerKey, innerKey, value});
            }
        }
        CustomTable table = new CustomTable(new String[]{"Objex", "Variable", "Value"}, data);
        add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel databaseConnection(){

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if(Engine.getConfiguration().getEndpoint().equals("127.0.0.1")){
            panel.add(Elements.h3("Local Canal Instance"));
            JButton openFileLocation = Elements.button("Open File Location");
            panel.add(openFileLocation);
        }
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panel;
    }
}