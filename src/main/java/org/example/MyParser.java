package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MyParser {
    public static void run(String folderPath, String attribute, int threadCount) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadCount);

        try {
            List<File> fileList = getFilesFromFolder(folderPath);
            Map<String, Long> totalStatistics = processFiles(fileList, attribute, forkJoinPool);
            String resultsFile = "statistics_by_" + attribute + "_threads_" + threadCount + ".xml";
            saveStatisticsToXML(totalStatistics, resultsFile);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static List<File> getFilesFromFolder(String folderPath) throws IOException {
        return Files.list(Paths.get(folderPath)).filter(path -> path.toString().endsWith(".json")).map(Path::toFile).collect(Collectors.toList());
    }

    public static Map<String, Long> processFiles(List<File> fileList, String attribute, ForkJoinPool forkJoinPool) {
        try {
            List<Order> allOrders = forkJoinPool.submit(() -> fileList.parallelStream().flatMap(file -> {
                try {
                    return parseOrdersFromFile(file).stream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList())).get();

            List<String> attributes = extractAttributes(allOrders, attribute);
            return calculateStatistics(attributes);
        } catch (Exception e) {
            throw new RuntimeException("Error processing files: " + e.getMessage(), e);
        }
    }

    public static List<Order> parseOrdersFromFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Order> orders = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(file)) {
            JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream);

            while (jsonParser.nextToken() != null) {
                if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                    JsonNode node = objectMapper.readTree(jsonParser);

                    if (isOrderNode(node)) {
                        try {
                            String productType = node.get("productType").asText();
                            String brandName = node.get("brandName").asText();
                            int price = node.get("price").asInt();
                            String clientName = node.get("clientName").asText();

                            Order order = new Order(productType, brandName, price, clientName);
                            orders.add(order);
                        } catch (NullPointerException e) {
                            System.err.println("Error parsing JSON node: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            throw e;
        }

        return orders;
    }

    public static boolean isOrderNode(JsonNode node) {
        return node.has("productType") && node.has("brandName") && node.has("price") && node.has("clientName");
    }


    public static List<String> extractAttributes(List<Order> orders, String attribute) {
        return orders.stream().map(order -> {
            switch (attribute) {
                case "productType":
                    return order.getProductType();
                case "brandName":
                    return order.getBrandName();
                case "price":
                    return Integer.toString(order.getPrice());
                case "clientName":
                    return order.getClientName();
                default:
                    throw new IllegalArgumentException("Incorrectly specified attribute for statistics calculation");
            }
        }).collect(Collectors.toList());
    }

    public static Map<String, Long> calculateStatistics(List<String> attributes) {
        return attributes.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public static void saveStatisticsToXML(Map<String, Long> attributeCounts, String outputFileName) throws JAXBException {

        if (attributeCounts.isEmpty()) {
            System.out.println("Attribute counts map is null. Cannot save statistics to XML.");
            return;
        }

        try {
            XmlStatisticsWriter statistics = new XmlStatisticsWriter();
            statistics.setAttributeCounts(attributeCounts);
            JAXBContext context = JAXBContext.newInstance(XmlStatisticsWriter.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(statistics, new File(outputFileName));
        } catch (Exception e) {
            throw new JAXBException("An error occurred when saving statistics to a file", e);
        }
    }
}
