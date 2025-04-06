package input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InputHandlerTest {
    @Mock
    private InputProvider inputProvider;

    @Mock
    private InputValidator inputValidator;

    @InjectMocks
    private InputHandler inputHandler;

    @BeforeEach
    void setUp() {
        inputHandler = new InputHandler(inputProvider, inputValidator);
    }

    @Test
    @Order(1)
    @DisplayName("When given valid directory path, then should return it")
    void testValidDirectoryPath() {
        // Given
        String validPath = "C:\\validDirectory";
        when(inputProvider.getNextLine()).thenReturn(validPath);
        doNothing().when(inputValidator).validateDirectory(validPath);

        // When
        String result = inputHandler.getDirectory();

        // Then
        assertEquals(validPath, result);
        verify(inputValidator, times(1)).validateDirectory(validPath);
    }

    @Test
    @Order(2)
    @DisplayName("When given invalid directory path, then should retry until valid input")
    void testInvalidDirectoryPath() {
        // Given
        String invalidPath = "invalid/path";
        String validPath = "C:\\validDirectory";

        when(inputProvider.getNextLine()).thenReturn(invalidPath, validPath);
        doThrow(new IllegalArgumentException("Invalid directory path.")).when(inputValidator).validateDirectory(invalidPath);
        doNothing().when(inputValidator).validateDirectory(validPath);

        // When
        String result = inputHandler.getDirectory();

        // Then
        assertEquals(validPath, result);
        verify(inputValidator, times(2)).validateDirectory(anyString());
    }

    @Test
    @Order(3)
    @DisplayName("When given valid attribute, then should return it")
    void testValidAttribute() {
        // Given
        String validAttribute = "name";
        when(inputProvider.getNextLine()).thenReturn(validAttribute);
        doNothing().when(inputValidator).validateAttribute(validAttribute);

        // When
        String result = inputHandler.getAttribute();

        // Then
        assertEquals(validAttribute, result);
        verify(inputValidator, times(1)).validateAttribute(validAttribute);
    }

    @Test
    @Order(4)
    @DisplayName("When given invalid attribute, then should retry until valid input")
    void testInvalidAttribute() {
        // Given
        String invalidAttribute = "";
        String validAttribute = "valid";
        when(inputProvider.getNextLine()).thenReturn(invalidAttribute, validAttribute);
        doThrow(new IllegalArgumentException("Attribute cannot be empty.")).when(inputValidator).validateAttribute(invalidAttribute);
        doNothing().when(inputValidator).validateAttribute(validAttribute);

        // When
        String result = inputHandler.getAttribute();

        // Then
        assertEquals(validAttribute, result);
        verify(inputValidator, times(2)).validateAttribute(anyString());
    }

    @Test
    @Order(5)
    @DisplayName("When given valid thread pool size, then should return it")
    void testValidThreadPoolSize() {
        // Given
        String input = "4";
        int expectedSize = 4;
        when(inputProvider.getNextLine()).thenReturn(input);
        doNothing().when(inputValidator).validateInteger(input);
        doNothing().when(inputValidator).validateThreadPoolSize(expectedSize);

        // When
        int result = inputHandler.getThreadPoolSize();

        // Then
        assertEquals(expectedSize, result);
        verify(inputValidator, times(1)).validateInteger(input);
        verify(inputValidator, times(1)).validateThreadPoolSize(expectedSize);
    }

    @Test
    @Order(6)
    @DisplayName("When given invalid thread pool size, then should retry until valid input")
    void testInvalidThreadPoolSize() {
        // Given
        String invalidInputNotInteger = "invalid";
        String invalidInputIncorrectThreadPoolSize = "0";
        String validInput = "3";
        int expectedSize = 3;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        when(inputProvider.getNextLine()).thenReturn(invalidInputNotInteger, invalidInputIncorrectThreadPoolSize, validInput);

        doThrow(new IllegalArgumentException("Input must be an integer!"))
                .when(inputValidator).validateInteger(invalidInputNotInteger);

        doThrow(new IllegalArgumentException("Maximum thread pool size is " + availableProcessors + ". Please try again."))
                .when(inputValidator).validateInteger(invalidInputIncorrectThreadPoolSize);

        doNothing().when(inputValidator).validateInteger(validInput);

        // When
        int result = inputHandler.getThreadPoolSize();

        // Then
        assertEquals(expectedSize, result);
        verify(inputValidator, times(3)).validateInteger(anyString());
        verify(inputValidator, times(1)).validateThreadPoolSize(expectedSize);
    }

}
