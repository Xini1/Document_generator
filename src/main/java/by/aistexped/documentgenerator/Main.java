package by.aistexped.documentgenerator;

import by.aistexped.documentgenerator.ui.JavaFXApplication;

public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        logger.logStart();

        JavaFXApplication.launchApplication(args);

        logger.logExit();
    }
}
