package net.select;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * x.z
 * Create in 2023/11/24
 */
public class WriteServer {
    public static void main(String[] args) {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(8080));
            Selector selector = Selector.open();
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int select = selector.select();
                System.out.println("连接数量：" + select);

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isAcceptable()) {
                        // 客户的通道
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
//                        System.out.println(serverSocketChannel == server);
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        // 设置足够的数据将缓冲区打满
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < 6000000; i++) {
                            builder.append("a");
                        }
                        ByteBuffer buffer = ByteBuffer.wrap(builder.toString().getBytes());
                        int write = socketChannel.write(buffer);
                        System.out.println("第一次写入多少：" + write);
                        if (buffer.hasRemaining()) {
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_WRITE, buffer);
                        }
                    } else if (selectionKey.isWritable()) {

                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();
                        int write = socketChannel.write(attachment);
                        System.out.println("写事件触发："+write);
                        if (!attachment.hasRemaining()) {
                            System.out.println("写完了");
//                            socketChannel.close();
                            selectionKey.attach(null);
                            selectionKey.interestOps(0);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
