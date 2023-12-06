package net.netty.p1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * x.z
 * Create in 2023/11/28
 */
public class HelloServe {
    public static void main(String[] args) {
        // 一个EventLoop 绑定多个channel
        new ServerBootstrap()
                // 分配,执行的thread和selector
                .group(new NioEventLoopGroup())
                // 选择服务器ServerSocketChannel的实现方式
               .channel(NioServerSocketChannel.class)
                // 绑定处理器, 处理客户端的连接socketChannel 连接后, 就会调用initChannel方法绑定更多处理器
               .childHandler(new ChannelInitializer<NioSocketChannel>(){

                   @Override
                   protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                       // 接收数据 bytebuffer => string
                       nioSocketChannel.pipeline().addLast(new StringDecoder());
                       // 处理客户端的业务逻辑, 使用上个处理器的处理结果
                       nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                           @Override
                           protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                               System.out.println(msg);
                           }
                       });
                   }
               })
                // 绑定端口
                .bind(8080);
    }
}
