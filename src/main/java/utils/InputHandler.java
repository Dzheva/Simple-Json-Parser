package utils;

import dto.ProcessingConfig;

import java.io.File;
import java.util.Scanner;

public class InputHandler {
    public ProcessingConfig getUserInput() {
        Scanner scanner = new Scanner(System.in);

        String directoryPath = getDirectory(scanner);
        String attribute = getAttribute(scanner);
        int threadPoolSize = getThreadPoolSize(scanner);

        scanner.close();
        return new ProcessingConfig(directoryPath, attribute, threadPoolSize);
    }

    public String getDirectory(Scanner scanner) {
        String directoryPath;
        System.out.print("Enter the directory path: ");
        while (true) {
            directoryPath = scanner.nextLine().trim();
            try {
                validateDirectory(directoryPath);
                System.out.println("✅ You entered: " + directoryPath);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
        return directoryPath;
    }

    private static void validateDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("⛔ Error: Invalid directory path. Please try again.");
        }
    }

    public static String getAttribute(Scanner scanner) {
        String attribute;
        System.out.print("Enter the attribute: ");
        while (true) {
            attribute = scanner.nextLine().trim();

            try {
                validateAttribute(attribute);
                System.out.println("✅You entered: " + attribute);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }

        }

        return attribute;
    }

    private static void validateAttribute(String attribute) {
        if (attribute.isEmpty()) {
            throw new IllegalArgumentException("⛔ Error: Attribute cannot be empty. Please try again.");
        }

        if (attribute.length() > 50) {
            throw new IllegalArgumentException("⛔ Error: Attribute length exceeds 50 characters. Please try again.");
        }
    }

    public int getThreadPoolSize(Scanner scanner) {
        int threadPoolSize;
        System.out.print("Enter the thread pool size: ");

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                threadPoolSize = validateThreadPoolSize(input);
                System.out.println("✅ You entered: " + threadPoolSize);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }

        }

        return threadPoolSize;
    }

    private int validateThreadPoolSize(String input) {
        int size;

        try {
            size = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("⛔ Input must be an integer!");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("⛔ Input must be an integer greater than 0!");
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        if (size > availableProcessors) {
            throw new IllegalArgumentException("⛔ Maximum thread pool size is " + availableProcessors + ". Please try again.");
        }

        return size;
    }
}
