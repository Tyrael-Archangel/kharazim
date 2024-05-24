package com.tyrael.kharazim.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Tyrael Archangel
 * @since 2024/5/24
 */
@Slf4j
public class InheritableThreadLocalDemoTest {

    private static final ThreadLocal<String> current = new InheritableThreadLocal<>();

    @Test
    public void run() throws Exception {
        current.set("parent");

        Semaphore parentSemaphore = new Semaphore(0);
        Semaphore childSemaphore = new Semaphore(0);

        Thread childThread = new Thread(() -> {
            try {
                childSemaphore.acquire();
                log.info("get value: " + current.get());

                childSemaphore.acquire();
                current.set("child");
                log.info("set value: child");

                childSemaphore.acquire();
                log.info("get value: " + current.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Thread controller = new Thread(() -> {
            // 第一步，子线程先获取，验证子线程是否可以继承父线程的ThreadLocal
            childSemaphore.release();
            LockSupport.parkUntil(System.currentTimeMillis() + 1000L);

            // 第二步，子线程更改ThreadLocal中的值
            childSemaphore.release();
            LockSupport.parkUntil(System.currentTimeMillis() + 1000L);

            // 第三步，父线程获取，验证是否受子线程的影响
            parentSemaphore.release();
            LockSupport.parkUntil(System.currentTimeMillis() + 1000L);

            // 第四步，父线程更新ThreadLocal中的值
            parentSemaphore.release();
            LockSupport.parkUntil(System.currentTimeMillis() + 1000L);

            // 第五步，子线程再获取，验证是否受父线程的影响
            childSemaphore.release();
        });

        childThread.start();
        controller.start();

        parentSemaphore.acquire();
        log.info("get value: " + current.get());

        parentSemaphore.acquire();
        current.set("parent-update");
        log.info("set value: parent-update");

        childThread.join();
        controller.join();

    }

}
