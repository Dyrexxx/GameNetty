package ru.utin.game.packets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import ru.utin.game.domain.Cell;
import ru.utin.game.domain.CellPosition;

import java.nio.charset.StandardCharsets;

@Getter
public class Packet7MakeMotion extends Packet {
    public static final Integer ID = 6;
    private CellPosition board;

    public Packet7MakeMotion() {
        super(ID);
    }

    public Packet7MakeMotion(CellPosition board) {
        this();
        this.board = board;
    }

    @Override
    public void get(ByteBuf buffer) {
        ObjectMapper mapper = new ObjectMapper();
        String s = buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString();
        try {
            board = mapper.readValue(s, CellPosition.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void send(ByteBuf buffer) {
        ObjectMapper o = new ObjectMapper();
        try {
            String s = o.writeValueAsString(board);
            buffer.writeCharSequence(s, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
