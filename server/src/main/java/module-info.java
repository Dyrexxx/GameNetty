module server {
    requires io.netty.common;
    requires io.netty.buffer;
    requires static lombok;
    requires io.netty.transport;
    requires com.fasterxml.jackson.databind;
    requires domain;
    requires io.netty.codec;
}