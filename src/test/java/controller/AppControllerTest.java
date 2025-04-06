package controller;

import dto.ProcessingConfig;
import input.InputHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.ProcessingService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
class AppControllerTest {
    @Mock
    private InputHandler inputHandler;

    @Mock
    private ProcessingService processingService;

    @InjectMocks
    private AppController appController;

    @Test
    @DisplayName("Should start successfully")
    @Order(1)
    void testStartWithValidInput() {
        // Given
        ProcessingConfig config = new ProcessingConfig("valid_directory", "type", 4);

        // When
        when(inputHandler.getUserInput()).thenReturn(config);
        appController.start();

        // Then
        verify(processingService, times(1)).processing(config.directoryPath(), config.attribute(), config.threadPoolSize());
    }

    @Test
    @DisplayName("Should log error when exception is thrown during processing")
    @Order(2)
    void testStartWithException() {
        // Given
        ProcessingConfig config = new ProcessingConfig("invalid_directory", "type", 4);
        when(inputHandler.getUserInput()).thenReturn(config);
        doThrow(new RuntimeException("Processing failed")).when(processingService).processing(config.directoryPath(), config.attribute(), config.threadPoolSize());

        // Prepare to capture System.err output
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        // When
        appController.start();

        // Then
        String expectedErrorMessage = "⛔Error during processing: Processing failed";
        String actualErrorMessage = errContent.toString().trim();

        // Verify that the error message was printed correctly
        assertTrue(actualErrorMessage.contains(expectedErrorMessage), "Error message did not match");

    }
}