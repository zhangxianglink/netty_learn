package net.select;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * x.z
 * Create in 2023/11/23
 */
public class Server {
    public static void main(String[] args) {
        // 初始化服务
        try(ServerSocketChannel server = ServerSocketChannel.open()) {

            server.bind(new InetSocketAddress("127.0.0.1", 8080));
            // 选择器
            Selector selector = Selector.open();
            server.configureBlocking(false);
            // 通过选择器控制连接事件, 每个通道使用自己的附件
            server.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                int select = selector.select();
                System.out.println("select 数量：{}" + select);

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()){
                        // 客户端连接
                        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                        System.out.println("before accepting ... ");
                        // 获取连接, 处理/取消
                        SocketChannel accept = channel.accept();
                        System.out.println("after accepting ... ");
                        if (accept!= null){
                            accept.configureBlocking(false);
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            accept.register(selector, SelectionKey.OP_READ, buffer);
                        }
                    }else if (selectionKey.isReadable()){
                        // 客户端读取
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        // 获取附件
                        ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();
                        System.out.println("before reading ... ");
                        int read = client.read(attachment);
                        if (read == -1){
                            attachment.flip();
                            System.out.println("客户端断开连接"+  StandardCharsets.UTF_8.decode(attachment));
                            selectionKey.cancel();
                            client.close();
                        }
                        split(attachment);
                        if (attachment.position() == attachment.limit()){
                            // 附件扩容
                            ByteBuffer copy = ByteBuffer.allocate(attachment.capacity() * 2);
                            attachment.flip();
                            copy.put(attachment);
                            // 重新放入
                            selectionKey.attach(copy);
                        }
                        System.out.println("after reading " + read);
                    }
                    iterator.remove();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void split(ByteBuffer buffer) {
        buffer.flip();
        for(int i = 0; i < buffer.limit(); i++) {
            // 遍历寻找分隔符
            // get(i)不会移动position
            if (buffer.get(i) == '-') {
                // 缓冲区长度
                int length = i+1-buffer.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                // 将前面的内容写入target缓冲区
                for(int j = 0; j < length; j++) {
                    // 将buffer中的数据写入target中
                    target.put(buffer.get());
                }
                target.flip();
                System.out.println("target :" +  StandardCharsets.UTF_8.decode(target));
            }
        }
        // 切换为写模式，但是缓冲区可能未读完，这里需要使用compact
        buffer.compact();

}
}
