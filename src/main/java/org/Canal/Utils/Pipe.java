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
        Engine.load();
    }

    public static void saveConfiguration() {
        File md = new File(Start.WINDOWS_SYSTEM_DIR);
        File[] mdf = md.listFiles();
        if (mdf != null && mdf.length > 0) {
            for (int i = 0; i < mdf.length; i++) {
                File file = mdf[i];
                if (file.getPath().endsWith(".cnl.mfg")) {
                    Configuration newConfig = new Configuration(Engine.getConfiguration().getEndpoint(), Engine.getConfiguration().getInstance_name());
                    newConfig.setDefaultModule(Engine.getConfiguration().getDefaultModule());
                    newConfig.setTheme(Engine.getConfiguration().getTheme());
                    Json.save(file.getPath(), newConfig);
                    break;
                }
            }
        }
    }
}
