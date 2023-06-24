package org.campusmolndal;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class TodoApplicationTest {
    @Mock
    private TodoManager todoManager;
    @Mock
    private UserManager userManager;
    @Mock
    private Scanner scanner;
    @Mock
    private MongoCollection<Document> todoCollection;
    @Mock
    private MongoCollection<Document> userCollection;
    @Mock
    private MongoClient mongoClient;
    @Mock
    private MongoDatabase mongoDatabase;

    private TodoApplication todoApplication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(scanner.nextInt()).thenReturn(1).thenReturn(0); // Simulate user input: 1 and then 0
        when(scanner.nextLine()).thenReturn(""); // Simulate user pressing enter

        when(mongoClient.getDatabase("TODOAPP")).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection("Todos")).thenReturn(todoCollection);
        when(mongoDatabase.getCollection("Users")).thenReturn(userCollection);

        todoApplication = new TodoApplication();
        todoApplication.todoManager = todoManager;
        todoApplication.userManager = userManager;
        todoApplication.scanner = scanner;

        try {
            Field connectionField = TodoApplication.class.getDeclaredField("connection");
            connectionField.setAccessible(true);

            Connection connection = mock(Connection.class);
            connection.mongoClient = mongoClient;
            connectionField.set(todoApplication, connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void runTest() {
        todoApplication.run();

        // Verify that the corresponding methods were called
        verify(userManager).createUser(scanner);
    }
}
