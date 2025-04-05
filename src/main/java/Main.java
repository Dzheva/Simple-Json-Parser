import controller.AppController;
import input.InputHandler;
import input.InputProvider;
import input.InputProviderImpl;
import input.InputValidator;
import service.ProcessingService;
import service.StatisticsService;
import utils.JsonFilesParser;
import utils.StAXXmlWriter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            AppController appController = initializeApp(scanner);
            appController.start();
        }
    }

    private static AppController initializeApp(Scanner scanner) {
        InputProvider inputProvider = new InputProviderImpl(scanner);
        InputValidator inputValidator = new InputValidator();
        InputHandler inputHandler = new InputHandler(inputProvider, inputValidator);

        JsonFilesParser jsonFilesParser = new JsonFilesParser();

        StAXXmlWriter stAXXmlWriter = new StAXXmlWriter();
        StatisticsService statisticsService = new StatisticsService(stAXXmlWriter);

        ProcessingService processingService = new ProcessingService(jsonFilesParser, statisticsService);

        return new AppController(inputHandler, processingService);
    }

}
