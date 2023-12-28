package net.netty.p6;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * x.z
 * Create in 2023/12/21
 * 相互通信
 */
public class TestServer {
    public static void main(String[] args) {
        NioEventLoopGroup selector = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        new ServerBootstrap()
                .group(selector,work)
                .channel(NioServerSocketChannel.class)
               .childHandler(new ChannelInitializer<NioSocketChannel>() {
                   @Override
                   protected void initChannel(NioSocketChannel ch) {
                       ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                           @Override
                           public void channelRead(ChannelHandlerContext ctx, Object msg) {
                               ByteBuf buf = (ByteBuf) msg;
                               String s = buf.toString(Charset.defaultCharset());
                               System.out.println("服务端 : "+ s);
                               if(s.equals("quit")){
                                   ctx.close();
                               }else {
                                   ByteBuf byteBufToClient = ctx.alloc().buffer();
                                   byteBufToClient.writeBytes("准备关闭".getBytes(StandardCharsets.UTF_8));
                                   ctx.channel().writeAndFlush(byteBufToClient);
                               }
                           }
                       });
                   }
               })
               .bind(8080);

    }
}
