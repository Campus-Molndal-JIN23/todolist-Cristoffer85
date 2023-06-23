package org.campusmolndal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KeyHandlerTest {

    @Mock
    private Properties mockProperties;

    @Mock
    private FileInputStreamFactory mockFileInputStreamFactory;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPasscode() throws Exception {
        String file = "testfile";
        String password = "mockedPassword";

        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read(any())).thenReturn(-1);
        doAnswer(invocation -> {
            InputStream inputStream = invocation.getArgument(0);
            inputStream.close();
            return null;
        }).when(mockInputStream).close();

        when(mockFileInputStreamFactory.createFileInputStream(anyString())).thenReturn(mockInputStream);
        when(mockProperties.getProperty("Pass")).thenReturn(password);

        KeyHandler keyHandler = new KeyHandler(file, mockProperties, mockFileInputStreamFactory);
        assertEquals(password, keyHandler.getPasscode());

        verify(mockFileInputStreamFactory).createFileInputStream(System.getProperty("user.home") + "/Documents/Pass/" + file + ".txt");
        verify(mockInputStream).close();
    }

    interface FileInputStreamFactory {
        InputStream createFileInputStream(String filePath) throws Exception;
    }
}
