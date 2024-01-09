package com.tyrael.kharazim.demo;

/**
 * @author Tyrael Archangel
 * @since 2024/1/9
 */
public class StackTrackDemo {

    public static void main(String[] args) {
        firstMethod();
    }

    private static void firstMethod() {
        secondMethod();
    }

    private static void secondMethod() {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            System.out.println(stackTrace);
        }
    }

}
