package ru.utin.game.packets;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;

@Getter
public abstract class Packet {
    private int id;

    protected Packet(int id) {
        this.id = id;
    }

    public static Packet read(ByteBuf buffer) throws IOException {
        int id = buffer.readUnsignedShort();
        Packet packet = getPacket(id);
        if (packet == null)
            throw new IOException("Bad packet ID: " + id);
        packet.get(buffer);
        return packet;
    }

    public static void write(Packet packet, ByteBuf buffer) {
        buffer.writeChar(packet.getId());
        packet.send(buffer);
    }

    @SneakyThrows
    private static Packet getPacket(int id) {
        for (Packets packet : Packets.values()) {
            if (packet.getId() == id) {
                return packet.getPacket();
            }
        }
        return null;
    }

    public abstract void get(ByteBuf buffer);

    public abstract void send(ByteBuf buffer);
}
