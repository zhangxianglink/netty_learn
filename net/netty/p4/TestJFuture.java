package net.netty.p4;

import java.time.LocalTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * x.z
 * Create in 2023/12/6
 */
public class TestJFuture {

    // 使用Future
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<String> submit = pool.submit(() -> {
            System.out.println("开始计算 1秒后返回");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return LocalTime.now().toString();
        });

        System.out.println("当前时间" + LocalTime.now());
        System.out.println("计算结果" + submit.get());
        pool.shutdown();
    }
}
