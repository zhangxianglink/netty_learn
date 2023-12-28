package net.netty.p4;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;

/**
 * x.z
 * Create in 2023/12/6
 */
public class TestNPromise {

    // 针对netty Future 进一步强化
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop next = group.next();
        // 主动创建promise
        DefaultPromise<Object> promise = new DefaultPromise<>(next);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
//                int a = 10 /0;
                promise.setSuccess("success");
            } catch (Exception e) {
                promise.setFailure(new RuntimeException(e));
            }
        }).start();

        // 从外部获取执行结果
        System.out.println(promise.get());
    }
}
