package org.Canal.Utils;

import com.google.gson.JsonObject;
import org.Canal.Models.Objex;
import org.Canal.Start;
import java.io.File;
import java.util.UUID;

public class Pipe {

    public static File[] list(String dir){
        File f = new File(Start.DIR + "\\.store\\" + dir);
        if(!f.exists()){
            f.mkdirs();
        }
        return f.listFiles();
    }

    public static void save(String dir, Object o){
        File f = new File(Start.DIR + "\\.store\\" + dir);
        if(!f.exists()){
            f.mkdirs();
        }
        Json.save(Start.DIR + "\\.store\\" + dir + "\\" + UUID.randomUUID() + dir.replace("/", ".").toLowerCase(), o);
    }

    public static boolean delete(String objex, String id){
        File[] fs = Pipe.list(objex);
        for(File f : fs){
            if(f.getName().endsWith(objex.toLowerCase().replaceAll("/", "."))){
                Objex o = Json.load(f.getPath(), Objex.class);
                if(o.getId().equals(id)){
                    f.delete();
                    return true;
                }
            }
        }
        return false;
    }

    public static void saveConfiguration() {
        File md = new File(Start.DIR);
        if(!md.exists()){
            md.mkdirs();
        }
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
            Json.save(Start.DIR + UUID.randomUUID() + ".cnl.mfg", Engine.getConfiguration());
        }
    }

    public static JsonObject get(String objex) {
        File[] d = Pipe.list(objex); //Excpects '/OBJEX/.../..'
        for(File f : d){
            if(!f.isDirectory()){
                return Json.load(f.getPath(), JsonObject.class);
            }
        }
        return null;
    }
}