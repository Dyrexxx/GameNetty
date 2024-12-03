package ru.utin.game.domain;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;
import ru.utin.game.packets.Packet1OnlineCountMenu;

@Getter
public class ServerWorker {
    private final ChannelGroup mainChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private int onlineCount;


    public ServerWorker() {
        onlineCount = 0;
    }

    public void connect(Channel channel) {
        onlineCount++;
        mainChannelGroup.add(channel);
        mainChannelGroup.writeAndFlush(new Packet1OnlineCountMenu(onlineCount));
    }

    public void disconnect(Channel channel) {
        onlineCount--;
        mainChannelGroup.add(channel);
        mainChannelGroup.writeAndFlush(new Packet1OnlineCountMenu(onlineCount));
    }
}
