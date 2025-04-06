package utils;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.StatisticsService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class JsonFilesParserTest {
    @Mock
    private StatisticsService statisticsService;
    private JsonFilesParser jsonFilesParser;
    private String attribute;

    @BeforeEach
    void setUp() {
        jsonFilesParser = new JsonFilesParser();
        attribute = "type";
    }

    @Test
    @DisplayName("Should count matching attribute occurrences in flat JSON")
    @SneakyThrows
    @Order(1)
    void tesFlatJson() {
        // Given
        String flatJson = """
                {
                  "type": "A",
                  "value": 123,
                  "type": "A"
                }
                """;
        File file = createTempJsonFile(flatJson);

        // When
        jsonFilesParser.parseFile(file, attribute, statisticsService);

        // Then
        verify(statisticsService, times(2)).increment("A");
    }

    @Test
    @DisplayName("Should count nested attribute values correctly")
    @SneakyThrows
    @Order(2)
    void testNestedJson() {
        // Given
        String nestedJson = """
                {
                  "items": [
                    {"type": "A"},
                    {"type": "B"},
                    {"type": "A"}
                  ]
                }
                """;
        File file = createTempJsonFile(nestedJson);

        //When
        jsonFilesParser.parseFile(file, attribute, statisticsService);

        // Then
        verify(statisticsService, times(1)).increment("B");
        verify(statisticsService, times(2)).increment("A");
    }

    @Test
    @DisplayName("Should count attribute even with malformed JSON")
    @SneakyThrows
    @Order(3)
    void testMalformedJson() {
        // Given
        String malformedJson = """
                {
                  "type": "A",
                  "value": 123,
                """;

        File file = createTempJsonFile(malformedJson);

        // When
        jsonFilesParser.parseFile(file, attribute, statisticsService);

        // Then
        verify(statisticsService, times(1)).increment("A");
    }

    @Test
    @DisplayName("Should not call increment when attribute not found")
    @SneakyThrows
    @Order(4)
    void testMissingAttribute() {
        // Given
        String json = """
            {
              "name": "test",
              "value": 1
            }
            """;
        File file = createTempJsonFile(json);

        // When
        jsonFilesParser.parseFile(file, attribute, statisticsService);

        // Then
        verifyNoInteractions(statisticsService);
    }

    @Test
    @DisplayName("Should work with empty JSON")
    @SneakyThrows
    @Order(5)
    void testEmptyJson() {
        // Given
        String emptyJson = """
            {}
            """;
        File file = createTempJsonFile(emptyJson);

        // When
        jsonFilesParser.parseFile(file, attribute, statisticsService);

        // Then
        verifyNoInteractions(statisticsService);
    }

    @Test
    @DisplayName("Should count attribute even if its value is null")
    @SneakyThrows
    @Order(6)
    void testNullAttributeValue() {
        // Given
        String jsonWithNull = """
            {
              "type": null,
              "other": "value"
            }
            """;
        File file = createTempJsonFile(jsonWithNull);

        // When
        jsonFilesParser.parseFile(file, attribute, statisticsService);

        // Then
        verify(statisticsService, times(1)).increment("null");
    }

    private File createTempJsonFile(String json) throws IOException {
        File tempFile = File.createTempFile("test", ".json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(json);
        }
        tempFile.deleteOnExit();
        return tempFile;
    }
}