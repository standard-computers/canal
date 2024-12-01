package org.Canal.Utils;

import org.Canal.Start;
import java.io.File;
import java.util.UUID;

public class Pipe {

    public static File[] list(String dir){
        return new File(Start.WINDOWS_SYSTEM_DIR + "\\.store\\" + dir).listFiles();
    }

    public static void save(String dir, Object o){
        File f = new File(Start.WINDOWS_SYSTEM_DIR + "\\.store\\" + dir);
        if(!f.exists()){
            f.mkdirs();
        }
        Json.save(Start.WINDOWS_SYSTEM_DIR + "\\.store\\" + dir + "\\" + UUID.randomUUID() + dir.replace("/", ".").toLowerCase(), o);
    }

    public static void saveConfiguration() {
        File md = new File(Start.WINDOWS_SYSTEM_DIR);
        File[] mdf = md.listFiles();
        boolean existed = false;
        if (mdf != null) {
            for (File file : mdf) {
                if (file.getPath().endsWith(".cnl.mfg")) {
                    existed = true;
                    Json.save(file.getPath(), Engine.getConfiguration());
                }
            }
        }
        if (!existed) {
            Json.save(Start.WINDOWS_SYSTEM_DIR + UUID.randomUUID() + ".cnl.mfg", Engine.getConfiguration());
        }
    }
}