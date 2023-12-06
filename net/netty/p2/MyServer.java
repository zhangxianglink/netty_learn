package net.netty.p2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * x.z
 * Create in 2023/12/5
 */
public class MyServer {
    public static void main(String[] args) {
        //细分2：创建一个新的EventLoopGroup专门处理那些耗时较长的操作
        EventLoopGroup group = new DefaultEventLoopGroup();

        // 连接组，默认一个线程。 读写操作可以设置线程个数
        new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel >() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("Header1",new ChannelInboundHandlerAdapter(){
                           @Override
                           public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                               ByteBuf buf = (ByteBuf) msg;
                               System.out.println(Thread.currentThread().getName() + " " + buf.toString(StandardCharsets.UTF_8));
                               //将消息传递给下一个handler
                               ctx.fireChannelRead(msg);
                           }
                        }).addLast(group, "handler2", new ChannelInboundHandlerAdapter(){
                            @Override                       //ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println(Thread.currentThread().getName() + " " + buf.toString(StandardCharsets.UTF_8));
                            }
                        });
                    }
                })
               .bind(8080);
    }
}
