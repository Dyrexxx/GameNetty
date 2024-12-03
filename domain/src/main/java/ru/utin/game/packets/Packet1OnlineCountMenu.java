package ru.utin.game.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.group.ChannelGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Packet1OnlineCountMenu extends Packet{
    public static final Integer ID = 0;
    private Integer online;
    private Map<String, ChannelGroup> allLobbies;

    public Packet1OnlineCountMenu() {
        super(ID);
    }

    public Packet1OnlineCountMenu(Integer online) {
        this();
        this.online = online;
    }

    @Override
    public void get(ByteBuf buffer) {
        online = buffer.readInt();
    }

    @Override
    public void send(ByteBuf buffer) {
        buffer.writeInt(online);
    }
}