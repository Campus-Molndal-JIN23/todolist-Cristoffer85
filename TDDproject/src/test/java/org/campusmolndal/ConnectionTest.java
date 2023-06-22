package org.campusmolndal;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {
    private static MongoClient mongoClient;

    @BeforeAll
    static void setUp() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        // Set up the test MongoDB server or use an existing one
    }

    @AfterAll
    static void tearDown() {
        mongoClient.close();
        // Clean up resources after the tests are finished
    }

    @Test
    void testConnection_ExternalServer() {
        // Arrange
        // Set up the test to simulate connecting to an external MongoDB server

        // Act
        Connection connection = new Connection();

        // Assert
        assertNotNull(connection);
        // Add additional assertions to verify the connection behavior
    }

    @Test
    void testConnection_LocalServer() {
        // Arrange
        // Set up the test to simulate connecting to a local MongoDB server

        // Act
        Connection connection = new Connection();

        // Assert
        assertNotNull(connection);
        // Add additional assertions to verify the connection behavior
    }
}
