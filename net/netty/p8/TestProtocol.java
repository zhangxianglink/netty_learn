package net.netty.p8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * x.z
 * Create in 2023/12/27
 * redis 协议
 */
public class TestProtocol {
    public static void main(String[] args) throws InterruptedException {
        //换行，回车
        final byte[] LINE = {13, 10};
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("准备发送：");
                                ByteBuf buffer = ctx.alloc().buffer();
                                buffer.writeBytes("*3".getBytes());
                                buffer.writeBytes(LINE);
                                buffer.writeBytes("$3".getBytes());
                                buffer.writeBytes(LINE);
                                buffer.writeBytes("set".getBytes());
                                buffer.writeBytes(LINE);
                                buffer.writeBytes("$1".getBytes());
                                buffer.writeBytes(LINE);
                                buffer.writeBytes("a".getBytes());
                                buffer.writeBytes(LINE);
                                buffer.writeBytes("$1".getBytes());
                                buffer.writeBytes(LINE);
                                buffer.writeBytes("b".getBytes());
                                buffer.writeBytes(LINE);
                                ctx.writeAndFlush(buffer);
                            }
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println("返回结果：" + buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .connect(new InetSocketAddress("192.168.1.1", 6379))
                .sync()
                .channel();
    }
}
