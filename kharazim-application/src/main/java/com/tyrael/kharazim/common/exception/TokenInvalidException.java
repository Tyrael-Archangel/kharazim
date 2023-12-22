package com.tyrael.kharazim.common.exception;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
public class TokenInvalidException extends UnauthorizedException {

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}
