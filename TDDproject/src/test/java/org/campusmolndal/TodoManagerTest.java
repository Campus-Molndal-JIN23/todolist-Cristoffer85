package org.campusmolndal;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todoManager = new TodoManager(todoCollection, userCollection);
        System.setOut(new PrintStream(outputStream));
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

    @Test
    void readOneTodoTest() {
        // Prepare test data
        int todoId = 1;
        Document todoDocument = new Document("_id", todoId)
                .append("text", "Sample todo")
                .append("done", false);

        // Configure the input scanner with the desired input
        InputStream sysInBackup = System.in; // Backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
        System.setIn(in);

        // Mock the MongoDB behavior
        when(todoCollection.find(any(Document.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(todoDocument);

        // Call the method under test
        todoManager.readOneTodo(new Scanner(System.in));

        // Verify the output
        String expectedOutput = "-------------------" + System.lineSeparator();
        expectedOutput += "Enter the Todo ID: ";
        expectedOutput += "------------------------------" + System.lineSeparator();
        expectedOutput += "Todo ID: 1" + System.lineSeparator();
        expectedOutput += "Text: Sample todo" + System.lineSeparator();
        expectedOutput += "Done: false" + System.lineSeparator();
        expectedOutput += "Assigned To: No assigned users" + System.lineSeparator();

        assertEquals(expectedOutput, outputStream.toString());

        // Restore System.in
        System.setIn(sysInBackup);
    }

    @Test
    void readAllTodosTest() {
        // Prepare test data
        List<Document> todoDocuments = Arrays.asList(
                new Document("_id", 1)
                        .append("text", "Sample todo 1")
                        .append("done", false),
                new Document("_id", 2)
                        .append("text", "Sample todo 2")
                        .append("done", true)
        );

        // Mock the MongoDB behavior
        when(todoCollection.find()).thenReturn(findIterable);
        when(findIterable.into(any())).thenReturn(todoDocuments);

        // Call the method under test
        todoManager.readAllTodos();

        // Verify the output
        String expectedOutput = "------------------------------" + System.lineSeparator();
        expectedOutput += "All Todos:" + System.lineSeparator();
        expectedOutput += "------------------------------" + System.lineSeparator();
        expectedOutput += "Todo ID: 1" + System.lineSeparator();
        expectedOutput += "Text: Sample todo 1" + System.lineSeparator();
        expectedOutput += "Done: false" + System.lineSeparator();
        expectedOutput += "Assigned To: No assigned users" + System.lineSeparator();
        expectedOutput += System.lineSeparator();
        expectedOutput += "------------------------------" + System.lineSeparator();
        expectedOutput += "Todo ID: 2" + System.lineSeparator();
        expectedOutput += "Text: Sample todo 2" + System.lineSeparator();
        expectedOutput += "Done: true" + System.lineSeparator();
        expectedOutput += "Assigned To: No assigned users" + System.lineSeparator();
        expectedOutput += System.lineSeparator();

        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void updateTodoTest() {
        // Prepare test data
        int todoId = 1;
        Document todoDocument = new Document("_id", todoId)
                .append("text", "Sample todo")
                .append("done", false);

        // Configure the input scanner with the desired input
        InputStream sysInBackup = System.in; // Backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream((
                todoId + System.lineSeparator() +
                        "Updated text" + System.lineSeparator() +
                        "true" + System.lineSeparator()
        ).getBytes());
        System.setIn(in);

        // Mock the MongoDB behavior
        when(todoCollection.find(any(Document.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(todoDocument);
        when(todoCollection.updateOne(any(Document.class), any(Document.class))).thenReturn(null);

        // Call the method under test
        todoManager.updateTodo(new Scanner(System.in));

        // Verify the output
        String expectedOutput = "------------------" + System.lineSeparator();
        expectedOutput += "Enter the ID of the Todo: " +
                "Enter the updated text of the Todo: " +
                "Enter the updated status of the Todo (true/false): " +
                "Todo updated successfully." + System.lineSeparator();

        assertEquals(expectedOutput, outputStream.toString());

        // Verify the update document passed to the todoCollection.updateOne() method
        Document expectedUpdateDocument = new Document("$set", new Document("text", "Updated text").append("done", true));
        verify(todoCollection, times(1)).updateOne(eq(new Document("_id", todoId)), eq(expectedUpdateDocument));

        // Restore System.in
        System.setIn(sysInBackup);
    }

    @Test
    void deleteTodoTest() {
        // Prepare test data
        int todoId = 1;

        // Configure the input scanner with the desired input
        InputStream sysInBackup = System.in; // Backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream((todoId + System.lineSeparator()).getBytes());
        System.setIn(in);

        // Mock the MongoDB behavior
        when(todoCollection.deleteOne(any(Document.class))).thenReturn(null);
        when(userCollection.updateMany(any(Document.class), any(Document.class))).thenReturn(null);

        // Call the method under test
        todoManager.deleteTodo(new Scanner(System.in));

        // Verify the output
        String expectedOutput = "-------------------" + System.lineSeparator();
        expectedOutput += "Enter the ID of the Todo: " +
                "Todo deleted successfully." + System.lineSeparator();

        assertEquals(expectedOutput, outputStream.toString());

        // Verify the deleteOne and updateMany methods were called with the correct filters
        verify(todoCollection, times(1)).deleteOne(eq(new Document("_id", todoId)));
        verify(userCollection, times(1)).updateMany(eq(new Document("todos", todoId)),
                eq(new Document("$pull", new Document("todos", todoId))));

        // Restore System.in
        System.setIn(sysInBackup);
    }
}
