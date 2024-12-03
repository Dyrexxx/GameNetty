package ru.utin.game.packets;

import io.netty.buffer.ByteBuf;

public class Packet5StartGame extends Packet {
    public static final Integer ID = 4;
    public Packet5StartGame() {
        super(ID);
    }

    @Override
    public void get(ByteBuf buffer) {

    }

    @Override
    public void send(ByteBuf buffer) {

    }
}
