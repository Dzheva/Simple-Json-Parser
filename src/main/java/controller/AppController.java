package controller;

import lombok.extern.slf4j.Slf4j;
import input.InputHandler;
import dto.ProcessingConfig;
import service.ProcessingService;
@Slf4j
public class AppController {
    private final InputHandler inputHandler;
    private final ProcessingService processingService;

    public AppController(InputHandler inputHandler, ProcessingService processingService) {
        this.inputHandler = inputHandler;
        this.processingService = processingService;
    }

    public void start()  {
        ProcessingConfig config  = inputHandler.getUserInput();

        System.out.println("✅Processing started...");
        log.info("✅Processing started...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long startTime = System.nanoTime();

        try {
            processingService.processing(config.directoryPath(), config.attribute(), config.threadPoolSize());
        } catch (Exception e) {
            System.err.println("⛔Error during processing: " + e.getMessage());
            log.error("⛔Error during processing: {}", e.getMessage(), e);
            return;
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        System.out.println("✅Processing finished.");
        log.info("✅Processing finished.");
        System.out.println("✅Total execution time: " + durationMs + " ms");
        log.info("✅Total execution time: " + durationMs + " ms");
    }

}

