package org.campusmolndal;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TodoManagerTest {

    @Mock
    private MongoCollection<Document> todoCollection;

    @Mock
    private MongoCollection<Document> userCollection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTodo() {
        TodoManager todoManager = new TodoManager(todoCollection, userCollection);
        Scanner scanner = new Scanner("Todo text\n1,2\n");

        List<Integer> userIds = new ArrayList<>();
        userIds.add(1);
        userIds.add(2);

        Document todoDocument = new Document("_id", 1)
                .append("text", "Todo text")
                .append("done", false)
                .append("assignedTo", userIds);

        when(todoCollection.insertOne(any(Document.class))).thenReturn(null);

        for (int userId : userIds) {
            Document userFilter = new Document("_id", userId);
            Document userUpdate = new Document("$push", new Document("todos", 1));
            when(userCollection.updateOne(eq(userFilter), eq(userUpdate))).thenReturn(null);
        }

        todoManager.createTodo(scanner);

        verify(todoCollection, times(1)).insertOne(eq(todoDocument));

        for (int userId : userIds) {
            Document userFilter = new Document("_id", userId);
            Document userUpdate = new Document("$push", new Document("todos", 1));
            verify(userCollection, times(1)).updateOne(eq(userFilter), eq(userUpdate));
        }
    }

    @Test
    void testReadOneTodo() {
        TodoManager todoManager = new TodoManager(todoCollection, userCollection);
        Scanner scanner = new Scanner("1\n");

        Document todoDocument = new Document("_id", 1)
                .append("text", "Todo text")
                .append("done", false)
                .append("assignedTo", List.of(1, 2));

        when(todoCollection.find(any(Document.class))).thenReturn(Mockito.mock(FindIterable.class));
        when(todoCollection.find(any(Document.class)).first()).thenReturn(todoDocument);

        todoManager.readOneTodo(scanner);

        verify(todoCollection, times(1)).find(any(Document.class));
        verify(todoCollection.find(any(Document.class)), times(1)).first();
    }


    @Test
    void testReadAllTodos() {
        TodoManager todoManager = new TodoManager(todoCollection, userCollection);

        List<Document> todoDocuments = new ArrayList<>();
        todoDocuments.add(new Document("_id", 1)
                .append("text", "Todo 1")
                .append("done", false)
                .append("assignedTo", List.of(1)));

        todoDocuments.add(new Document("_id", 2)
                .append("text", "Todo 2")
                .append("done", true)
                .append("assignedTo", List.of(2, 3)));

        when(todoCollection.find()).thenReturn((FindIterable<Document>) mock(MongoCollection.class));
        when(todoCollection.find().into(any())).thenReturn(todoDocuments);

        todoManager.readAllTodos();

        verify(todoCollection, times(1)).find();
        verify(todoCollection.find(), times(1)).into(any());
    }

    @Test
    void testUpdateTodo() {
        TodoManager todoManager = new TodoManager(todoCollection, userCollection);
        Scanner scanner = new Scanner("1\nUpdated Todo text\n");

        Document todoDocument = new Document("_id", 1)
                .append("text", "Updated Todo text");

        when(todoCollection.find(any(Document.class))).thenReturn((FindIterable<Document>) mock(MongoCollection.class));
        when(todoCollection.find(any(Document.class)).first()).thenReturn(todoDocument);

        todoManager.updateTodo(scanner);

        verify(todoCollection, times(1)).find(any(Document.class));
        verify(todoCollection.find(any(Document.class)), times(1)).first();

        Document updateDocument = new Document("$set", new Document("text", "Updated Todo text"));
        verify(todoCollection, times(1)).updateOne(any(Document.class), eq(updateDocument));
    }

    void testDeleteTodo() {
        TodoManager todoManager = new TodoManager(todoCollection, userCollection);
        Scanner scanner = new Scanner("1\n");

        Document todoFilter = new Document("_id", 1);
        Document userFilter = new Document("todos", 1);
        Document userUpdate = new Document("$pull", new Document("todos", 1));

        todoManager.deleteTodo(scanner);

        verify(todoCollection, times(1)).deleteOne(eq(todoFilter));
        verify(userCollection, times(1)).updateMany(eq(userFilter), eq(userUpdate));
    }
}
