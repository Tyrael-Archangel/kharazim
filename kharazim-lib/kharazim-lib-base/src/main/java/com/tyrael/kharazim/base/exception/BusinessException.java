package com.tyrael.kharazim.base.exception;

import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String msg;

    public BusinessException(String msg) {
        this(msg, null);
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
    }

    /**
     * condition必须为true，否则抛出异常
     */
    public static void assertTrue(boolean condition, String msg) {
        if (!condition) {
            throw new BusinessException(msg);
        }
    }

    /**
     * condition必须为false，否则抛出异常
     */
    public static void assertFalse(boolean condition, String msg) {
        assertTrue(!condition, msg);
    }

}

