package org.campusmolndal;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MongoDBFacadeTest {

    private MongoDBFacade mongoDBFacade;
    private MongoDatabase mockDatabase;
    private MongoCollection<Document> mockCollection;

    @BeforeEach
    public void setUp() {
        // Arrange
        mockDatabase = Mockito.mock(MongoDatabase.class);
        mockCollection = Mockito.mock(MongoCollection.class);

        when(mockDatabase.getCollection(ArgumentMatchers.anyString())).thenReturn(mockCollection);

        mongoDBFacade = new MongoDBFacade("testDatabase", "testCollection");
        mongoDBFacade.database = mockDatabase;
        mongoDBFacade.collection = mockCollection;
    }

    @Test
    public void create_shouldInsertDocument() {
        // Arrange
        Document document = new Document("key", "value");

        // Act
        mongoDBFacade.create(document);

        // Assert
        verify(mockCollection).insertOne(document);
    }

    @Test
    public void read_shouldFindDocument() {
        // Arrange
        String key = "key";
        Object value = "value";
        Document expectedDocument = new Document("key", "value");

        // Create a mock for the MongoCursor<Document>
        MongoCursor<Document> mockCursor = Mockito.mock(MongoCursor.class);

        // Set up the behavior of the mock objects
        when(mockCollection.find(ArgumentMatchers.<Document>any())).thenReturn((FindIterable<Document>) mockCursor);
        when(mockCursor.hasNext()).thenReturn(true);
        when(mockCursor.next()).thenReturn(expectedDocument);

        // Act
        Document result = mongoDBFacade.read(key, value);

        // Assert
        // Verify the expected interactions with the mock objects
        verify(mockCollection).find(new Document(key, value));
        verify(mockCursor).hasNext();
        verify(mockCursor).next();

        // Assert the result
        assertEquals(expectedDocument, result);
    }

    @Test
    public void update_shouldUpdateDocument() {
        // Arrange
        String key = "key";
        Object value = "value";
        Document updatedDocument = new Document("key", "updatedValue");

        // Act
        mongoDBFacade.update(key, value, updatedDocument);

        // Assert
        verify(mockCollection).updateOne(new Document(key, value), new Document("$set", updatedDocument));
    }

    @Test
    public void delete_shouldDeleteDocument() {
        // Arrange
        String key = "key";
        Object value = "value";

        // Act
        mongoDBFacade.delete(key, value);

        // Assert
        verify(mockCollection).deleteOne(new Document(key, value));
    }

    @Test
    public void close_shouldCloseMongoClient() {
        // Act
        mongoDBFacade.close();

        // Assert
        verify(Connection.mongoClient).close();
    }
}