package ru.utin.game.packets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import ru.utin.game.domain.Lobby;

import java.nio.charset.StandardCharsets;

@Getter
public class Packet2CreateLobby extends Packet {
    public static final Integer ID = 1;
    private Lobby lobby;

    public Packet2CreateLobby() {
        super(ID);
    }

    public Packet2CreateLobby(Lobby lobby) {
        this();
        this.lobby = lobby;
    }

    @Override
    public void get(ByteBuf buffer) {
        ObjectMapper mapper = new ObjectMapper();
        String s = buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString();
        try {
            lobby = mapper.readValue(s, Lobby.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(ByteBuf buffer) {
        ObjectMapper o = new ObjectMapper();
        try {
            String s = o.writeValueAsString(lobby);
            buffer.writeCharSequence(s, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}