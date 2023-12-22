package net.netty.p7;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * x.z
 * Create in 2023/12/21
 * tcp上面应用层，传输数据大小不一致问题
 */
public class TestClient {
    public static void main(String[] args) throws Exception {
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
               .handler(new ChannelInitializer<NioSocketChannel>() {
                   @Override
                   protected void initChannel(NioSocketChannel ch) throws Exception {
                       ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                           @Override
                           public void channelActive(ChannelHandlerContext ctx) throws Exception {
                               for (int i = 0; i < 10; i++) {
                                   ByteBuf buffer = ctx.alloc().buffer();
                                   buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
                                   ctx.writeAndFlush(buffer);
                               }
                           }
                       });
                   }
               })
               .connect("127.0.0.1", 8080)
               .sync()
               .channel();
    }
}
