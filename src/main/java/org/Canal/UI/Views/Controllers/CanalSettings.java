package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Copiable;
import org.Canal.UI.Elements.Selectable;
import org.Canal.UI.Elements.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Form;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * /CNL
 */
public class CanalSettings extends LockeState {

    private Selectable themeOptions;
    private JCheckBox showCanalCodes;

    public CanalSettings(){

        super("Canal Settings", "/CNL", false, true, false, false);
        setFrameIcon(new ImageIcon(CanalSettings.class.getResource("/icons/settings.png")));

        JButton cr = Elements.button("Save");
        cr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedTheme = themeOptions.getSelectedValue();
                Engine.getConfiguration().setTheme(selectedTheme);
                Engine.getConfiguration().setShowCanalCodes(showCanalCodes.isSelected());
                Pipe.saveConfiguration();
                dispose();
            }
        });

        setLayout(new BorderLayout());
        JTabbedPane settings = new JTabbedPane();
        settings.add(generalSettings(), "General");
        settings.add(instanceVars(), "Instace Vars");
        settings.add(databaseConnection(), "Database");

        add(Elements.header("Canal Settings", SwingConstants.LEFT), BorderLayout.NORTH);
        add(settings, BorderLayout.CENTER);
        add(cr, BorderLayout.SOUTH);
    }

    public JPanel generalSettings(){
        Form f = new Form();
        themeOptions = Selectables.themes();
        if(Engine.getConfiguration().getTheme() != null){
            themeOptions.setSelectedValue(Engine.getConfiguration().getTheme());
        }
        HashMap<String, String> codeoptsMap = new HashMap<>();
        for(String t : Constants.getAllTransactions()){
            codeoptsMap.put(t, t);
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
        showCanalCodes = new JCheckBox();
        if(Engine.getConfiguration().showCanalCodes()){
            showCanalCodes.setSelected(true);
        }
        String assignedUser = "NOT_ASSIGNED/SIGN_IN";
        if(Engine.getAssignedUser() != null){
            assignedUser = Engine.getAssignedUser().getId();
        }
        f.addInput(new Label("Instance Name", UIManager.getColor("Label.foreground")), new Copiable(Engine.getConfiguration().getInstance_name()));
        f.addInput(new Label("Endpoint", UIManager.getColor("Label.foreground")), new Copiable(Engine.getConfiguration().getEndpoint()));
        f.addInput(new Label("Assigned User", UIManager.getColor("Label.foreground")), new Copiable(assignedUser));
        f.addInput(new Label("Font Size", Constants.colors[10]), Elements.input("12", 5));
        f.addInput(new Label("Theme", Constants.colors[9]), themeOptions);
        f.addInput(new Label("Upload Theme", Constants.colors[8]), uploadTheme);
        f.addInput(new Label("Background", Constants.colors[7]), backgroundChooser);
        f.addInput(new Label("Show Canal Codes", Constants.colors[6]), showCanalCodes);
        return f;
    }

    private JPanel instanceVars(){
        JPanel panel = new JPanel(new BorderLayout());
        ArrayList<Object[]> data = new ArrayList<>();
        CustomTable table = new CustomTable(new String[]{"Var.", "Value"}, data);
        add(table, BorderLayout.CENTER);
        return panel;
    }

    private JPanel databaseConnection(){
        JPanel panel = new JPanel(new BorderLayout());
        return panel;
    }
}