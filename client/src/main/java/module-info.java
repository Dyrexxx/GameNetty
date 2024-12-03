module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires com.almasb.fxgl.all;
    exports ru.utin.game.client to javafx.graphics;
    opens ru.utin.game.client to javafx.fxml;
    opens ru.utin.game.client.controllers to javafx.fxml;

    requires io.netty.transport;
    requires io.netty.codec;
    requires com.fasterxml.jackson.databind;
    requires static lombok;
    requires domain;
    requires io.netty.buffer;
    requires java.desktop;

}