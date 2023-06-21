package org.campusmolndal;

import static org.mockito.Mockito.*;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MongoDBFacadeTest {
    private MongoDBFacade mongoDBFacade;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @BeforeEach
    void setUp() {
        //Arrange
        database = mock(MongoDatabase.class);
        collection = mock(MongoCollection.class);
        //Act
        when(database.getCollection(anyString())).thenReturn(collection);
        //Assert
        mongoDBFacade = new MongoDBFacade("testDatabase", "testCollection");
    }

    @AfterEach
    void tearDown() {
        mongoDBFacade.close();
    }

    @Test
    void createShouldInsertOneDocument() {
        //Arrange
        Document document = new Document();
        when(collection.insertOne(any(Document.class))).thenReturn(null);
        //Act
        mongoDBFacade.create(1, "Test", true, "John Doe");
        //Assert
        verify(collection).insertOne(document);
    }

    @Test
    void readShouldFindOneDocument() {
        //Arrange
        Document document = new Document();
        FindIterable<Document> iterable = mock(FindIterable.class);

        when(collection.find(any(Document.class))).thenReturn(iterable);
        when(iterable.first()).thenReturn(document);
        //Act
        mongoDBFacade.read("1");
        //Assert
        verify(collection).find(new Document("id", "1"));
        verify(iterable).first();
    }

    @Test
    void updateShouldUpdateOneDocument() {
        //Arrange
        Document updatedDocument = new Document();
        when(collection.updateOne(any(Document.class), any(Document.class))).thenReturn(null);
        //Act
        mongoDBFacade.update("1", updatedDocument);
        //Assert
        verify(collection).updateOne(new Document("id", "1"), new Document("$set", updatedDocument));
    }

    @Test
    void deleteShouldDeleteOneDocument() {
        //Arrange
        when(collection.deleteOne(any(Document.class))).thenReturn(null);
        //Act
        mongoDBFacade.delete("1");
        //Assert
        verify(collection).deleteOne(new Document("id", "1"));
    }
}