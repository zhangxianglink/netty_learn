package net.netty.p8;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * x.z
 * Create in 2023/12/28
 * 自定义编解码器
 */
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        System.out.println("encode:");
        // 魔数  4
        out.writeBytes(new byte[]{'N', 'Y', 'I', 'M'});
        // 版本号 1
        out.writeByte(1);
        // 序列化 1
        out.writeByte(1);
        // 指令 1
        out.writeByte(msg.getMessageType());
        // 请求序号 4
        out.writeInt(msg.getSequenceId());
        // 无意义，对齐填充 1
        out.writeByte(0xff);
        // 将一个对象转换为字节数组。
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();

        // 获得并设置正文长度 4
        out.writeInt(bytes.length);
        // 设置消息正文
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode:");
        int magic = in.readInt();
        byte version = in.readByte();
        byte serializableType = in.readByte();
        byte messageType = in.readByte();
        int seqId = in.readInt();
        in.readByte();

        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes,0 ,length);
        // 字符数组转换为对象
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();
        System.out.println("magic " + magic +" version " + version+" serializableType " + serializableType+" messageType " + messageType+" seqId " + seqId);
        System.out.println("message: " + message.toString());
        // 将信息放入List中，传递给下一个handler
        out.add(message);
    }
}
