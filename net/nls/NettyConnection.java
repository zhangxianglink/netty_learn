package net.nls;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class NettyConnection {
    Channel channel;
    public NettyConnection(Channel channel) {
        this.channel = channel;
    }

    public String getId() {
        return this.channel != null ? this.channel.id().toString() : null;
    }

    public boolean isActive() {
        return this.channel != null && this.channel.isActive();
    }


    public void close() {
        this.channel.close();
    }

    public void sendText(String payload) {
        if (this.channel != null && this.channel.isActive()) {
            TextWebSocketFrame frame = new TextWebSocketFrame(payload);
            this.channel.writeAndFlush(frame);
        }
    }

    public void sendBinary(byte[] payload) {
        if (this.channel != null && this.channel.isActive()) {
            BinaryWebSocketFrame frame = new BinaryWebSocketFrame(Unpooled.wrappedBuffer(payload));
            this.channel.writeAndFlush(frame);
        }
    }

    public void sendPing() {
        PingWebSocketFrame frame = new PingWebSocketFrame();
        if (this.channel != null && this.channel.isActive()) {
            this.channel.writeAndFlush(frame);
        }
    }
}
