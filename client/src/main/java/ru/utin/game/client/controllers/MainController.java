package ru.utin.game.client.controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.utin.game.client.MainApplication;
import ru.utin.game.client.network.Network;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private FlowPane root;

    @FXML
    public void onMultiplyButtonClick() throws IOException {
        FXMLLoader flowPane = new FXMLLoader((getClass().getClassLoader().getResource("multiply.fxml")));
        flowPane.setController(new MultiplyController(new Network()));
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(flowPane.load()));}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}