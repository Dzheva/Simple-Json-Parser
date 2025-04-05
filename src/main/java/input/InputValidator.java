package input;

import java.io.File;

public class InputValidator {
    public void validateDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path. Please try again.");
        }
    }

    public void validateAttribute(String attribute) {
        if (attribute.isEmpty()) {
            throw new IllegalArgumentException("Attribute cannot be empty. Please try again.");
        }

        if (attribute.length() > 50) {
            throw new IllegalArgumentException("Attribute length exceeds 50 characters. Please try again.");
        }
    }

    public void validateInteger(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input must be an integer!");
        }
    }

    public void validateThreadPoolSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Input must be an integer greater than 0!");
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (size > availableProcessors) {
            throw new IllegalArgumentException("Maximum thread pool size is " + availableProcessors + ". Please try again.");
        }
    }
}
