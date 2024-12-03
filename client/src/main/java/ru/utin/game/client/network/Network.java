package ru.utin.game.client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import ru.utin.game.client.network.handlers.PlayerEncoder;
import ru.utin.game.domain.CallbackGame;
import ru.utin.game.domain.Player;
import ru.utin.game.domain.Callback;
import ru.utin.game.packets.*;


@Getter
@Setter
public class Network {
    private SocketChannel channel;
    private Callback onPlayerReceivedCallback;
    private CallbackGame callbackGame;
    private Player player;

    public Network() {
        player = new Player();
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();

                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                player.getGame().setChannel(channel);
                                socketChannel.pipeline().addLast(new PlayerEncoder(),
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                Packet packet = Packet.read((ByteBuf) msg);
                                                if (onPlayerReceivedCallback != null) {
                                                    if (packet instanceof Packet1OnlineCountMenu) {
                                                        onPlayerReceivedCallback.callback(packet);
                                                    }
                                                    if (packet instanceof Packet3UpdateAllLobbies) {
                                                        onPlayerReceivedCallback.callback(packet);
                                                    }

                                                }
                                                if (getCallbackGame() != null) {
                                                    if (packet instanceof Packet9GameOver) {
                                                        getCallbackGame().callback(packet);
                                                    }
                                                }
                                                player.acceptPackage(packet);
                                            }
                                        });
                            }
                        });

                ChannelFuture f = b.connect("localhost", 8080).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();

    }
}