package ru.utin.game.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import ru.utin.game.domain.LobbyWorker;
import ru.utin.game.domain.ServerWorker;
import ru.utin.game.packets.*;

public class SimpleProcessingHandler extends ChannelInboundHandlerAdapter {
    private static final ServerWorker serverWorker = new ServerWorker();
    private static final LobbyWorker lobbyWorker = new LobbyWorker(serverWorker.getMainChannelGroup());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключен");
        serverWorker.connect(ctx.channel());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packet packet = Packet.read((ByteBuf) msg);
        if (packet instanceof Packet2CreateLobby packet2CreateLobby) {
            lobbyWorker.create(packet2CreateLobby, ctx.channel());
        }
        if (packet instanceof Packet3UpdateAllLobbies) {
            lobbyWorker.update(ctx.channel());
        }
        if (packet instanceof Packet4ConnectToLobby packet4ConnectToLobby) {
            lobbyWorker.connectToLobby(packet4ConnectToLobby, ctx.channel());
        }
        if (packet instanceof Packet7MakeMotion packet7MakeMotion) {
            lobbyWorker.updateGameSituation(packet7MakeMotion, ctx.channel());
        }
        if (packet instanceof Packet9GameOver packet9GameOver) {
            lobbyWorker.gameOver(packet9GameOver, ctx.channel());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент отключился");
        serverWorker.disconnect(ctx.channel());
    }

}