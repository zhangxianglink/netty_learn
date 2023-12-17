package net.netty.p5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

/**
 * x.z
 * Create in 2023/12/6
 */
public class TestHandler {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        new ServerBootstrap()
                .group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                      @Override
                      protected void initChannel(NioSocketChannel ch) throws Exception {
                          ch.pipeline().addLast("1",new ChannelInboundHandlerAdapter(){
                              @Override
                              public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                  System.out.println("1");
                                  ByteBuf buf = (ByteBuf)msg;
                                  String name = buf.toString(Charset.defaultCharset());
                                  super.channelRead(ctx, name + "1");
                              }
                          });

                          ch.pipeline().addLast("2",new ChannelInboundHandlerAdapter(){
                              @Override
                              public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                  System.out.println("2");
                                  super.channelRead(ctx, msg + "2");
                              }
                          });

                          ch.pipeline().addLast("3",new ChannelInboundHandlerAdapter(){
                              @Override
                              public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                  System.out.println("3");
                                  ch.writeAndFlush(msg + "3");
                              }
                          });

                          ch.pipeline().addLast("4",new ChannelOutboundHandlerAdapter(){
                              @Override
                              public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                  System.out.println("4");
                                  super.write(ctx, msg + "4", promise);
                              }
                          });

                          ch.pipeline().addLast("5",new ChannelOutboundHandlerAdapter(){
                              @Override
                              public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                  System.out.println("5");
                                  msg = msg + "5";
                                  System.out.println(msg);
                                  super.write(ctx, msg, promise);
                              }
                          });
                      }
                  }
                ).bind(8080);
    }
}
