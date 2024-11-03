package org.Canal.UI.Views.Controllers;

import com.formdev.flatlaf.IntelliJTheme;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.Inputs.Copiable;
import org.Canal.UI.Elements.Inputs.Selectable;
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

    public CanalSettings(){
        super("Canal Settings", false, true, false, false);
        setFrameIcon(new ImageIcon(CanalSettings.class.getResource("/icons/settings.png")));
        JPanel l = new JPanel(new BorderLayout());
        Form f = new Form();
        HashMap<String, String> themeMap = new HashMap<>();
        themeMap.put("Arc", "/com/formdev/flatlaf/intellijthemes/themes/arc-theme.theme.json");
        themeMap.put("Arc Dark", "/com/formdev/flatlaf/intellijthemes/themes/arc_theme_dark.theme.json");
        themeMap.put("Arc Orange", "/com/formdev/flatlaf/intellijthemes/themes/arc-theme-orange.theme.json");
        themeMap.put("Arc Dark Orange", "/com/formdev/flatlaf/intellijthemes/themes/arc_theme_dark_orange.theme.json");
        themeMap.put("Carbon", "/com/formdev/flatlaf/intellijthemes/themes/Carbon.theme.json");
        themeMap.put("Cobalt 2", "/com/formdev/flatlaf/intellijthemes/themes/Cobalt_2.theme.json");
        themeMap.put("Cyan", "/com/formdev/flatlaf/intellijthemes/themes/Cyan.theme.json");
        themeMap.put("Dark Flat", "/com/formdev/flatlaf/intellijthemes/themes/DarkFlatTheme.theme.json");
        themeMap.put("Dark Purple", "/com/formdev/flatlaf/intellijthemes/themes/DarkPurple.theme.json");
        themeMap.put("Dracula", "/com/formdev/flatlaf/intellijthemes/themes/Dracula.theme.json");
        themeMap.put("Gradianto Dark Fuchsia", "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_dark_fuchsia.theme.json");
        themeMap.put("Gradianto Deep Ocean", "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_deep_ocean.theme.json");
        themeMap.put("Gradianto Midnight Blue", "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_midnight_blue.theme.json");
        themeMap.put("Gradianto Nature Green", "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_Nature_Green.theme.json");
        themeMap.put("Gray", "/com/formdev/flatlaf/intellijthemes/themes/Gray.theme.json");
        themeMap.put("Gruvbox Dark Hard", "/com/formdev/flatlaf/intellijthemes/themes/gruvbox_dark_hard.theme.json");
        themeMap.put("Gruvbox Dark Medium", "/com/formdev/flatlaf/intellijthemes/themes/gruvbox_dark_medium.theme.json");
        themeMap.put("Gruvbox Dark Soft", "/com/formdev/flatlaf/intellijthemes/themes/gruvbox_dark_soft.theme.json");
        themeMap.put("Solarized Light", "/com/formdev/flatlaf/intellijthemes/themes/SolarizedLight.theme.json");
        themeMap.put("Solarized Dark", "/com/formdev/flatlaf/intellijthemes/themes/SolarizedDark.theme.json");
        themeMap.put("Flat Monocai", "/com/formdev/flatlaf/intellijthemes/themes/Monocai.theme.json");
        themeMap.put("Flat Monocai Pro", "/com/formdev/flatlaf/intellijthemes/themes/Monokai_Pro.default.theme.json");
        themeMap.put("Nord", "/com/formdev/flatlaf/intellijthemes/themes/nord.theme.json");
        themeMap.put("One Dark", "/com/formdev/flatlaf/intellijthemes/themes/one_dark.theme.json");
        themeMap.put("Vuesion", "/com/formdev/flatlaf/intellijthemes/themes/vuesion_theme.theme.json");
        themeMap.put("Xcode Dark", "/com/formdev/flatlaf/intellijthemes/themes/Xcode-Dark.theme.json");
        themeMap.put("Hiberbee Dark", "/com/formdev/flatlaf/intellijthemes/themes/HiberbeeDark.theme.json");
        themeMap.put("Spacegray", "/com/formdev/flatlaf/intellijthemes/themes/Spacegray.theme.json");
        themeMap.put("High Contrast", "/com/formdev/flatlaf/intellijthemes/themes/HighContrast.theme.json");
        themeMap.put("Light Flat", "/com/formdev/flatlaf/intellijthemes/themes/LightFlatTheme.theme.json");
        themeMap.put("Material Theme", "/com/formdev/flatlaf/intellijthemes/themes/MaterialTheme.theme.json");
        themeMap.put("~Material Arc Dark", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Arc Dark.theme.json");
        themeMap.put("~Material Atom One Dark", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Atom One Dark.theme.json");
        themeMap.put("~Material Atom One Light", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Atom One Light.theme.json");
        themeMap.put("~Material Dracula", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Dracula.json");
        themeMap.put("~Material GitHub", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/GitHub.theme.json");
        themeMap.put("~Material GitHub Dark", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/GitHub Dark.theme.json");
        themeMap.put("~Material Light Owl", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Light Owl.theme.json");
        themeMap.put("~Material Material Darker", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Darker.theme.json");
        themeMap.put("~Material Material Deep Ocean", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Deep Ocean.theme.json");
        themeMap.put("~Material Material Lighter", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Lighter.theme.json");
        themeMap.put("~Material Material Oceanic", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Oceanic.theme.json");
        themeMap.put("~Material Material Palenight", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Material Palenight.theme.json");
        themeMap.put("~Material Monokai Pro", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Monokai Pro.theme.json");
        themeMap.put("~Material Night Owl", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Night Owl.theme.json");
        themeMap.put("~Material Solarized Dark", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Solarized Dark.theme.json");
        themeMap.put("~Material Solarized Light", "/com/formdev/flatlaf/intellijthemes/themes/material-theme-ui-lite/Solarized Light.theme.json");
        Selectable themeOptions = new Selectable(themeMap);
        if(Engine.getConfiguration().getTheme() != null){
            themeOptions.setSelectedValue(Engine.getConfiguration().getTheme());
        }
        themeOptions.addActionListener(_ -> {
            String selectedTheme = (String) themeOptions.getSelected();
            if (selectedTheme != null) {
                String themePath = themeMap.get(selectedTheme);
                try {
                    IntelliJTheme.setup(Selectable.class.getResourceAsStream(themePath));
                    for (Window window : Window.getWindows()) {
                        SwingUtilities.updateComponentTreeUI(window);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        HashMap<String, String> codeoptsMap = new HashMap<>();
        for(String t : Constants.getAllTransactions()){
            codeoptsMap.put(t, t);
        }
        Selectable codeSelect = new Selectable(codeoptsMap);
        codeSelect.editable();
        JCheckBox showCanalCodes = new JCheckBox();
        if(Engine.getConfiguration().isShowCanalCodes()){
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
        l.add(f, BorderLayout.CENTER);
        l.add(cr, BorderLayout.SOUTH);
        add(l);
    }
}