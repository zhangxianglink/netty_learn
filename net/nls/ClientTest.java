package net.nls;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

import java.nio.ByteBuffer;

public class ClientTest {

    // 启动后等待JavaStart3的调用
    public static void main(String[] args) throws Exception {
        NettyWebsocketClient client = new NettyWebsocketClient("ws://192.168.6.102:6026");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            Channel channel = ch.pipeline().channel();
                            NettyConnection connection = client.connect("token", new ConnectionListener() {
                                @Override
                                public void onOpen() {}
                                @Override
                                public void onClose(int var1, String var2) {}
                                @Override
                                public void onMessage(String var1) {
                                    System.out.println(var1);
                                    channel.writeAndFlush(new TextWebSocketFrame(var1));
                                }
                                @Override
                                public void onMessage(ByteBuffer var1) {}
                            }, 5);
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new HttpServerCodec()); // HTTP 协议解析，用于握手阶段
                            pipeline.addLast(new HttpObjectAggregator(65536)); // HTTP 协议解析，用于握手阶段
                            pipeline.addLast(new WebSocketServerCompressionHandler()); // WebSocket 数据压缩扩展
                            pipeline.addLast(new WebSocketServerProtocolHandler("/", null, true)); // WebSocket 握手、控制帧处理
                            pipeline.addLast(new AudioWebSocketFrameHandler(connection));
                        }
                    });

            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
