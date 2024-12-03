package ru.utin.game.packets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import ru.utin.game.domain.TypeCell;

import java.nio.charset.StandardCharsets;

@Getter
public class Packet9GameOver extends Packet {
    public static final Integer ID = 8;
    private TypeCell typeCell;

    public Packet9GameOver() {
        super(ID);
    }

    public Packet9GameOver(TypeCell typeCell) {
        this();
        this.typeCell = typeCell;
    }

    @Override
    public void get(ByteBuf buffer) {
        ObjectMapper mapper = new ObjectMapper();
        String s = buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString();
        try {
            typeCell = mapper.readValue(s, TypeCell.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(ByteBuf buffer) {
        ObjectMapper o = new ObjectMapper();
        try {
            String s = o.writeValueAsString(typeCell);
            buffer.writeCharSequence(s, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
