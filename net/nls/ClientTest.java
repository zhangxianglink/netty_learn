package net.nls;

import audio.v3.JavaClient;
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
import org.java_websocket.WebSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ClientTest {

    public static void client() throws Exception {
        NettyWebsocketClient client = new NettyWebsocketClient("ws://192.168.6.102:6026");
        NettyConnection connection = client.connect("token", new ConnectionListener() {
            @Override
            public void onOpen() {}
            @Override
            public void onClose(int var1, String var2) {}
            @Override
            public void onMessage(String var1) {
                System.out.println(var1);
            }
            @Override
            public void onMessage(ByteBuffer var1) {}
        }, 5);
        Thread.sleep(2000);

        byte[] bytesToSend = getBytes();
        byte[] tmp = new byte[16384];
        int bytesIndex = 0;
        for (int i = 0; i < bytesToSend.length; i++) {
            tmp[bytesIndex++] = bytesToSend[i];
            if (bytesIndex == 16384) {
                connection.sendBinary(tmp);
                Thread.sleep(10);
                bytesIndex = 0;
            }
        }
        if (bytesIndex > 0) { // if there is any leftover data
            byte[] leftover = Arrays.copyOf(tmp, bytesIndex);
            connection.sendBinary(leftover);
        }
    }



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


    public static byte[] getBytes() throws Exception {
        String path = "D:\\data\\8kt\\right_16k.wav";

        byte[] buffer = Files.readAllBytes(Paths.get(path));
        System.out.println("buffer: " + buffer.length);

        File file = new File(path);
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        AudioFormat format = ais.getFormat();
        System.out.println(format.toString());

        // byte array to short array
        short[] shortArr = new short[buffer.length / 2];
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArr);

        // short arr 转 float arr
        int sampleIndex = 0;
        float[] floats = new float[buffer.length / 2];
        for (int i = 0; i < shortArr.length; i ++) {
            float temp = shortArr[i] / 32767.0f;
            floats[sampleIndex++] = temp;
        }

        // float arr 转 byte arr
        ByteBuffer buffer2 = ByteBuffer.allocate(floats.length * Float.BYTES);
        // 设置字节顺序为小端
        buffer2.order(ByteOrder.LITTLE_ENDIAN);
        for (float f : floats) {
            buffer2.putFloat(f);
        }
        byte[] bytesToSend = buffer2.array();
        return bytesToSend;
    }
}
