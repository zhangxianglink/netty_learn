package net.nls;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class AudioWebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    NettyConnection connection;

    public AudioWebSocketFrameHandler(NettyConnection connection){
        this.connection = connection;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof BinaryWebSocketFrame) {
            //二进制
            System.out.println("接收字节:" );
            ByteBuf content = frame.content();
            byte[] reg = new byte[content.readableBytes()];
            content.readBytes(reg);
            connection.sendBinary(reg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("发生异常");
        cause.printStackTrace();
        ctx.close();
    }
}
