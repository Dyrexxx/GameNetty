package ru.utin.game.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.utin.game.packets.*;

import java.util.List;

@Getter
@Setter
@ToString
public class Player {
    private String name;
    private final Lobby lobby;
    private List<Lobby> lobbyList;
    private final Game game;

    public Player() {
        lobby = new Lobby();
        game = new Game();
    }

    public void acceptPackage(Packet packet) {
        if (packet instanceof Packet3UpdateAllLobbies packet3) {
            lobbyList = packet3.getLobbyList();
        }
        if (packet instanceof Packet6UpdateGameSituation packet6) {
            game.setStarted(true);
            game.setLobbyUUID(packet6.getLobbyId());
            System.out.println("Игра началась");
        }
        if (packet instanceof Packet5StartGame packet5StartGame) {
            game.setCanMove(true);
            System.out.println("Ваш ход");
        }
        if (packet instanceof Packet8MakeTypeCell packet8MakeTypeCell) {
            game.getBoard().setTypeCell(packet8MakeTypeCell.getTypeCell());
        }
        if (packet instanceof Packet7MakeMotion packet7MakeMotion) {
            game.makeMotion(packet7MakeMotion);
            System.out.println("Ваш ход");
        }
        if (packet instanceof Packet9GameOver packet9GameOver) {
            if (game.getBoard().getTypeCell() == packet9GameOver.getTypeCell()) {
                System.out.println("Вы победили");
            } else {
                System.out.println("Вы проиграли");
            }
            lobby.clear();
            game.clear();
        }
    }
}
