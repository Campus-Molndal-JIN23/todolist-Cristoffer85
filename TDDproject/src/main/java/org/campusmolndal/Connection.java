package org.campusmolndal;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class Connection {
    KeyHandler keyhandler = new KeyHandler("Pass");     //One instantiation of the KeyHandler class
    static MongoClient mongoClient;

    public Connection() {
        try {               // Server Connection
            mongoClient = MongoClients.create("mongodb+srv://cristofferostberg85:" + keyhandler.getPasscode() + "@cluster0.imetavy.mongodb.net/?retryWrites=true&w=majority");
            System.out.println("Connected to external MongoDBServer.");
        } catch (MongoException e) {
            System.out.println("Sorry unable to connect: " + e.getMessage());
            System.out.println("Trying to connect locally instead...");

        try {               // Local Connection
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            System.out.println("Connected to local MongoDBServer.");
        } catch (MongoException ex) {
            System.out.println("Sorry unable to connect locally as well: " + ex.getMessage());
            }
        }
    }
}
