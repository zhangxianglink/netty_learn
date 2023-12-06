package net.netty.p3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;


/**
 * x.z
 * Create in 2023/12/5
 */
public class MyChannelFuture {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 通道的异步操作
        ChannelFuture connect = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                }).connect("127.0.0.1", 8080);
        // 开始线程阻塞，中间过程NIO
        connect.sync();
        Channel channel = connect.channel();
        channel.writeAndFlush("hello world");

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String next = scanner.nextLine();
                if ("exit".equals(next)) {
                    channel.close();
                    System.out.println("执行关闭前处理 ----------------");
                    break;
                }
                channel.writeAndFlush(next);
            }
        }).start();

        // 如何合理关闭
        ChannelFuture closeFuture = channel.closeFuture();
        // 同步关闭
        closeFuture.sync();
        group.shutdownGracefully();
        System.out.println("执行关闭后处理 ----------------");

        // 异步关闭
//        closeFuture.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                System.out.println("处理关闭后的操作");
//                group.shutdownGracefully();
//            }
//        });


    }
}
