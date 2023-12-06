package net.netty.p1;

import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;

/**
 * x.z
 * Create in 2023/12/5
 * EventLoopGroup 是一组 EventLoop，
 * Channel 一般会调用 EventLoopGroup 的 register 方法来绑定其中一个 EventLoop，
 * 后续这个 Channel 上的 io 事件都由此 EventLoop 来处理（保证了 io 事件处理时的线程安全）
 */
public class TestEventLoop {
    public static void main(String[] args) throws InterruptedException {
        // 创建了两个 EventLoop 单线程执行对象
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(2);

        // loopGroup.next() 是轮询获取下一个 EventLoop
        loopGroup.next().execute(() ->
                System.out.println(Thread.currentThread().getName() +" hello world")
        );

        loopGroup.next().scheduleAtFixedRate(()->
                System.out.println(Thread.currentThread().getName() + " hello2"),
                0, 1, TimeUnit.SECONDS);

        Thread.sleep(2000);
        loopGroup.shutdownGracefully();
    }
}
