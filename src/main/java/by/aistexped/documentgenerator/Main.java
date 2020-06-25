package by.aistexped.documentgenerator;

import by.aistexped.documentgenerator.ui.JavaFXApplication;

public class Main {

    public static void main(String[] args) {
        try{
            JavaFXApplication.launchApplication(args);
        } catch (Exception e){
            Logger.getInstance().logException(e);
        }
    }
}
