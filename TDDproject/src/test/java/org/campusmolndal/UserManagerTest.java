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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userManager = new UserManager(userCollection, todoCollection);
        System.setOut(new PrintStream(outputStream));
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

    @Test
    void readUserTest() {
        // Prepare input stream with user ID
        String input = "1" + System.lineSeparator();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Prepare expected output
        String lineSeparator = System.lineSeparator();
        String expectedOutput = "-------------------" + lineSeparator +
                "Enter the ID of the User: " +
                "User ID: 1" + lineSeparator +
                "Name: John" + lineSeparator +
                "Age: 25" + lineSeparator +
                "##############################" + lineSeparator +
                "Todos:" + lineSeparator +
                "##############################" + lineSeparator +
                "No Todos assigned to this user." + lineSeparator;

        // Prepare mocked data
        Document userDocument = new Document("_id", 1)
                .append("name", "John")
                .append("age", 25)
                .append("todos", new ArrayList<Integer>());

        when(userCollection.find(any(Document.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(userDocument);

        // Call the method
        userManager.readUser(new Scanner(System.in));

        // Verify the output
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void updateUserTest() {
        // Prepare input stream with user ID and updated name
        String input = "1" + System.lineSeparator() + "John Doe" + System.lineSeparator();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Prepare expected output
        String lineSeparator = System.lineSeparator();
        String expectedOutput = "-------------------" + lineSeparator +
                "Enter the ID of the User: " +
                "Enter the updated name of the User: " +
                "User updated successfully." + lineSeparator;

        // Prepare mocked data
        Document userDocument = new Document("_id", 1)
                .append("name", "John")
                .append("age", 25)
                .append("todos", new ArrayList<Integer>());

        when(userCollection.find(any(Document.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(userDocument);

        // Call the method
        userManager.updateUser(new Scanner(System.in));

        // Verify the output
        assertEquals(expectedOutput, outputStream.toString());

        // Verify the updated document
        Document expectedUpdateDocument = new Document("$set", new Document("name", "John Doe"));
        verify(userCollection, times(1)).updateOne(eq(new Document("_id", 1)), eq(expectedUpdateDocument));
    }

    @Test
    void deleteUserTest() {
        // Prepare input stream with user ID
        String input = "1" + System.lineSeparator();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Prepare expected output
        String lineSeparator = System.lineSeparator();
        String expectedOutput = "-------------------" + lineSeparator +
                "Enter the ID of the User: " +
                "User deleted successfully." + lineSeparator;

        // Call the method
        userManager.deleteUser(new Scanner(System.in));

        // Verify the output
        assertEquals(expectedOutput, outputStream.toString());

        // Verify the delete operation
        verify(userCollection, times(1)).deleteOne(eq(new Document("_id", 1)));
    }
}