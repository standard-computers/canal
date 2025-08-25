package org.Canal.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import org.Canal.Models.Objex;
import org.Canal.Start;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static org.Canal.Utils.ConnectDB.collection;

public class Pipe {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static File[] list(String dir){
        File f = new File(Start.DIR + "\\.store\\" + dir);
        if(!f.exists()){
            f.mkdirs();
        }
        return f.listFiles();
    }

    private static Document toDocument(Object entity) {
        if (entity instanceof Document d) return d;
        return Document.parse(GSON.toJson(entity));
    }

    /** Normalize an _id value: String 24-hex → ObjectId, else keep as-is. */
    private static Object normalizeId(Object id) {
        if (id == null) return null;
        if (id instanceof ObjectId) return id;
        String s = id.toString();
        return s.matches("^[0-9a-fA-F]{24}$") ? new ObjectId(s) : s;
    }

    /**
     * Overwrite a document by its _id contained in the entity.
     * If upsert=true and no match exists, it will insert.
     *
     * The entity MUST contain an `_id` field.
     */
    public static UpdateResult overwriteById(String collectionName, Object entity, boolean upsert) {
        Document doc = toDocument(entity);
//        Object id = normalizeId(doc.get("id"));
//        if (id == null) {
//            throw new IllegalArgumentException("Entity must contain _id for overwriteById.");
//        }
        // Ensure stored doc has properly-typed _id
//        doc.put("id", id);

        Bson filter = Filters.eq("id", doc.get("id"));
        return collection(collectionName).replaceOne(filter, doc, new ReplaceOptions().upsert(upsert));
    }

    /**
     * Overwrite using a custom filter (e.g., match by a unique key other than _id).
     * If upsert=true and no match exists, it will insert the entity.
     */
    public static UpdateResult overwrite(String collectionName, Bson filter, Object entity, boolean upsert) {
        Document doc = toDocument(entity);
        return collection(collectionName).replaceOne(filter, doc, new ReplaceOptions().upsert(upsert));
    }
    
    public static void save(String dir, Object o){

        if(Engine.getConfiguration().getMongodb().isEmpty()){ //Save to local disk

            File f = new File(Start.DIR + "\\.store\\" + dir);
            if(!f.exists()){
                f.mkdirs();
            }
            Pipe.export(Start.DIR + "\\.store\\" + dir + "\\" + UUID.randomUUID() + dir.replace("/", ".").toLowerCase(), o);
        }else{

            String objex = dir.startsWith("/") ? dir.replaceFirst("/", "") : dir;
            overwriteById(objex, o, true);
        }
    }

    public static boolean delete(String objex, String id){
        File[] fs = Pipe.list(objex);
        for(File f : fs){
            if(f.getName().endsWith(objex.toLowerCase().replaceAll("/", "."))){
                Objex o = Pipe.load(f.getPath(), Objex.class);
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
                    Pipe.export(file.getPath(), Engine.getConfiguration());
                }
            }
        }
        if (!existed) {
            Pipe.export(Start.DIR + UUID.randomUUID() + ".cnl.mfg", Engine.getConfiguration());
        }
    }

    public static JsonObject get(String objex) {
        File[] d = Pipe.list(objex); //Excpects '/OBJEX/.../..'
        for(File f : d){
            if(!f.isDirectory()){
                return Pipe.load(f.getPath(), JsonObject.class);
            }
        }
        return null;
    }

    public static void export(String filename, Object object) {
        try (FileWriter writer = new FileWriter(filename)) {
            GSON.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T load(String filename, Class<T> clazz) {
        try (FileReader reader = new FileReader(filename)) {
            return GSON.fromJson(reader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> T load(Document document, Class<T> clazz) {
        return GSON.fromJson(document.toJson(), clazz);
    }
}