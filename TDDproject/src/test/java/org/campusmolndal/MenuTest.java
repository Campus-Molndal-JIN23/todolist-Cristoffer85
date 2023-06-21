package org.campusmolndal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

class MenuTest {

    @Mock
    private static MongoDBFacade mongoDBFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Menu.mongoDBFacade = mongoDBFacade;
    }

    @AfterEach
    void tearDown() {
        reset(mongoDBFacade);
    }

    @Test
    void testShowMenu() {
        String input = "1\n1\nTask 1\nfalse\nJohn Doe\n5\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Menu.showMenu();

        verify(mongoDBFacade).create(1, "Task 1", false, "John Doe");
        verify(mongoDBFacade).close();
    }
}