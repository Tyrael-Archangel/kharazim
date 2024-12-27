package com.tyrael.kharazim.common.exception;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
public class LoginFailedException extends UnauthorizedException {

    public LoginFailedException(String message) {
        super(message);
    }

}
