package org.Canal.UI.Views.Controllers;

import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.CustomJTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Inputs.Selectable;
import org.Canal.UI.Elements.Inputs.Selectables;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.Windows.Form;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * /CNL
 */
public class CanalSettings extends JInternalFrame {

    private Selectable themeOptions;
    private Selectable codeSelect;
    private JCheckBox showCanalCodes;

    public CanalSettings(){
        super("Canal Settings", false, true, false, false);
        setFrameIcon(new ImageIcon(CanalSettings.class.getResource("/icons/settings.png")));

        Button cr = new Button("Save");
        cr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedTheme = themeOptions.getSelectedValue();
                Engine.getConfiguration().setTheme(selectedTheme);
                Engine.getConfiguration().setDefaultModule(codeSelect.getSelectedValue());
                Engine.getConfiguration().setShowCanalCodes(showCanalCodes.isSelected());
                Pipe.saveConfiguration();
                dispose();
            }
        });

        setLayout(new BorderLayout());
        JTabbedPane settings = new JTabbedPane();
        settings.add(generalSettings(), "General");
        settings.add(instanceVars(), "Instace Vars");

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
        codeSelect = new Selectable(codeoptsMap);
        codeSelect.editable();
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
        f.addInput(new Label("Launch Module(s)", Constants.colors[8]), codeSelect);
        f.addInput(new Label("Background", Constants.colors[7]), new Button("Choose File"));
        f.addInput(new Label("Show Canal Codes", Constants.colors[6]), showCanalCodes);
        return f;
    }

    private JPanel instanceVars(){
        JPanel panel = new JPanel();
        String[][] data = new String[][]{};
        CustomJTable table = new CustomJTable(data, new String[]{"Var.", "Value"});add(table);
        return panel;
    }
}