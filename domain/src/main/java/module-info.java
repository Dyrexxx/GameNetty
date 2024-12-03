module domain {
    requires static lombok;
    requires io.netty.transport;
    requires io.netty.common;
    requires io.netty.buffer;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires java.desktop;
    exports ru.utin.game.domain;
    exports ru.utin.game.packets;
}