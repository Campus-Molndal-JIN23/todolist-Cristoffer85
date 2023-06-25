package org.campusmolndal;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileInputStream;
import java.util.Properties;

public class Connection {
    static MongoClient mongoClient;

    Connection() {
        //-------------------- Server Connection --------------------
        try {
            Properties props = loadProperties();
            String connectionString = props.getProperty("connectionString");
            mongoClient = MongoClients.create(connectionString);
            MongoDatabase db = mongoClient.getDatabase("admin");
            db.runCommand(new Document("ping", 1));
            System.out.println("  ******Connected to external MongoDBServer******");
        } catch (Exception e) {
            System.out.println("Sorry unable to connect: " + e.getMessage());
            System.out.println("Trying to connect locally instead...");

            //-------------------- Local Connection --------------------
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            System.out.println("  ******Connected to local MongoDBServer******");
        } catch (Exception ex) {
            System.out.println("Sorry unable to connect locally as well: " + ex.getMessage());
            }
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try {
            FileInputStream input = new FileInputStream("TDDproject/src/mongodb.properties");
            props.load(input);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return props;
    }
}