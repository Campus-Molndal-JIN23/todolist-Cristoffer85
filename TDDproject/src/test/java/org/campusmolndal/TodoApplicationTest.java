package org.campusmolndal;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Scanner;

import static org.mockito.Mockito.*;

public class TodoApplicationTest {

    private TodoApplication todoApplication;

    @Mock
    private Connection mockConnection;

    @Mock
    private MongoCollection<Document> mockTodoCollection;

    @Mock
    private MongoCollection<Document> mockUserCollection;

    @Mock
    private Scanner mockScanner;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(mockConnection.mongoClient.getDatabase("TODOAPP").getCollection("Todos")).thenReturn(mockTodoCollection);
        when(mockConnection.mongoClient.getDatabase("TODOAPP").getCollection("Users")).thenReturn(mockUserCollection);

        todoApplication = new TodoApplication();
        todoApplication.connection = mockConnection;
        todoApplication.todoCollection = mockTodoCollection;
        todoApplication.userCollection = mockUserCollection;
        todoApplication.scanner = mockScanner;
    }

    @Test
    public void run_shouldExitWhenChoiceIsZero() {
        // Arrange
        when(mockScanner.nextInt()).thenReturn(0);
        when(mockScanner.nextLine()).thenReturn("");

        // Act
        todoApplication.run();

        // Assert
        verify(mockScanner, times(1)).close();
    }
}
