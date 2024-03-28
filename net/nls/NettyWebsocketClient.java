package net.nls;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class NettyWebsocketClient {

    //自己决定使用多少个线程
    EventLoopGroup group = new NioEventLoopGroup(0);
    Bootstrap bootstrap = new Bootstrap();
    final String url;
    public NettyWebsocketClient(String url) {
        this.url = url;
        // 连接组
        this.bootstrap.option(ChannelOption.TCP_NODELAY, true)
                .group(this.group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ChannelHandler[]{new HttpClientCodec(), new HttpObjectAggregator(8192)});
                        p.addLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS));
                        // 添加入站事件处理器
                        p.addLast("hookedHandler", new WebSocketClientHandler());
                    }
        });
    }
    public NettyConnection connect(String token, ConnectionListener listener, int connectionTimeout) throws Exception {
        this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout);
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        httpHeaders.set("X-NLS-Token", token);
        Channel channel = this.bootstrap.connect("192.168.6.102",6026).sync().channel();
        // channel完成

        // WebSocketClientHandshaker: 表示一个WebSocket客户端握手器
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(new URI("ws://192.168.6.102:6026"), WebSocketVersion.V13, null, true, httpHeaders, 196608);
        WebSocketClientHandler handler = (WebSocketClientHandler)channel.pipeline().get("hookedHandler");
        // 添加用户的信息处理器
        handler.setListener(listener);
        handler.setHandshaker(handshaker);
        handshaker.handshake(channel);
        this.waitHandshake(handler.handshakeFuture(), channel);
        // channel连接ws服务完成

        return new NettyConnection(channel);
    }

    public void shutdown() {
        this.group.shutdownGracefully();
    }

    private void waitHandshake(ChannelFuture handshakeFuture, Channel channel) throws Exception {
        if (!handshakeFuture.await(10, TimeUnit.SECONDS)) {
            if (channel.isActive()) {
                channel.close();
            }
            if (handshakeFuture.cause() != null) {
                throw new Exception("Handshake timeout!", handshakeFuture.cause());
            } else {
                throw new Exception("Handshake timeout!");
            }
        }
    }

}
