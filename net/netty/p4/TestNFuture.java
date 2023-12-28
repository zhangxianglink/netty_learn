package net.netty.p4;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;

import java.util.concurrent.ExecutionException;

/**
 * x.z
 * Create in 2023/12/6
 */
public class TestNFuture {

    // 强化版本java Future
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop next = group.next();
        Future<Integer> future = next.submit(() -> {
            System.out.println("1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 3;
        });


        future.addListener(f -> {
            System.out.println("2");
            System.out.println(f.getNow());
            group.shutdownGracefully();
        });


    }
}
