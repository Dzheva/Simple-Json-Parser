package org.example;

import java.io.File;

public class App {
    public static void main(String[] args) {
        MyParser parser = new MyParser();
        int[] threadCounts = {1, 2, 4, 8};

        //In the folderPath variable, you can specify the path to the folder where the JSON files for parsing are stored.
        String folderPath = System.getProperty("user.dir") + File.separator + "src/main/java/org/example/JsonFilesFolder";

        //Specify the attribute for parsing: productType, brandName, clientName, price
        String attribute = "brandName";

        for (int threadCount : threadCounts) {
            long startTime = System.currentTimeMillis();

            parser.run(folderPath, attribute, threadCount);

            long endTime = System.currentTimeMillis();
            System.out.println("Parsing with " + threadCount + (threadCount == 1 ? " thread " : " threads ")
                    + (endTime - startTime) + " ms");
        }
    }
}
