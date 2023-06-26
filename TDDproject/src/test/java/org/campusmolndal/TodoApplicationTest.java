package org.campusmolndal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TodoApplicationTest {
    @Mock
    TodoManager todoManager;

    @Mock
    UserManager userManager;

    @Mock
    Scanner scanner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void run_shouldInvokeUserManagerCreateUser_whenChoiceIsOne() {
        // Arrange
        TodoApplication todoApplication = createTodoApplicationWithMockedDependencies();

        // Simulate user input: 1 (for choice 1), 0 (for exit)
        when(scanner.nextLine())
                .thenReturn("1")
                .thenReturn("0");

        // Act
        todoApplication.run();

        // Assert
        verify(userManager).createUser(scanner);
        verify(todoApplication, times(2)).displayMenu();
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void run_shouldInvokeUserManagerCreateUser_whenChoiceIsTwo() {
        // Arrange
        TodoApplication todoApplication = createTodoApplicationWithMockedDependencies();

        // Simulate user input: 1 (for choice 1), 0 (for exit)
        when(scanner.nextLine())
                .thenReturn("2")
                .thenReturn("0");

        // Act
        todoApplication.run();

        // Assert
        verify(userManager).readUser(scanner);
        verify(todoApplication, times(2)).displayMenu();
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void run_shouldInvokeUserManagerCreateUser_whenChoiceIsThree() {
        // Arrange
        TodoApplication todoApplication = createTodoApplicationWithMockedDependencies();

        // Simulate user input: 1 (for choice 1), 0 (for exit)
        when(scanner.nextLine())
                .thenReturn("3")
                .thenReturn("0");

        // Act
        todoApplication.run();

        // Assert
        verify(userManager).updateUser(scanner);
        verify(todoApplication, times(2)).displayMenu();
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void run_shouldInvokeUserManagerCreateUser_whenChoiceIsFour() {
        // Arrange
        TodoApplication todoApplication = createTodoApplicationWithMockedDependencies();

        // Simulate user input: 1 (for choice 1), 0 (for exit)
        when(scanner.nextLine())
                .thenReturn("4")
                .thenReturn("0");

        // Act
        todoApplication.run();

        // Assert
        verify(userManager).deleteUser(scanner);
        verify(todoApplication, times(2)).displayMenu();
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void run_shouldInvokeUserManagerCreateUser_whenChoiceIsFive() {
        // Arrange
        TodoApplication todoApplication = createTodoApplicationWithMockedDependencies();

        // Simulate user input: 1 (for choice 1), 0 (for exit)
        when(scanner.nextLine())
                .thenReturn("5")
                .thenReturn("0");

        // Act
        todoApplication.run();

        // Assert
        verify(todoManager).createTodo(scanner);
        verify(todoApplication, times(2)).displayMenu();
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void run_shouldInvokeUserManagerCreateUser_whenChoiceIsSix() {
        // Arrange
        TodoApplication todoApplication = createTodoApplicationWithMockedDependencies();

        // Simulate user input: 1 (for choice 1), 0 (for exit)
        when(scanner.nextLine())
                .thenReturn("6")
                .thenReturn("0");

        // Act
        todoApplication.run();

        // Assert
        verify(todoManager).readOneTodo(scanner);
        verify(todoApplication, times(2)).displayMenu();
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void run_shouldInvokeUserManagerCreateUser_whenChoiceIsSeven() {
        // Arrange
        TodoApplication todoApplication = createTodoApplicationWithMockedDependencies();

        // Simulate user input: 1 (for choice 1), 0 (for exit)
        when(scanner.nextLine())
                .thenReturn("7")
                .thenReturn("0");

        // Act
        todoApplication.run();

        // Assert
        verify(todoManager).readAllTodos();
        verify(todoApplication, times(2)).displayMenu();
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void run_shouldInvokeUserManagerCreateUser_whenChoiceIsEight() {
        // Arrange
        TodoApplication todoApplication = createTodoApplicationWithMockedDependencies();

        // Simulate user input: 1 (for choice 1), 0 (for exit)
        when(scanner.nextLine())
                .thenReturn("8")
                .thenReturn("0");

        // Act
        todoApplication.run();

        // Assert
        verify(todoManager).updateTodo(scanner);
        verify(todoApplication, times(2)).displayMenu();
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void run_shouldInvokeUserManagerCreateUser_whenChoiceIsNine() {
        // Arrange
        TodoApplication todoApplication = createTodoApplicationWithMockedDependencies();

        // Simulate user input: 1 (for choice 1), 0 (for exit)
        when(scanner.nextLine())
                .thenReturn("9")
                .thenReturn("0");

        // Act
        todoApplication.run();

        // Assert
        verify(todoManager).deleteTodo(scanner);
        verify(todoApplication, times(2)).displayMenu();
        verify(scanner, times(2)).nextLine();
    }

    private TodoApplication createTodoApplicationWithMockedDependencies() {
        TodoApplication todoApplication = Mockito.spy(new TodoApplication());

        todoApplication.todoManager = todoManager;
        todoApplication.userManager = userManager;
        todoApplication.scanner = scanner;

        return todoApplication;
    }
}
