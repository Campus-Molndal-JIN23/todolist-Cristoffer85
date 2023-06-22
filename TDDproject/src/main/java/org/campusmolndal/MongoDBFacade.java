package org.campusmolndal;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MongoDBFacade {
    Connection connection;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDBFacade(String databaseName, String collectionName) {
        connection = new Connection();
        database = connection.mongoClient.getDatabase(databaseName);
        collection = database.getCollection(collectionName);
    }

    public void create(int id, String text, boolean done, String assignedTo) {
        Document document = new Document("id", id)
                .append("text", text)
                .append("done", done)
                .append("assignedTo", assignedTo);

        collection.insertOne(document);
    }

    public List<Document> read(int id) {
        List<Document> todos = new ArrayList<>();

        if (id != -1) {
            Document todo = collection.find(new Document("id", id)).first();
            if (todo != null) {
                todos.add(todo);
            }
        } else {
            collection.find().into(todos);
        }

        return todos;
    }



    public void update(int id, Document updatedDocument) {
        collection.updateOne(new Document("id", id), new Document("$set", updatedDocument));
    }

    public void delete(int id) {
        collection.deleteOne(new Document("id", id));
    }

    public void close() {
        connection.mongoClient.close();
    }
}