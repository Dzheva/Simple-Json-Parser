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
    void invalidDirectoryPath() {
        String invalidPath = "invalid/path";
        String expectedMsg = "Invalid directory path. Please try again.";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateDirectory(invalidPath));

        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(2)
    @DisplayName("whenGivenValidDirectoryPath_thenShouldNotThrowException")
    void validDirectoryPath() {
        String validPath = System.getProperty("user.dir");
        assertDoesNotThrow(() -> inputValidator.validateDirectory(validPath));
    }

    @Test
    @Order(3)
    @DisplayName("whenGivenEmptyAttribute_thenShouldThrowException")
    void emptyAttribute() {
        String emptyAttribute = "";
        String expectedMsg = "Attribute cannot be empty. Please try again.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateAttribute(emptyAttribute));

        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(4)
    @DisplayName("whenGivenTooLongAttribute_thenShouldThrowException")
    void tooLongAttribute() {
        String longAttribute = "A".repeat(51);  // Length greater than 50
        String expectedMsg = "Attribute length exceeds 50 characters. Please try again.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateAttribute(longAttribute));

        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(5)
    @DisplayName("whenGivenValidAttribute_thenShouldNotThrowException")
    void validAttribute() {
        String validAttribute = "Valid Attribute";
        assertDoesNotThrow(() -> inputValidator.validateAttribute(validAttribute));
    }

    @Test
    @Order(6)
    @DisplayName("whenGivenNonIntegerInputForThreadPoolSize_thenShouldThrowException")
    void nonIntegerInput() {
        String nonIntegerInput = "notAnInteger";
        String expectedMsg = "Input must be an integer!";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateInteger(nonIntegerInput));

        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(7)
    @DisplayName("whenGivenValidIntegerInputForThreadPoolSize_thenShouldNotThrowException")
    void validIntegerInput() {
        String validIntegerInput = "4";
        assertDoesNotThrow(() -> inputValidator.validateInteger(validIntegerInput));
    }

    @Test
    @Order(8)
    @DisplayName("whenGivenZeroThreadPoolSize_thenShouldThrowException")
    void zeroThreadPoolSize() {
        int zeroThreadPoolSize = 0;
        String expectedMsg = "Input must be an integer greater than 0!";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inputValidator.validateThreadPoolSize(zeroThreadPoolSize));

        assertAll(
                () -> assertTrue(exception instanceof IllegalArgumentException),
                () -> assertEquals(expectedMsg, exception.getMessage())
        );
    }

    @Test
    @Order(9)
    @DisplayName("whenGivenValidThreadPoolSize_thenShouldNotThrowException")
    void validThreadPoolSize() {
        int validThreadPoolSize = 4;
        assertDoesNotThrow(() -> inputValidator.validateThreadPoolSize(validThreadPoolSize));
    }
}