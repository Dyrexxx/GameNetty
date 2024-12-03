package ru.utin.game.packets;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public class Packet4ConnectToLobby extends Packet {
    public static final Integer ID = 3;
    private String uuid;

    public Packet4ConnectToLobby() {
        super(ID);
    }

    public Packet4ConnectToLobby(String uuid) {
        this();
        this.uuid = uuid;
    }

    @Override
    public void get(ByteBuf buffer) {
        uuid = (String) buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public void send(ByteBuf buffer) {
        buffer.writeCharSequence(uuid, StandardCharsets.UTF_8);
    }
}