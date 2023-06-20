package org.campusmolndal;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBFacade {
    Connection connection = new Connection();   //One instantiation of the Connection class
    MongoDatabase database;
    MongoCollection<Document> collection;

    public MongoDBFacade(String databaseName, String collectionName) {
        database = Connection.mongoClient.getDatabase(databaseName);
        collection = database.getCollection(collectionName);
    }

    public void create(Document document) {
        collection.insertOne(document);
    }

    public Document read(String key, Object value) {
        return collection.find(new Document(key, value)).first();
    }

    public void update(String key, Object value, Document updatedDocument) {
        collection.updateOne(new Document(key, value), new Document("$set", updatedDocument));
    }

    public void delete(String key, Object value) {
        collection.deleteOne(new Document(key, value));
    }

    public void close() {
        Connection.mongoClient.close();
    }
}
