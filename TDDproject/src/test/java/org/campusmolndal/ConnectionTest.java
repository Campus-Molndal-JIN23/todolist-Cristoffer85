package org.campusmolndal;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//Only tried the local connection in this just today, since i didnt know how implement the test for External connection

class ConnectionTest {
    private static MongoClient mongoClient;

    @BeforeEach
    void setUp() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
    }

    @AfterEach
    void tearDown() {
        mongoClient.close();
    }

    @Test
    void testConnection_LocalServer() {
        // Arrange
        // = is set in the @BeforeEach

        // Act
        Connection connection = new Connection();

        // Assert
        assertNotNull(connection);
    }
}
