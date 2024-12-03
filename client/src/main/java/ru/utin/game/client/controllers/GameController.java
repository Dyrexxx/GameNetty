package ru.utin.game.client.controllers;

import io.netty.channel.ChannelFutureListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.utin.game.client.network.Network;
import ru.utin.game.domain.Board;
import ru.utin.game.domain.Cell;
import ru.utin.game.domain.Game;
import ru.utin.game.domain.TypeCell;
import ru.utin.game.packets.*;


import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;


public class GameController implements Initializable {
    private final Network network;
    @FXML
    private FlowPane root;


    public GameController(Network network) {
        this.network = network;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.getChildren().add(network.getPlayer().getGame().getBoard().getCanvas());
        network.setOnPlayerReceivedCallback(null);
        network.setCallbackGame(args -> Platform.runLater(() -> {
            if(args[0] instanceof Packet9GameOver) {
                FXMLLoader flowPane = new FXMLLoader((getClass().getClassLoader().getResource("multiply.fxml")));
                flowPane.setController(new MultiplyController((network)));
                Stage stage = (Stage) root.getScene().getWindow();
                try {
                    stage.setScene(new Scene(flowPane.load()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));

    }
}
