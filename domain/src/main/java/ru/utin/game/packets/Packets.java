package ru.utin.game.packets;

import lombok.Getter;

@Getter
public enum Packets {
    PACKET_1_STATISTIC_CONNECT(0, new Packet1OnlineCountMenu()),
    PACKET_2_CREATE_LOBBY(1, new Packet2CreateLobby()),
    PACKET_2_UPDATE_ALL_LOBBIES(2, new Packet3UpdateAllLobbies()),
    PACKET_4_CONNECT_TO_LOBBY(3, new Packet4ConnectToLobby()),
    PACKET_5_START_GAME(4, new Packet5StartGame()),
    PACKET_6_UPDATE_GAME_SITUATION(5, new Packet6UpdateGameSituation()),
    PACKET_7(6, new Packet7MakeMotion()),
    PACKET_8(7, new Packet8MakeTypeCell()),
    PACKET_9(8, new Packet9GameOver());

    private final int id;
    private final Packet packet;

    Packets(int id, Packet packet) {
        this.id = id;
        this.packet = packet;
    }
}
