package ru.utin.game.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.utin.game.client.controllers.MainController;

import java.io.IOException;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Hello World");
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.show();
    }
}