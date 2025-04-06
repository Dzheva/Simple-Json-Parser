package service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.StAXXmlWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {
    @Mock
    private StAXXmlWriter stAXXmlWriter;
    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    @DisplayName("Should correctly increment the count for a given key in statistics map\"")
    @Order(1)
    void testIncrement() {
        // Given
        String key = "A";

        // When
        statisticsService.increment(key);

        // Then
        assertEquals(1, statisticsService.getStatistics().get(key).get(), "Increment did not work correctly");
    }

    @Test
    @DisplayName("Should call saveToXml method to save statistics to XML file")
    @Order(2)
    void testSaveToXml() {
        // Given
        String attribute = "type";

        // When
        statisticsService.saveToXml(attribute);

        // Then
        verify(stAXXmlWriter, times(1)).writeStatistics(statisticsService.getStatistics(), attribute);
    }
}