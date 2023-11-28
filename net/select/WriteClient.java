package net.select;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * x.z
 * Create in 2023/11/24
 */
public class WriteClient {
    public static void main(String[] args) {

        try(SocketChannel open = SocketChannel.open()) {
            ByteBuffer allocate = ByteBuffer.allocate(500000);
            open.connect(new java.net.InetSocketAddress("127.0.0.1", 8080));
            int count = 0;
            while (true){
                int read = open.read(allocate);
                count += read;
                allocate.flip();
                System.out.println("接收数据：" + count);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
