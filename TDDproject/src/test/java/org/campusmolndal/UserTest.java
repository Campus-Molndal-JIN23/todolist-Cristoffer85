package org.campusmolndal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {
    @Mock
    private Todo mockedTodo;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1, "John Doe", 25);
    }

    @Test
    void testAddTodoToUser() {
        // Arrange

        // Act
        user.addTodoToUser(mockedTodo);

        // Assert
        List<Todo> todos = user.getTodos();
        assertEquals(1, todos.size());
        assertTrue(todos.contains(mockedTodo));
    }

    @Test
    void testGetId() {
        // Arrange

        // Act
        int id = user.getId();

        // Assert
        assertEquals(1, id);
    }

    @Test
    void testSetId() {
        // Arrange

        // Act
        user.setId(2);

        // Assert
        assertEquals(2, user.getId());
    }

    @Test
    void testGetName() {
        // Arrange

        // Act
        String name = user.getName();

        // Assert
        assertEquals("John Doe", name);
    }

    @Test
    void testSetName() {
        // Arrange

        // Act
        user.setName("Jane Smith");

        // Assert
        assertEquals("Jane Smith", user.getName());
    }

    @Test
    void testGetAge() {
        // Arrange

        // Act
        int age = user.getAge();

        // Assert
        assertEquals(25, age);
    }

    @Test
    void testSetAge() {
        // Arrange

        // Act
        user.setAge(30);

        // Assert
        assertEquals(30, user.getAge());
    }
}
