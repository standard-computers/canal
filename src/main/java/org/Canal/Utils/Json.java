package org.Canal.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Json {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void save(String filename, Object object) {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T load(String filename, Class<T> clazz) {
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
