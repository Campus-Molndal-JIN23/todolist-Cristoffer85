package org.campusmolndal;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBFacade {
    private Connection connection; // Instantiate the Connection class
    private MongoDatabase database;
    private static MongoCollection<Document> collection;

    public MongoDBFacade(String databaseName, String collectionName) {
        connection = new Connection();
        database = connection.mongoClient.getDatabase(databaseName);
        collection = database.getCollection(collectionName);
    }

    public static void create(int id, String text, boolean done, String assignedTo) {
        Document document = new Document("id", id)
                .append("text", text)
                .append("done", done)
                .append("assignedTo", assignedTo);

        collection.insertOne(document);
    }

    public Document read(String id) {
        return collection.find(new Document("id", id)).first();
    }

    public void update(String id, Document updatedDocument) {
        collection.updateOne(new Document("id", id), new Document("$set", updatedDocument));
    }

    public void delete(String id) {
        collection.deleteOne(new Document("id", id));
    }

    public void close() {
        connection.mongoClient.close();
    }
}
