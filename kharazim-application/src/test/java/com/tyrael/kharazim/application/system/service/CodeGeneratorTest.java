package com.tyrael.kharazim.application.system.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

import static com.tyrael.kharazim.application.config.BusinessCodeConstants.SYSTEM_TEST;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@SpringBootTest(properties = {
        "system.global.code-generator=DATASOURCE"
})
class CodeGeneratorTest {

    @Resource
    private CodeGenerator codeGenerator;

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

