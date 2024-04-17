package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MyParserTest {
    @Test
    public void testParseOrdersFromFile() {
        String fileName = "src/test/java/org/example/test.json";
        File testFile = new File(System.getProperty("user.dir") + File.separator + fileName);

        List<Order> orders = new ArrayList<>();
        try {
            orders = MyParser.parseOrdersFromFile(testFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(orders);
        assertEquals(2, orders.size());
    }

    @Test
    public void testExtractAttributes() {
        List<Order> orders = Arrays.asList(
                new Order("Laptop", "HP", 1200, "John Doe"),
                new Order("Smartphone", "Samsung", 800, "Jane Smith"),
                new Order("Smartwatch", "Samsung", 1000, "Jane Smith")

        );

        List<String> productTypes = MyParser.extractAttributes(orders, "productType");

        assertNotNull(productTypes);
        assertEquals(3, productTypes.size());
    }

    @Test
    public void testCalculateStatistics() {
        List<String> attributes = Arrays.asList("Laptop", "Smartphone", "Tablet", "Smartphone");

        Map<String, Long> statistics = MyParser.calculateStatistics(attributes);

        assertNotNull(statistics);
        assertEquals(3, statistics.size());
        assertEquals(2L, statistics.get("Smartphone").longValue());
    }
}