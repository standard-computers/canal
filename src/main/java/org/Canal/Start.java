package org.Canal;

import com.formdev.flatlaf.IntelliJTheme;
import org.Canal.Models.Codex;
import org.Canal.UI.Views.Controllers.QuickExplorer;
import org.Canal.UI.Views.Controllers.Setup;
import org.Canal.Utils.Configuration;
import org.Canal.Utils.Engine;
import org.Canal.Utils.Json;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Start {

    public static String WINDOWS_SYSTEM_DIR = System.getenv("APPDATA") + "\\Canal\\";
    public static QuickExplorer q;

    public static void main(String[] args) {
        try {
            IntelliJTheme.setup(Start.class.getResourceAsStream("/com/formdev/flatlaf/intellijthemes/themes/SolarizedLight.theme.json"));
        } catch (Exception e) {
            System.err.println("Failed FlatLaf dark theme.");
        }
        File appDataPath = new File(WINDOWS_SYSTEM_DIR);
        if(!appDataPath.exists()) {
            appDataPath.mkdirs();
            new Setup();
        }else{
            File md = new File(WINDOWS_SYSTEM_DIR);
            File[] mdf = md.listFiles();
            if (mdf != null && mdf.length > 0) {
                boolean hasConfiguration = false;
                for (int i = 0; i < mdf.length; i++) {
                    File file = mdf[i];
                    if (file.getPath().endsWith(".cnl.mfg")) {
                        hasConfiguration = true;
                        Engine.setConfiguration(Json.load(file.getPath(), Configuration.class));
                        try {
                            Font defaultFont = UIManager.getFont("defaultFont");
                            if (defaultFont != null) {
                                UIManager.put("defaultFont", defaultFont.deriveFont(12f));
                            }
                            IntelliJTheme.setup(Start.class.getResourceAsStream(Engine.getConfiguration().getTheme()));
                        } catch (Exception e) {
                            System.err.println("Failed self assigned theme.");
                        }
                        i = mdf.length + 1;
                        Engine.setOrganization(Engine.getLocations("ORGS").get(0));
                        if(Engine.getConfiguration().getAssignedUser() != null){
                            Engine.assignUser(Engine.getUser(Engine.getConfiguration().getAssignedUser()));
                        }
                        q = new QuickExplorer();
                    }else if(file.getPath().endsWith(".cdx")){
                        Engine.codex = Json.load(file.getPath(), Codex.class);
                    }
                }
                if(!hasConfiguration) {
                    new Setup();
                }
                if(Engine.codex == null){
                    Codex cdx = new Codex();
                    Engine.codex = cdx;
                    Json.save(WINDOWS_SYSTEM_DIR + "\\codex.cdx", cdx);
                }
            }else{
                new Setup();
            }
        }
    }
}