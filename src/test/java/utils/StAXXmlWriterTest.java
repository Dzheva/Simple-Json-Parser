package utils;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


class StAXXmlWriterTest {
    private final StAXXmlWriter stAXXmlWriter = new StAXXmlWriter();
    private String pathToFile;
    private File xmlFile;

    @Test
    @DisplayName("Should save statistics to XML file")
    @SneakyThrows
    @Order(1)
    void testWriteStatistics() {
        // Given
        Map<String, AtomicInteger> statistics = Map.of(
                "A", new AtomicInteger(3),
                "B", new AtomicInteger(2),
                "C", new AtomicInteger(1)
        );
        String attribute = "type";

        // When
        stAXXmlWriter.writeStatistics(statistics, attribute);
        pathToFile = "src/main/resources/xml_statistics/statistics_by_" + attribute + ".xml";
        xmlFile = new File(pathToFile);

        // Read elements from XML into Map
        Map<String, Integer> mapFromXml = parseXmlToMap(xmlFile);

        // Convert statistics Map to Map<String, Integer>
        Map<String, Integer> statisticsMap = statistics.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));

        // Then
        assertTrue("XML file was not created", xmlFile.exists());
        assertEquals(statistics.size(), mapFromXml.size());
        assertEquals(statisticsMap, mapFromXml, "Parsed XML content does not match original statistics");
    }

    @Test
    @DisplayName("Should handle empty statistics correctly")
    @SneakyThrows
    @Order(2)
    void testEmptyStatistics() {
        // Given
        Map<String, AtomicInteger> emptyStatistics = new HashMap<>();
        String attribute = "type";

        // When
        stAXXmlWriter.writeStatistics(emptyStatistics, attribute);
        pathToFile = "src/main/resources/xml_statistics/statistics_by_" + attribute + ".xml";
        xmlFile = new File(pathToFile);
        Map<String, Integer> mapFromXml = parseXmlToMap(xmlFile);

        // Then
        assertTrue("XML file was not created", xmlFile.exists());
        assertTrue("XML file should be empty", mapFromXml.isEmpty());
        assertEquals(emptyStatistics.size(), mapFromXml.size());
    }

    @AfterEach
    void cleanUp() {
        if (xmlFile.exists()) {
            boolean deleted = xmlFile.delete();
            System.out.println("✅Deleted: " + deleted);
        } else {
            System.out.println("❌File not found: " + xmlFile.getPath());
        }
    }

    private Map<String, Integer> parseXmlToMap(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList itemNodes = doc.getElementsByTagName("item");
        Map<String, Integer> resultMap = new HashMap<>();

        for (int i = 0; i < itemNodes.getLength(); i++) {
            Element itemElement = (Element) itemNodes.item(i);
            String value = itemElement.getElementsByTagName("value").item(0).getTextContent();
            String count = itemElement.getElementsByTagName("count").item(0).getTextContent();
            resultMap.put(value, Integer.parseInt(count));
        }
        return resultMap;
    }

}
