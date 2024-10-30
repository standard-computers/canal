package org.Canal.UI.Views.Managers;

import com.formdev.flatlaf.IntelliJTheme;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Elements.Label;
import org.Canal.UI.Elements.*;
import org.Canal.Utils.Constants;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Pipe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class CanalSettings extends JInternalFrame {

    public CanalSettings(){
        setTitle("System Settings");
        JPanel l = new JPanel(new BorderLayout());
        Form f = new Form();
        f.addInput(new Label("Font Size", new Color(255, 178, 102)), new JTextField("12"));
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
        f.addInput(new Label("Theme", new Color(255, 255, 102)), themeOptions);
        themeOptions.addActionListener(e -> {
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

        f.addInput(new Label("Launch Module", new Color(255, 255, 102)), codeSelect);
        Button cr = new Button("Save");
        cr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String associatedValue = themeOptions.getSelectedValue();
                Engine.setDefaultModule(codeSelect.getSelectedValue());
                Engine.setTheme(associatedValue);
                Pipe.saveConfiguration();
                dispose();
            }
        });
        l.add(f, BorderLayout.CENTER);
        l.add(cr, BorderLayout.SOUTH);
        add(l);
        setClosable(true);
    }
}