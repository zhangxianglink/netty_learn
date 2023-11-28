package net.select;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * x.z
 * Create in 2023/11/27
 */
public class ThreadServer {

    static class Acceptor implements Runnable {
        private Thread thread;
        private Selector selector;
        private String name;
        private boolean started = false;
        /**
         * 同步队列，用于Boss线程与Worker线程之间的通信
         */
        private ConcurrentLinkedQueue<Runnable> queue;

        Acceptor(String name) {
            this.name = name;
        }

        public void register(final SocketChannel socket) throws IOException {
            if (!started) {
                System.out.println("执行一次" + name + "的注册");
                this.thread = new Thread(this,name);
                selector = Selector.open();
                queue = new ConcurrentLinkedQueue<>();
                thread.start();
                started = true;
            }
            // 向同步队列中添加SocketChannel的注册事件
            // 在Worker线程中执行注册事件
            queue.add(() -> {
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    socket.register(selector, SelectionKey.OP_READ, buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            // 唤醒被阻塞的Selector, start()后阻塞了, 唤醒后会执行注册事件
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                  selector.select();
                  Runnable poll = queue.poll();
                  if (poll != null) {
                    // 获得任务，执行注册操作
                    poll.run();
                  }
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()){
                            SocketChannel socket = (SocketChannel) key.channel();
                            ByteBuffer attachment = (ByteBuffer) key.attachment();
                            // 进入写模式
                            int read = socket.read(attachment);
                            if (read == -1){
                                key.cancel();
                                socket.close();
                            }
                            // 进入读模式
                            attachment.flip();
                            System.out.println(Thread.currentThread().getName() +" 读取数量：" + StandardCharsets.UTF_8.decode(attachment));
                            // 进入写模式
                            attachment.flip();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new java.net.InetSocketAddress(8080));
        // 当前主线程定义为Boss 用于控制接收accpet请求
        Thread.currentThread().setName("Boss");
        Selector boss = Selector.open();
        server.configureBlocking(false);
        server.register(boss, SelectionKey.OP_ACCEPT);
        // 新增几个线程用于处理读写请求
        Acceptor[] acceptor = new Acceptor[4];
        for (int i = 0; i < acceptor.length; i++) {
            acceptor[i] = new Acceptor("Worker"+i);
        }

        AtomicInteger robin = new AtomicInteger(0);
        while (true){
            boss.select();
            Set<SelectionKey> selectionKeys = boss.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()){
                    SocketChannel accept = server.accept();
                    System.out.println("connected...");
                    accept.configureBlocking(false);
                    System.out.println("before read...");
                    acceptor[robin.getAndIncrement() % acceptor.length].register(accept);
                    System.out.println("after read...");
                }
            }
        }
    }
}


