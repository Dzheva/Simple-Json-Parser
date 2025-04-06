package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ProcessingServiceTest {
    @Mock
    private JsonFilesParser jsonFilesParser;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private ProcessingService processingService;

    @Test
    @DisplayName("Should correctly process files and save statistics to XML")
    @Order(1)
    void testProcessingWithValidDirectory() {
        // Given
        String validDirectoryPath = "src/main/resources/json_files";
        String attribute = "type";
        int threadPoolSize = 4;

        // When
        processingService.processing(validDirectoryPath, attribute, threadPoolSize);

        // Then
        verify(statisticsService, times(1)).saveToXml(attribute);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException and not save statistics to XML")
    @Order(2)
    void testProcessingWithInValidDirectory() {
        // Given
        String invalidDirectoryPath = "invalid/path/to/json_files";
        String attribute = "type";
        int threadPoolSize = 4;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            processingService.processing(invalidDirectoryPath, attribute, threadPoolSize);
        });

        // Then
        assertTrue(exception.getMessage().contains("Invalid directory path"));
        assertTrue(exception instanceof IllegalArgumentException);
        verifyNoInteractions(statisticsService);
    }


}