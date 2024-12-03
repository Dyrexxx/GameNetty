package ru.utin.game.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ru.utin.game.client.network.Network;
import ru.utin.game.packets.*;
import ru.utin.game.domain.Lobby;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MultiplyController implements Initializable {
    private final Network network;
    @FXML
    private Label online;
    @FXML
    private FlowPane root;
    @FXML
    private FlowPane container;
    @FXML
    private Button createLobbyButton;
    @FXML
    private Button updateLobby;

    public MultiplyController(Network network) {
        this.network = network;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        network.setCallbackGame(null);
        network.setOnPlayerReceivedCallback((args) -> {
            Platform.runLater(() -> {
                if (args[0] instanceof Packet1OnlineCountMenu) {
                    online.setText(String.valueOf(((Packet1OnlineCountMenu) args[0]).getOnline()));
                }
                if (args[0] instanceof Packet3UpdateAllLobbies packet) {
                    updateAllLobbies(container, packet.getLobbyList());
                }
            });
        });
        createLobbyButton.setOnAction(event -> {
            createLobby();
        });
        updateLobby.setOnAction(event -> {
            updateLobby();
        });
    }


    private void updateAllLobbies(FlowPane flowPane, List<Lobby> lobbyList) {
        flowPane.getChildren().clear();
        for (Lobby lobby : lobbyList) {
            flowPane.getChildren().add(new Label(lobby.getTitle()));
            Button button = new Button("Зайти");
            button.setOnAction(event -> {
                connectToLobby(lobby.getUuid());
            });
            flowPane.getChildren().add(button);
        }
    }

    private void connectToLobby(String uuid) {
        network.getChannel().writeAndFlush(new Packet4ConnectToLobby(uuid));
        try {
            FXMLLoader flowPane = new FXMLLoader((getClass().getClassLoader().getResource("game.fxml")));
            flowPane.setController(new GameController(network));
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(flowPane.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLobby() {
        network.getChannel().writeAndFlush(new Packet3UpdateAllLobbies());
    }


    private void createLobby() {
        Lobby l = network.getPlayer().getLobby();
        network.getChannel().writeAndFlush(new Packet2CreateLobby(l.create("dimaLobby")));
        try {
            FXMLLoader flowPane = new FXMLLoader((getClass().getClassLoader().getResource("game.fxml")));
            flowPane.setController(new GameController(network));
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(flowPane.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}