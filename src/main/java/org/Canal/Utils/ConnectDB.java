package org.Canal.Utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.bson.codecs.configuration.CodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.*;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * Static MongoDB connector.
 * Config via env or -D system properties:
 *   MONGODB_URI (default: mongodb://localhost:27017)
 *   MONGODB_DB  (default: canal)
 */
public final class ConnectDB {

    private static final String DEFAULT_URI = Engine.getConfiguration().getMongodb();
    private static final String DEFAULT_DB  = "canal";

    private static final MongoClient CLIENT;
    private static final MongoDatabase DATABASE;

    static {
        // Read config from system prop first, then env, then default
        String uri = System.getProperty("MONGODB_URI");
        if (uri == null || uri.isBlank()) uri = System.getenv("MONGODB_URI");
        if (uri == null || uri.isBlank()) uri = DEFAULT_URI;

        String dbName = System.getProperty("MONGODB_DB");
        if (dbName == null || dbName.isBlank()) dbName = System.getenv("MONGODB_DB");
        if (dbName == null || dbName.isBlank()) dbName = DEFAULT_DB;

        // Optional: enable POJO mapping automatically
        CodecRegistry pojoRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .codecRegistry(pojoRegistry)
                .build();

        CLIENT = MongoClients.create(settings);
        DATABASE = CLIENT.getDatabase(dbName);

        // Close cleanly when the JVM exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { CLIENT.close(); } catch (Exception ignored) {}
        }));
    }

    private ConnectDB() {} // no instances

    /** Get the shared MongoClient. */
    public static MongoClient client() {
        return CLIENT;
    }

    /** Get the shared MongoDatabase. */
    public static MongoDatabase db() {
        return DATABASE;
    }

    /** Get a collection as Documents. */
    public static MongoCollection<Document> collection(String name) {
        return DATABASE.getCollection(name);
    }

    /** Get a collection mapped to a POJO class. */
    public static <T> MongoCollection<T> collection(String name, Class<T> clazz) {
        return DATABASE.getCollection(name, clazz);
    }

    /** Fast health check (throws on failure). */
    public static void ping() {
        DATABASE.runCommand(new Document("ping", 1));
    }
}
