package net.select;

import buider.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * x.z
 * Create in 2023/11/27
 */
public class ThreadClient {
    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                    try (SocketChannel socketChannel = SocketChannel.open()) {
                        // 建立连接
                        socketChannel.connect(new InetSocketAddress("localhost", 8080));
                        socketChannel.write(ByteBuffer.wrap("hello-sda".getBytes()));
                        socketChannel.write(ByteBuffer.wrap("hello-sda".getBytes()));
                    } catch (IOException  e) {
                        e.printStackTrace();
                    }
            }).start();
        }
    }

}
