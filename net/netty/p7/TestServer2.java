package net.netty.p7;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.Charset;

/**
 * x.z
 * Create in 2023/12/21
 */
public class TestServer2 {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap server = new ServerBootstrap();
        server.group(boss, work)
                .channel(NioServerSocketChannel.class)

                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        /**
                         * LTC解码器
                         * maxFrameLength 数据最大长度
                         *
                         * 表示数据的最大长度（包括附加信息、长度标识等内容）
                         * lengthFieldOffset 数据长度标识的起始偏移量
                         *
                         * 用于指明数据第几个字节开始是用于标识有用字节长度的，因为前面可能还有其他附加信息
                         * lengthFieldLength 数据长度标识所占字节数（用于指明有用数据的长度）
                         *
                         * 数据中用于表示有用数据长度的标识所占的字节数
                         * lengthAdjustment 长度表示与有用数据的偏移量
                         *
                         * 用于指明数据长度标识和有用数据之间的距离，因为两者之间还可能有附加信息
                         * initialBytesToStrip 数据读取起点
                         *
                         * 读取起点，不读取 0 ~ initialBytesToStrip 之间的数据
                         */
                        ch.pipeline().addLast( new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));

                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println("服务端接收，信息长度：" + buf.readableBytes() + "，内容：" + buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8080);

    }
}
