package org.campusmolndal;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MongoDBFacadeTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private MongoDatabase mockDatabase;

    @Mock
    private MongoCollection<Document> mockCollection;

    private MongoDBFacade mongoDBFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.getDatabase(anyString())).thenReturn(mockDatabase);
        when(mockDatabase.getCollection(anyString())).thenReturn(mockCollection);
        mongoDBFacade = new MongoDBFacade("testDB", "testCollection");
    }

    @AfterEach
    void tearDown() {
        mongoDBFacade.close();
    }

    @Test
    void create_shouldInsertDocumentIntoCollection() {
        // Arrange
        int id = 1;
        String text = "Sample text";
        boolean done = false;
        String assignedTo = "John Doe";
        Document expectedDocument = new Document("id", id)
                .append("text", text)
                .append("done", done)
                .append("assignedTo", assignedTo);

        // Act
        MongoDBFacade.create(id, text, done, assignedTo);

        // Assert
        verify(mockCollection, times(1)).insertOne(expectedDocument);
    }

    @Test
    void read_shouldReturnDocumentFromCollection() {
        // Arrange
        String id = "1";
        Document expectedDocument = new Document("id", id);

        when(mockCollection.find(any(Document.class))).thenReturn(new DocumentCursorStub(expectedDocument));

        // Act
        Document result = mongoDBFacade.read(id);

        // Assert
        assertEquals(expectedDocument, result);
    }

    @Test
    void update_shouldUpdateDocumentInCollection() {
        // Arrange
        String id = "1";
        Document updatedDocument = new Document("updatedField", "updatedValue");

        // Act
        mongoDBFacade.update(id, updatedDocument);

        // Assert
        verify(mockCollection, times(1)).updateOne(eq(new Document("id", id)), eq(new Document("$set", updatedDocument)));
    }

    @Test
    void delete_shouldDeleteDocumentFromCollection() {
        // Arrange
        String id = "1";

        // Act
        mongoDBFacade.delete(id);

        // Assert
        verify(mockCollection, times(1)).deleteOne(new Document("id", id));
    }

    // Helper class to stub DocumentCursor for testing
    private static class DocumentCursorStub implements Iterable<Document> {
        private final Document document;

        DocumentCursorStub(Document document) {
            this.document = document;
        }

        @Override
        public java.util.Iterator<Document> iterator() {
            return new DocumentIteratorStub(document);
        }
    }

    // Helper class to stub DocumentIterator for testing
    private static class DocumentIteratorStub implements java.util.Iterator<Document> {
        private final Document document;
        private boolean hasNext = true;

        DocumentIteratorStub(Document document) {
            this.document = document;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public Document next() {
            hasNext = false;
            return document;
        }
    }
}
