import service.ProcessingService;
import controller.AppController;
import service.StatisticsService;
import utils.InputHandler;
import utils.JsonFilesParser;
import utils.StAXXmlWriter;

public class Main {

    public static void main(String[] args) {
        AppController appController = initializeApp();
        appController.start();
    }

    private static AppController initializeApp() {
        InputHandler inputHandler = new InputHandler();

        JsonFilesParser jsonFilesParser = new JsonFilesParser();
        StAXXmlWriter stAXXmlWriter = new StAXXmlWriter();
        StatisticsService statisticsService = new StatisticsService(stAXXmlWriter);
        ProcessingService processingService = new ProcessingService(jsonFilesParser, statisticsService);

        return new AppController(inputHandler, processingService);
    }

}
