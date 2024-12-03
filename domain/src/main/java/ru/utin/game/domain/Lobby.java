package ru.utin.game.domain;

import io.netty.channel.Channel;
import lombok.Data;

import java.util.Date;

@Data
public class Lobby {
    private boolean isCreate = false;
    private String uuid;
    private String title;

    public Lobby create(String title) {
        if (!isCreate) {
            isCreate = true;
            uuid = title + new Date().getTime();
            this.title = title;
        }
        return this;
    }

    public void clear() {
        isCreate = false;
        uuid = null;
        title = null;
    }
}
