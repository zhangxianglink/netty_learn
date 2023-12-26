package net.netty.p7;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * x.z
 * Create in 2023/12/21
 */
public class TestServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap server = new ServerBootstrap();
        server.group(boss, work)
                .channel(NioServerSocketChannel.class)

                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 最大长度1024 \n 作为分割标志 ，可以考虑DelimiterBasedFrameDecoder进行自定义分割
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println("服务端接收，信息长度：" + buf.readableBytes());
                            }
                        });
                    }
                })
                // 设置接收缓冲区大小，过小分包，过大粘包
//                .option(ChannelOption.SO_RCVBUF, 10)
                .bind(8080);

    }
}
