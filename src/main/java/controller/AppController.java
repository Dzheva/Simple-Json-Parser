package controller;

import utils.InputHandler;
import dto.ProcessingConfig;
import service.ProcessingService;

public class AppController {
    private final InputHandler inputHandler;
    private final ProcessingService processingService;

    public AppController(InputHandler inputHandler, ProcessingService processingService) {
        this.inputHandler = inputHandler;
        this.processingService = processingService;
    }

    public void start()  {
        ProcessingConfig config = inputHandler.getUserInput();

        System.out.println("✅Processing started...");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long startTime = System.nanoTime();

        processingService.processing(config.directoryPath(), config.attribute(), config.threadPoolSize());

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        System.out.println("✅Processing finished.");
        System.out.println("✅Total execution time: " + durationMs + " ms");
    }

}

