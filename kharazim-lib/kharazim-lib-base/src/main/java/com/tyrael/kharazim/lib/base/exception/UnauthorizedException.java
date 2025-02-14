package com.tyrael.kharazim.lib.base.exception;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

}