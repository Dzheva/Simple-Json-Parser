package utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import service.StatisticsService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class JsonFilesParser {
    public void parseFile(File file, String attribute, StatisticsService statisticsService) {
        try (JsonParser parser = new JsonFactory().createParser(file)) {
            processJson(parser, attribute, statisticsService);
        } catch (IOException e) {
            System.err.println("Error processing file: " + file.getName() + " - " + e.getMessage());
        }
    }

    private void processJson(JsonParser parser, String attribute, StatisticsService statisticsService) throws IOException {
        Deque<JsonToken> stack = new ArrayDeque<>(); // Stack to track JSON depth

        while (!parser.isClosed()) {
            JsonToken token = parser.nextToken();
            if (token == null) break;

            switch (token) {
                case START_OBJECT, START_ARRAY -> stack.push(token); // Track nested structures
                case END_OBJECT, END_ARRAY -> {
                    if (!stack.isEmpty()) stack.pop(); // Safe pop, prevents NoSuchElementException
                }
                case FIELD_NAME -> {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken(); // Move to value

                    if (fieldName.equals(attribute)) {
                        String value = parser.getText();
                        statisticsService.increment(value);
                    }
                }
            }
        }
    }

}
