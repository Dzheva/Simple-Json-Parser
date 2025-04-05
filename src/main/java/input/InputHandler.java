package input;

import dto.ProcessingConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InputHandler {
    private final InputProvider inputProvider;
    private final InputValidator inputValidator;

    public InputHandler(InputProvider inputProvider, InputValidator inputValidator) {
        this.inputProvider = inputProvider;
        this.inputValidator = inputValidator;
    }

    public ProcessingConfig getUserInput() {
        String directoryPath = getDirectory();
        String attribute = getAttribute();
        int threadPoolSize = getThreadPoolSize();

        return new ProcessingConfig(directoryPath, attribute, threadPoolSize);
    }

    public String getDirectory() {
        String directoryPath;
        System.out.print("Enter the directory path: ");
        while (true) {
            directoryPath = inputProvider.getNextLine();
            try {
                inputValidator.validateDirectory(directoryPath);
                System.out.println("✅You entered: " + directoryPath);
                log.info("✅You entered: " + directoryPath);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("⛔Input error: %s", e.getMessage()));
                log.warn("⛔Input error: {}", e.getMessage());
            }
        }
        return directoryPath;
    }

    public String getAttribute() {
        String attribute;
        System.out.print("Enter the attribute: ");

        while (true) {
            attribute = inputProvider.getNextLine();

            try {
                inputValidator.validateAttribute(attribute);
                System.out.println("✅You entered: " + attribute);
                log.info("✅You entered: {}", attribute);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("⛔Input error: %s", e.getMessage()));
                log.warn("⛔Input error: {}", e.getMessage());
            }

        }

        return attribute;
    }

    public int getThreadPoolSize() {
        int threadPoolSize;
        System.out.print("Enter the thread pool size: ");

        while (true) {
            String input = inputProvider.getNextLine();

            try {
                inputValidator.validateInteger(input);
                threadPoolSize = Integer.parseInt(input);

                inputValidator.validateThreadPoolSize(threadPoolSize);

                System.out.println("✅You entered: " + threadPoolSize);
                log.info("✅You entered: {}", threadPoolSize);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("⛔Input error: %s", e.getMessage()));
                log.warn("⛔Input error: {}", e.getMessage());
            }
        }

        return threadPoolSize;
    }

}
