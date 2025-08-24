package org.Canal;

import com.formdev.flatlaf.IntelliJTheme;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.UI.Views.System.IniSystem;
import org.Canal.UI.Views.System.Setup;
import org.Canal.Utils.*;
import org.Canal.UI.Views.System.QuickExplorer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Start {

    public static String DIR = System.getenv("APPDATA") + "\\Canal\\";
    public static QuickExplorer q;

    public static void main(String[] args) {
        try {
            IntelliJTheme.setup(Start.class.getResourceAsStream("/com/formdev/flatlaf/intellijthemes/themes/SolarizedLight.theme.json"));
        } catch (Exception e) {
            System.err.println("Failed FlatLaf dark theme.");
        }
        File appDataPath = new File(DIR);
        if(!appDataPath.exists()) {
            appDataPath.mkdirs();
            new Setup();
        }else{
            File[] mdf = appDataPath.listFiles();
            if (mdf != null && mdf.length > 0) {
                boolean hasConfiguration = false;
                for (int i = 0; i < mdf.length; i++) {
                    File file = mdf[i];
                    if (file.getPath().endsWith(".cnl.mfg")) {
                        hasConfiguration = true;
                        Engine.setConfiguration(Pipe.load(file.getPath(), Configuration.class));
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
                        ArrayList<Location> orgs = Engine.getLocations("ORGS");
                        if(orgs.isEmpty()){

                            new IniSystem();
                        } else {

                            Engine.setOrganization(orgs.getFirst());
                            if(Engine.getConfiguration().getAssignedUser() != null){
                                Engine.assignUser(Engine.getUser(Engine.getConfiguration().getAssignedUser()));
                            }
                            if(!Engine.getConfiguration().getMongodb().isEmpty()){

                            }
                            q = new QuickExplorer();
                        }
                    }else if(file.getPath().endsWith(".cdx")){
                        Engine.codex = Pipe.load(file.getPath(), Codex.class);
                    }
                }
                if(!hasConfiguration) {
                    new Setup();
                }
                if(Engine.codex == null){
                    Codex cdx = new Codex();
                    Engine.codex = cdx;
                    Pipe.export(DIR + "\\codex.cdx", cdx);
                }
            }else{
                new Setup();
            }
        }
    }
}