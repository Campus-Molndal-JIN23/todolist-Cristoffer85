package org.campusmolndal;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TodoManagerTest {

    @Mock
    private MongoCollection<Document> todoCollection;
    @Mock
    private MongoCollection<Document> userCollection;
    @Mock
    private FindIterable<Document> findIterable;

    private TodoManager todoManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todoManager = new TodoManager(todoCollection, userCollection);
    }

    @Test
    void createTodoTest() {
        Scanner scanner = new Scanner("Sample todo\n1,2,3\n");
        List<Integer> userIds = Arrays.asList(1, 2, 3);
        int todoId = 1;

        Document todoDocument = new Document("_id", todoId)
                .append("text", "Sample todo")
                .append("done", false)
                .append("assignedTo", userIds);

        when(todoCollection.insertOne(any(Document.class))).thenReturn(null);
        when(todoCollection.find()).thenReturn(findIterable);
        when(findIterable.sort(any())).thenReturn(findIterable);
        when(findIterable.limit(anyInt())).thenReturn(findIterable);
        when(userCollection.updateOne(any(Document.class), any(Document.class))).thenReturn(null);

        todoManager.createTodo(scanner);

        verify(todoCollection, times(1)).insertOne(eq(todoDocument));

        for (int userId : userIds) {
            Document userFilter = new Document("_id", userId);
            Document userUpdate = new Document("$push", new Document("todos", todoId));
            verify(userCollection, times(1)).updateOne(eq(userFilter), eq(userUpdate));
        }
    }

}