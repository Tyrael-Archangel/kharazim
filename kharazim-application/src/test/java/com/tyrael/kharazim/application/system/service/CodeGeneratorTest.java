package com.tyrael.kharazim.application.system.service;

import com.tyrael.kharazim.application.config.TagStepConfig;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import static com.tyrael.kharazim.application.config.BusinessCodeConstants.SYSTEM_TEST;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@SpringBootTest(properties = {
        "system.global.code-generator=DATASOURCE"
})
@Slf4j
class CodeGeneratorTest {

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private TagStepConfig tagStepConfig;

    @Test
    void next() {
        for (int i = 0; i < 10; i++) {
            String next = codeGenerator.next(SYSTEM_TEST);
            System.out.println(next);
        }
    }

    @Test
    void concurrentNext() throws Exception {

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        try {
            for (int i = 0; i < 200; i++) {
                CountDownLatch cdl = new CountDownLatch(threadCount);
                CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount);
                List<String> codes = new ArrayList<>();
                for (int j = 0; j < threadCount; j++) {
                    executorService.execute(() -> {
                        try {
                            cyclicBarrier.await();
                            String next = codeGenerator.next(SYSTEM_TEST);
                            codes.add(next);
                            cdl.countDown();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                cdl.await();
                System.out.println(codes);
                if (codes.size() != new HashSet<>(codes).size()) {
                    throw new IllegalStateException("error");
                }
            }
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    void speedTest() throws Exception {

        int defaultStep = 500;
        int threadCount = 20;
        long maxCount = 10000000L;

        tagStepConfig.setDefaultStep(defaultStep);
        List<String> randomTags = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            randomTags.add(SYSTEM_TEST.name() + i);
        }

        AtomicLong count = new AtomicLong();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    codeGenerator.next(CollectionUtils.random(randomTags), 12);
                    if (count.incrementAndGet() >= maxCount) {
                        return;
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }

        long lastCount = 0L;
        int seconds = 5;
        while (count.incrementAndGet() < maxCount) {
            try {
                LockSupport.parkUntil(System.currentTimeMillis() + (seconds * 1000L));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return;
            }
            long current = count.get();
            log.info("got " + ((current - lastCount) / seconds) + " codes per second "
                    + "with cache: " + defaultStep
                    + ", thread count: " + threadCount);
            lastCount = current;
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    void dailyNext() {
        for (int i = 0; i < 10; i++) {
            String next = codeGenerator.dailyNext(SYSTEM_TEST);
            System.out.println(next);
        }
    }

    @Test
    void dailyTimeNext() {
        for (int i = 0; i < 10; i++) {
            String next = codeGenerator.dailyTimeNext(SYSTEM_TEST);
            System.out.println(next);
        }
    }

}

