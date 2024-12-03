package ru.utin.game.packets;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import ru.utin.game.domain.Lobby;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Getter
public class Packet3UpdateAllLobbies extends Packet {
    public static final Integer ID = 2;
    private List<Lobby> lobbyList;

    public Packet3UpdateAllLobbies() {
        super(ID);
    }

    public Packet3UpdateAllLobbies(List<Lobby> lobbyList) {
        this();
        this.lobbyList = lobbyList;

    }

    @Override
    public void get(ByteBuf buffer) {
        ObjectMapper mapper = new ObjectMapper();
        String s = buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString();
        try {
            lobbyList = mapper.readValue(s, new TypeReference<List<Lobby>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void send(ByteBuf buffer) {
        ObjectMapper o = new ObjectMapper();
        try {
            String s = o.writeValueAsString(lobbyList);
            buffer.writeCharSequence(s, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}