package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author Tyrael Archangel
 * @since 2024/11/8
 */
public class ThreadLocalRandomDemoTest {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    @Test
    public void random() throws InterruptedException {
        int threadCounts = 10;
        CountDownLatch cdl = new CountDownLatch(threadCounts);
        for (int i = 0; i < threadCounts; i++) {
            new Thread(() -> {
                List<Integer> list = IntStream.range(1, 10)
                        .mapToObj(e -> RANDOM.nextInt(100))
                        .toList();
                System.out.println(list);
                cdl.countDown();
            }).start();
        }
        cdl.await();
    }

}
