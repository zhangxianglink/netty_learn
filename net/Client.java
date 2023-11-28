package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * x.z
 * Create in 2023/11/23
 */
public class Client {
    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // 建立连接
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
            System.out.println("waiting...");
            Thread.sleep(5000);
            socketChannel.write(ByteBuffer.wrap("hello-sda".getBytes()));
            Thread.sleep(5000);
            socketChannel.write(ByteBuffer.wrap("hello-oooooooo-ooooo".getBytes()));
            Thread.sleep(5000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
