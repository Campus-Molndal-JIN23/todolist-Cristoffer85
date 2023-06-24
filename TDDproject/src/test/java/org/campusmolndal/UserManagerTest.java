package org.campusmolndal;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserManagerTest {

    @Mock
    private MongoCollection<Document> userCollection;
    @Mock
    private MongoCollection<Document> todoCollection;
    @Mock
    private FindIterable<Document> findIterable;

    private UserManager userManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userManager = new UserManager(userCollection, todoCollection);
    }

    @Test
    void createUserTest() {
        Scanner scanner = new Scanner("John\n25\n");
        List<Integer> todoList = new ArrayList<>();
        Document userDocument = new Document("_id", 1)
                .append("name", "John")
                .append("age", 25)
                .append("todos", todoList);

        when(userCollection.insertOne(any(Document.class))).thenReturn(null);
        when(userCollection.find(any(Document.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);
        when(userCollection.find()).thenReturn(findIterable);
        when(findIterable.sort(any())).thenReturn(findIterable);
        when(findIterable.limit(anyInt())).thenReturn(findIterable);

        userManager.createUser(scanner);

        verify(userCollection, times(1)).insertOne(eq(userDocument));
    }
}