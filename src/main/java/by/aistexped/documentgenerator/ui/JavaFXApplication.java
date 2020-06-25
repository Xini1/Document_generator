package by.aistexped.documentgenerator.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXApplication extends Application {

    public static void launchApplication(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/window.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Генератор документов");

        stage.show();
    }
}
