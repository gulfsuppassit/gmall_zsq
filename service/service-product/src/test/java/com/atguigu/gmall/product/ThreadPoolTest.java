package com.atguigu.gmall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

import static java.lang.System.out;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/23 18:56
 */
@SpringBootTest
public class ThreadPoolTest {

    @Autowired
    private ThreadPoolExecutor corePool;

    @Test
    public void threadPoolTest() throws ExecutionException, InterruptedException {
//       corePool.execute(()->{
//           System.out.println(Thread.currentThread().getName()+":hello world");
//       });
//        System.out.println("threadPool:"+corePool.getMaximumPoolSize());
        CompletableFuture.supplyAsync(()->{
            int i = 1+1;
            out.println(Thread.currentThread().getName()+":"+i);
            return i;
        },corePool).thenAcceptAsync((result)->{
            out.println(Thread.currentThread().getName()+":"+(result+10));
        });

    }

    @Test
    public void completableFutureTest(){
        CompletableFuture.supplyAsync(()->{
            int i = 1+1;
            //int z = 1/0;
            return i;
        });
    }

    @Test
    public void completableFutureTest02() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            int num = 1 + 1;
            return num;
        }, corePool).thenAccept((t) -> {
            out.println(t + 2);
        });

        Void unused = future.get();
    }

    @Test
    public void completableFutureTest03() throws ExecutionException, InterruptedException {
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.supplyAsync(() -> {
            int num = 1 + 1;
            return num;
        }, corePool).thenCompose((t) -> {
            out.println(t);
            return null;
        });

      /*  Object o = objectCompletableFuture.get();
        out.println(o);*/
    }





}
