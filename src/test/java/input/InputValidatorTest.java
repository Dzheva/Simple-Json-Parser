package input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    private InputValidator inputValidator;

    @BeforeEach
    void setUp() {
        inputValidator = new InputValidator();
    }

    @Test
    @Order(1)
    @DisplayName("whenGivenInvalidDirectoryPath_thenShouldThrowException")
    void testInvalidDirectoryPath() {
        // Given
        String invalidPath = "invalid/path";
        String expectedMsg = "Invalid directory path. Please try again.";

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateDirectory(invalidPath));

        // Then
        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(2)
    @DisplayName("whenGivenValidDirectoryPath_thenShouldNotThrowException")
    void testValidDirectoryPath() {
        String validPath = System.getProperty("user.dir");
        assertDoesNotThrow(() -> inputValidator.validateDirectory(validPath));
    }

    @Test
    @Order(3)
    @DisplayName("whenGivenEmptyAttribute_thenShouldThrowException")
    void testEmptyAttribute() {
        // Given
        String emptyAttribute = "";
        String expectedMsg = "Attribute cannot be empty. Please try again.";

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateAttribute(emptyAttribute));

        // Then
        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(4)
    @DisplayName("whenGivenTooLongAttribute_thenShouldThrowException")
    void testTooLongAttribute() {
        // Given
        String longAttribute = "A".repeat(51);  // Length greater than 50
        String expectedMsg = "Attribute length exceeds 50 characters. Please try again.";

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateAttribute(longAttribute));

        // Then
        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(5)
    @DisplayName("whenGivenValidAttribute_thenShouldNotThrowException")
    void testValidAttribute() {
        String validAttribute = "Valid Attribute";
        assertDoesNotThrow(() -> inputValidator.validateAttribute(validAttribute));
    }

    @Test
    @Order(6)
    @DisplayName("whenGivenNonIntegerInputForThreadPoolSize_thenShouldThrowException")
    void testNonIntegerInput() {
        // Given
        String nonIntegerInput = "notAnInteger";
        String expectedMsg = "Input must be an integer!";

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateInteger(nonIntegerInput));

        // Then
        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(7)
    @DisplayName("whenGivenValidIntegerInputForThreadPoolSize_thenShouldNotThrowException")
    void testValidIntegerInput() {
        String validIntegerInput = "4";
        assertDoesNotThrow(() -> inputValidator.validateInteger(validIntegerInput));
    }

    @Test
    @Order(8)
    @DisplayName("whenGivenZeroThreadPoolSize_thenShouldThrowException")
    void testZeroThreadPoolSize() {
        // Given
        int zeroThreadPoolSize = 0;
        String expectedMsg = "Input must be an integer greater than 0!";

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateThreadPoolSize(zeroThreadPoolSize));

        // Then
        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(9)
    @DisplayName("whenGivenValidThreadPoolSize_thenShouldNotThrowException")
    void testValidThreadPoolSize() {
        int validThreadPoolSize = 4;
        assertDoesNotThrow(() -> inputValidator.validateThreadPoolSize(validThreadPoolSize));
    }
}