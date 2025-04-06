package service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ProcessingService {
    private final JsonFilesParser jsonFilesParser;
    private final StatisticsService statisticsService;

    public ProcessingService(JsonFilesParser jsonFilesParser, StatisticsService statisticsService) {
        this.jsonFilesParser = jsonFilesParser;
        this.statisticsService = statisticsService;
    }

    public void processing(String directoryPath, String attribute, int threadPoolSize) {
        Path dirPath = Paths.get(directoryPath);
        if (!Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }

        try {
            parseJsonFiles(dirPath, attribute, threadPoolSize);
            statisticsService.saveToXml(attribute);
        } catch (Exception e) {
            System.err.println("Unexpected error during processing: " + e.getMessage());
            log.error("Unexpected error during processing: {}", e.getMessage(), e);
        }
    }


    private void parseJsonFiles(Path dirPath, String attribute, int threadPoolSize) {
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.json")) {
            for (Path filePath : stream) {
                futures.add(parseFileAsync(filePath, attribute, executor));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (IOException e) {
            log.error("Error reading directory: {}", dirPath, e);
        } finally {
            shutdownExecutor(executor);
        }
    }


    private CompletableFuture<Void> parseFileAsync(Path filePath, String attribute, ExecutorService executor) {
        return CompletableFuture.runAsync(() -> {
            jsonFilesParser.parseFile(filePath.toFile(), attribute, statisticsService);
            log.debug("Processed: {} in {}", filePath.getFileName(), Thread.currentThread().getName());
        }, executor);
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error shutting down executor", e);
        }
    }
}
