package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/1/9
 */
public class StackTrackDemoTest {

    @Test
    public void runStackTrack() {
        firstMethod();
    }

    private void firstMethod() {
        secondMethod();
    }

    private void secondMethod() {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            System.out.println(stackTrace);
        }
    }

}
