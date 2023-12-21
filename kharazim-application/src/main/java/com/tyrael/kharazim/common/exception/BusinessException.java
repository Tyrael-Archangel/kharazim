package com.tyrael.kharazim.common.exception;

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

    public static void assertTrue(boolean condition, String msg) {
        if (!condition) {
            throw new BusinessException(msg);
        }
    }
}

