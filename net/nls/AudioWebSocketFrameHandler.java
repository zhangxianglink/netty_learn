package net.nls;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class AudioWebSocketFrameHandler extends SimpleChannelInboundHandler<Object> {
    NettyConnection connection;

    public AudioWebSocketFrameHandler(NettyConnection connection){
        this.connection = connection;
    }
    // 获取客户端连接 WebSocket 服务器使用的请求 URL，包括 URL 中的参数，以及请求的 Header
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete handshakeCompletedEvent = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String uri = handshakeCompletedEvent.requestUri(); // 握手请求 URI
            HttpHeaders headers = handshakeCompletedEvent.requestHeaders(); // 握手请求头
            System.out.println("客户端：" + uri);
            System.out.println("请求头：" + headers);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object frame) {
        //TODO 从客户端接收数据，推送固定大小的byte到asr服务器
        if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame by = (BinaryWebSocketFrame) frame;
            ByteBuf content = by.content();
            byte[] reg = new byte[content.readableBytes()];
            content.readBytes(reg);
            connection.sendBinary(reg);
        }
    }

    public void channelInactive(ChannelHandlerContext ctx) {
        if (ctx.channel() != null) {
            System.out.println("channelInactive:" + ctx.channel().id());
        } else {
            System.out.println("channelInactive");
        }
        connection.close();
    }
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("发生异常");
        cause.printStackTrace();
        ctx.close();
    }
}
