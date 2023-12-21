package net.netty.p6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;

/**
 * x.z
 * Create in 2023/12/21
 */
public class TestSlice {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16, 20);
        buffer.writeBytes("hello-world".getBytes());

        // 切片后时依然使用元素内存， 维护独立的 read，write 指针
        ByteBuf slice = buffer.slice(0, 5);
        System.out.println(slice.toString(Charset.defaultCharset()));

        // 引用计数 +1, 防止原始内存释放
        slice.retain();

    }
}
