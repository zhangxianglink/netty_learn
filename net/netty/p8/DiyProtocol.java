package net.netty.p8;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * x.z
 * Create in 2023/12/28
 */
public class DiyProtocol {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new MessageCodec());

        // 进入encode阶段
        Message message = new Message((byte) '1', 1,"hello");
        channel.writeOutbound(message);

        // encode -> decode阶段
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message, buf);

        //测试入站
        channel.writeInbound(buf);

    }
}
