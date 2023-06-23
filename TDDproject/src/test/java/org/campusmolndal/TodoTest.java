package org.campusmolndal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoTest {
    @Mock
    private User mockedUser;

    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.todo = new Todo(1, "Sample Todo", false);
    }

    @Test
    void testAssignToUser() {
        // Arrange

        // Act
        todo.assignToUser(mockedUser);

        // Assert
        verify(mockedUser, times(1)).addTodoToUser(todo);
    }

    @Test
    void testGetId() {
        // Arrange

        // Act
        int id = todo.getId();

        // Assert
        assertEquals(1, id);
    }

    @Test
    void testSetId() {
        // Arrange

        // Act
        todo.setId(2);

        // Assert
        assertEquals(2, todo.getId());
    }

    @Test
    void testGetText() {
        // Arrange

        // Act
        String text = todo.getText();

        // Assert
        assertEquals("Sample Todo", text);
    }

    @Test
    void testSetText() {
        // Arrange

        // Act
        todo.setText("Updated Todo");

        // Assert
        assertEquals("Updated Todo", todo.getText());
    }

    @Test
    void testIsDone() {
        // Arrange

        // Act
        boolean isDone = todo.isDone();

        // Assert
        assertFalse(isDone);
    }

    @Test
    void testSetDone() {
        // Arrange

        // Act
        todo.setDone(true);

        // Assert
        assertTrue(todo.isDone());
    }
}
