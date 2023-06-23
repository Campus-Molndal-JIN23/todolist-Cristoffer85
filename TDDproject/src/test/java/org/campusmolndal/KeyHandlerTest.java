package org.campusmolndal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doNothing;

class KeyHandlerTest {

    @Mock
    private Properties propertiesMock;

    private KeyHandler keyHandler;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        keyHandler = spy(new KeyHandler("test"));
        doReturn(propertiesMock).when(keyHandler).createProperties();
        doReturn("mockedPassword").when(propertiesMock).getProperty("Pass");
        setFileInputStream(keyHandler, createMockFileInputStream());
    }

    @Test
    void testGetPasscode() {
        String passcode = keyHandler.getPasscode();
        assertEquals("mockedPassword", passcode);
    }

    private FileInputStream createMockFileInputStream() throws IOException {
        FileInputStream fileInputStreamMock = mock(FileInputStream.class);
        doNothing().when(fileInputStreamMock).close();
        return fileInputStreamMock;
    }

    private void setFileInputStream(KeyHandler keyHandler, FileInputStream fileInputStream) throws Exception {
        Field propsField = KeyHandler.class.getDeclaredField("props");
        propsField.setAccessible(true);
        propsField.set(keyHandler, null);

        Field fileInputStreamField = KeyHandler.class.getDeclaredField("input");
        fileInputStreamField.setAccessible(true);
        fileInputStreamField.set(keyHandler, fileInputStream);

        propsField.set(keyHandler, propertiesMock);
    }
}
