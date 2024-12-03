package ru.utin.game.domain;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import javafx.application.Platform;
import lombok.Data;
import ru.utin.game.packets.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class LobbyWorker {
    private final Map<String, ChannelGroup> lobbyGroups = new ConcurrentHashMap<>();
    private final Map<String, ChannelGroup> startedGames = new ConcurrentHashMap<>();
    private final ChannelGroup mainGroup;

    public LobbyWorker(ChannelGroup mainGroup) {
        this.mainGroup = mainGroup;
    }

    private List<Lobby> createInfoLobbiesMap(Map<String, ChannelGroup> allLobbies) {
        List<Lobby> list = new ArrayList<>();
        for (Map.Entry<String, ChannelGroup> entry : allLobbies.entrySet()) {
            Lobby lobby = new Lobby();
            lobby.setUuid(entry.getKey());
            lobby.setTitle(entry.getValue().name());
            list.add(lobby);
        }
        return list;
    }

    public void gameOver(Packet9GameOver gameOver, Channel channel) {
        for (ChannelGroup channelGroup : startedGames.values()) {
            if (channelGroup.contains(channel)) {
                channelGroup.writeAndFlush(gameOver);
                clearStartedGames(channelGroup);
                break;
            }
        }
    }

    private void clearStartedGames(ChannelGroup channelGroup) {
        for (Map.Entry<String, ChannelGroup> entry : startedGames.entrySet()) {
            if (entry.getValue().contains(channelGroup)) {
                startedGames.remove(entry.getKey());
            }
        }
    }

    public void updateGameSituation(Packet7MakeMotion packet7MakeMotion, Channel channel) {
        for (ChannelGroup channelGroup : startedGames.values()) {
            if (channelGroup.contains(channel)) {
                for (Channel c : channelGroup) {
                    if (c != channel) {
                        c.writeAndFlush(packet7MakeMotion);
                        break;
                    }
                }
                break;
            }
        }

    }

    public void connectToLobby(Packet4ConnectToLobby packet, Channel channel) {
        ChannelGroup channelGroup = lobbyGroups.get(packet.getUuid());
        lobbyGroups.remove(packet.getUuid());
        channelGroup.add(channel);
        startedGames.put(packet.getUuid(), channelGroup);
        startedGames.get(packet.getUuid()).writeAndFlush(new Packet6UpdateGameSituation(packet.getUuid()));
        startedGames.get(packet.getUuid()).find(channel.id()).writeAndFlush(new Packet5StartGame());
        for (ChannelGroup group : startedGames.values()) {
            if (group.contains(channel)) {
                for (Channel c : group) {
                    if (c != channel) {
                        c.writeAndFlush(new Packet8MakeTypeCell(TypeCell.CIRCLE));
                    } else {
                        c.writeAndFlush(new Packet8MakeTypeCell(TypeCell.CROSS));
                    }
                }
                break;
            }
        }
    }

    public void create(Packet2CreateLobby packet, Channel channel) {
        ChannelGroup newChannelGroup = new DefaultChannelGroup(packet.getLobby().getTitle(), GlobalEventExecutor.INSTANCE);
        newChannelGroup.add(channel);
        lobbyGroups.put(packet.getLobby().getUuid(), newChannelGroup);
        mainGroup.writeAndFlush(new Packet3UpdateAllLobbies(createInfoLobbiesMap(lobbyGroups)));
    }

    public void update(Channel channel) {
        channel.writeAndFlush(new Packet3UpdateAllLobbies(createInfoLobbiesMap(lobbyGroups)));
    }
}
