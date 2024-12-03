package ru.utin.game.packets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import ru.utin.game.domain.Board;
import ru.utin.game.domain.Lobby;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Getter
public class Packet6UpdateGameSituation extends Packet {
    public static final Integer ID = 5;
    private String lobbyId;

    public Packet6UpdateGameSituation() {
        super(ID);
    }
    public Packet6UpdateGameSituation(String lobbyId) {
        this();
        this.lobbyId = lobbyId;
    }

    @Override
    public void get(ByteBuf buffer) {
        lobbyId = (String) buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public void send(ByteBuf buffer) {
        buffer.writeCharSequence(lobbyId, StandardCharsets.UTF_8);
    }
}