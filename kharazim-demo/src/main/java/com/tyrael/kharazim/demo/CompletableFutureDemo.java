package com.tyrael.kharazim.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Tyrael Archangel
 * @since 2024/1/23
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("do something");
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果1";
        });

        CompletableFuture<String> future2 = future1.handleAsync((r, t) -> r + " 二次处理结果");
        String result = future2.get();
        System.out.println(result);

    }

}
