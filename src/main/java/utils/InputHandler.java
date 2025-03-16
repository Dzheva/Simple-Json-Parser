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
        while (true) {
            System.out.print("Enter the directory path: ");
            directoryPath = scanner.nextLine().trim();

            File directory = new File(directoryPath);
            if (directory.exists() && directory.isDirectory()) {
                System.out.println("✅You entered: " + directoryPath);
                break;
            } else {
                System.out.println("⛔️Error: Invalid directory path. Please try again.");
            }
        }
        return directoryPath;
    }

    public String getAttribute(Scanner scanner) {
        String attribute;
        while (true) {
            System.out.print("Enter the attribute for statistics: ");
            attribute = scanner.nextLine().trim();

            if (attribute.isEmpty()) {
                System.out.println("⛔️Error: Attribute cannot be empty. Please try again.");
            } else if (attribute.length() > 50) {
                System.out.println("⛔️Error: Attribute length exceeds 50 characters. Please try again.");
            } else {
                System.out.println("✅You entered: " + attribute);
                break;
            }

        }
        return attribute;
    }

    public int getThreadPoolSize(Scanner scanner) {
        int threadPoolSize;
        while (true) {
            System.out.print("Enter the thread pool size: ");

            if (scanner.hasNextInt()) {
                threadPoolSize = scanner.nextInt();
                if (threadPoolSize > 0) {
                    System.out.println("✅You entered: " + threadPoolSize);
                    break;
                } else {
                    System.out.println("⛔️Input an integer greater than 0!");
                }
            } else {
                System.out.println("⛔️That's not an integer!");
                scanner.next();
            }
        }
        return threadPoolSize;
    }

}
