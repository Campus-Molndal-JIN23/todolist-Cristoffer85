package org.campusmolndal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Scanner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TodoApplicationTest {
    @Mock
    private TodoManager todoManager;
    @Mock
    private UserManager userManager;
    @Mock
    private Scanner scanner;

    private TodoApplication todoApplication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todoApplication = new TodoApplication();
    }

    @Test
    void testRun() {
        when(scanner.nextInt())
                .thenReturn(0); // Simulate user entering 0 to exit the application

        Thread thread = new Thread(() -> {
            todoApplication.run();
        });
        thread.start();

        // Wait for the application to start and enter the loop
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interrupt the thread to stop the loop
        thread.interrupt();

        // Wait for the thread to finish
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(scanner, times(1)).nextInt();
    }
}
